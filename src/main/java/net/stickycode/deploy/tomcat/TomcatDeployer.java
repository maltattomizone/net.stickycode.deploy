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
package net.stickycode.deploy.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Loader;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Embedded;

public class TomcatDeployer {

  public class EmbeddedDeployer
      extends Embedded {

    boolean started = false;

    public boolean isStarted() {
      return started;
    }

    @Override
    protected void startInternal()
        throws LifecycleException {
      super.startInternal();
      started = true;
    }
  }

  private EmbeddedDeployer container;

  private Engine engine;

  private StandardHost host;

  private final Loader loader;

  private final DeploymentConfiguration configuration;

  public TomcatDeployer(DeploymentConfiguration configuration, Loader loader) {
    super();
    this.loader = loader;
    this.configuration = configuration;
  }

  public void deploy() {
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

    if (!container.isStarted())
      throw new FailedToStartDeploymentException();

    verifyListening();
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
    Connector connector = container.createConnector(configuration.getBindAddress(), configuration.getPort(), "http");
    container.addConnector(connector);
  }

  private void createContextForWar() {
    StandardContext context = new StandardContext();
    context.setDocBase(configuration.getDocumentBase());
    context.setPath(configuration.getContextPath());
    context.setResources(new EmbeddedResources());
    context.setLoader(loader);
    context.setProcessTlds(false);
    context.setTldNamespaceAware(false);
    context.setAntiResourceLocking(false);

    ContextConfig listener = new ContextConfig();
    listener.setDefaultWebXml("META-INF/sticky/stripped-web.xml");
    context.addLifecycleListener(listener);

    host.addChild(context);
  }

  private void createDefaultHost() {
    host = new StandardHost();
    host.setName("sticky-host");
    host.setUnpackWARs(false);
    host.setCreateDirs(false);
    engine.addChild(host);
    engine.setDefaultHost(host.getName());
  }

  private void createEngine() {
    engine = container.createEngine();
    engine.setName("sticky-" + System.currentTimeMillis());
    engine.setService(container);
    container.addEngine(engine);
  }

  public void stop() {
    try {
      if (container != null)
        container.stop();
    }
    catch (LifecycleException e) {
      throw new FailedToStopDeploymentException(e);
    }
  }

  private void createContainer() {
    container = new EmbeddedDeployer();
    container.setName("sticky-container");
    container.setUseNaming(true);
    container.setCatalinaHome(configuration.getWorkingDirectory().getAbsolutePath());
    container.setCatalinaBase(configuration.getWorkingDirectory().getAbsolutePath());
  }
}
