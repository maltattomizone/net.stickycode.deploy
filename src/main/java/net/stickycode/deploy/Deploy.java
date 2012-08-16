/**
 * Copyright (c) 2010 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import net.stickycode.deploy.tomcat.DeploymentConfiguration;
import net.stickycode.deploy.tomcat.TomcatDeployer;

public class Deploy {

  private static final class IgnoreHandler
      implements SignalHandler {

    @Override
    public void handle(Signal s) {
      System.out.println("Ignoring " + s.getName());
    }

  }

  private static final class StopHandler
      implements SignalHandler {

    private final TomcatDeployer deployer;
    private AtomicInteger stopping = new AtomicInteger(0);

    private StopHandler(TomcatDeployer deployer) {
      this.deployer = deployer;
    }

    public void handle(Signal s) {
      int count = stopping.incrementAndGet();
      switch (count) {
      case 1:
        System.out.println("Cleanly shutting down on " + s.getName());
        deployer.stop();
        System.exit(0);
      case 2:
        System.err.println("Third time is the charm of the impatient. I will force the exit next time you " + s.getName());
        break;
      default:
        System.err.println("Forcing exit without proper shutdown on " + s.getName());
        System.exit(1);
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    DeploymentConfiguration configuration = new DeploymentConfiguration();
    File war = new File(args[0]);
    if (!war.canRead()) {
      System.err.println("War not found " + war.getAbsolutePath());
      System.exit(78);
    }
    configuration.setWar(war);
    configuration.setPort(new Integer(args[1]));
    if (args.length > 2)
      configuration.setBindAddress(args[2]);
    if (args.length > 3)
      configuration.setWorkingDirectory(new File(args[3]));
    else
      configuration.setWorkingDirectory(new File("tomcat"));

    if (args.length > 4)
      validatePidPath(args);

    if (args.length > 5)
      loadSystemProperties(args[5]);

    final TomcatDeployer deployer = new TomcatDeployer(configuration);

    try {

      deployer.deploy();

      if (args.length > 4) {
        writePid(args);
      }
    }
    catch (RuntimeException e) {
      e.printStackTrace();
      System.exit(77);
    }

    System.out.println("CTRL-C to exit");

    Signal.handle(new Signal("INT"), new StopHandler(deployer));
    Signal.handle(new Signal("TERM"), new StopHandler(deployer));
    Signal.handle(new Signal("HUP"), new IgnoreHandler());

    synchronized (deployer) {
      deployer.wait();
    }
  }

  private static void validatePidPath(String[] args) {
    File pidFile = new File(args[4]);
    if (pidFile.exists()) {
      System.err.println("Pid already file exists '" + args[4] + "'");
      System.exit(80);
    }

    File parentFile = pidFile.getParentFile();
    if (parentFile != null && !parentFile.canWrite()) {
      System.err.println("Pid directory " + args[4] + " is not writable");
      System.exit(80);
    }
  }

  private static void loadSystemProperties(String path) {
    File configuration = new File(path);
    if (!configuration.canRead()) {
      System.err.println("Cannot read " + path);
      System.exit(79);
    }

    try {
      InputStream i = new FileInputStream(path);
      Properties p = new Properties(System.getProperties());
      p.load(i);
      i.close();
      System.setProperties(p);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void writePid(String[] args) {
    try {
      String name = ManagementFactory.getRuntimeMXBean().getName();
      String pid = name.substring(0, name.indexOf('@'));
      FileWriter w = new FileWriter(new File(args[4]));
      w.write(pid);
      w.close();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
