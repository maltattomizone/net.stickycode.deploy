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
      switch(count) {
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
    configuration.setWar(new File(args[0]));
    configuration.setPort(new Integer(args[1]));
    if (args.length > 2)
      configuration.setBindAddress(args[2]);
    if (args.length > 3)
      configuration.setWorkingDirectory(new File(args[3]));
    else
      configuration.setWorkingDirectory(new File("tomcat"));

    final TomcatDeployer deployer = new TomcatDeployer(configuration);

    try {
      deployer.deploy();
    }
    catch (RuntimeException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.out.println("CTRL-C to exit");

    Signal.handle(new Signal("INT"), new StopHandler(deployer));
    Signal.handle(new Signal("TERM"), new StopHandler(deployer));
    Signal.handle(new Signal("HUP"), new IgnoreHandler());

    synchronized (deployer) {
      deployer.wait();
    }
  }
}
