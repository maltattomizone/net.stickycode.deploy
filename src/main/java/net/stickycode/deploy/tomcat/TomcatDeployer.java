/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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
package net.stickycode.deploy.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.ExpandWar;
import org.apache.catalina.startup.Tomcat;

public class TomcatDeployer {
  private static class Embedded
      extends Tomcat {
    @Override
    protected void initBaseDir() {
    }
  }

  private Tomcat container;

  private Engine engine;

  private StandardHost host;

  private final DeploymentConfiguration configuration;

  private Path workingDirectory;

  public TomcatDeployer(DeploymentConfiguration configuration) {
    super();
    this.configuration = configuration;
  }

  public void deploy() {
    try {
      FileAttribute<Set<PosixFilePermission>> userOnly = userOnly();
      Files.createDirectories(configuration.getWorkingDirectory(), userOnly);
      this.workingDirectory = Files.createTempDirectory(configuration.getWorkingDirectory(), "tmp", userOnly);
    }
    catch (IOException e1) {
      throw new RuntimeException(e1);
    }
    createContainer();
    createEngine();
    createDefaultHost();
    createContextForWar();
    listenToHttpOnPort();

    try {
      container.start();
    }
    catch (LifecycleException e) {
      throw new FailedToStartDeploymentException(e);
    }

    verifyListening();
  }

  private FileAttribute<Set<PosixFilePermission>> userOnly() {
    return PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
  }

  private void verifyListening() {
    try {
      Socket s = new Socket(configuration.getBindAddress(), configuration.getPort());
      PrintWriter w = new PrintWriter(s.getOutputStream());
      w.print("OPTIONS * HTTP/1.1\r\nHOST: web\r\n\r\n");
      w.flush();
      BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
      String pingResult = r.readLine();
      s.close();
      if (!"HTTP/1.1 200 OK".equals(pingResult))
        throw new FailedToStartDeploymentException("Options test after start returned '" + pingResult + "'");

    }
    catch (UnknownHostException e) {
      throw new FailedToStartDeploymentException(e);
    }
    catch (IOException e) {
      throw new FailedToStartDeploymentException(e);
    }
  }

  private void listenToHttpOnPort() {
    container.setPort(configuration.getPort());
  }

  private void createContextForWar() {
    StandardContext context = new StandardContext();
    context.setDocBase(configuration.getWar().getAbsolutePath());
    context.setPath(configuration.getContextPath());
    context.setLoader(new WebappLoader());
    context.setAntiResourceLocking(false);
    context.setWorkDir(configuration.getWorkingDirectory().toString());

    ContextConfig listener = new ContextConfig();
    listener.setDefaultWebXml("META-INF/sticky/stripped-web.xml");
    context.addLifecycleListener(listener);

    host.addChild(context);
  }

  private void createDefaultHost() {
    host = new StandardHost();
    host.setName("localhost");
    host.setUnpackWARs(false);
    host.setCreateDirs(false);
    engine.addChild(host);
    engine.setDefaultHost(host.getName());
  }

  private void createEngine() {
    engine = container.getEngine();
    engine.setName("t8");
  }

  public void stop() {
    try {
      container.stop();
    }
    catch (LifecycleException e) {
      throw new FailedToStopDeploymentException(e);
    }
    finally {
      ExpandWar.delete(configuration.getWorkingDirectory().toFile());
    }
  }

  private void createContainer() {
    container = new Embedded();
    container.setBaseDir(workingDirectory.toString());
    Service server = container.getService();
    server.setName("t7-container");
    Server service = server.getServer();
    service.setCatalinaHome(workingDirectory.toFile());
    service.setCatalinaBase(workingDirectory.toFile());

  }
}
