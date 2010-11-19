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

import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Embedded;

import net.stickycode.deploy.DeploymentConfiguration;
import net.stickycode.deploy.cli.Main;

public class TomcatDeployer {


  public class EmbeddedDeployer
      extends Embedded {
    public boolean isStarted() {
      return started;
    }
  }

  private EmbeddedDeployer container;
  private Engine engine;
  private StandardHost host;

  private final DeploymentConfiguration configuration;

  public TomcatDeployer(DeploymentConfiguration configuration) {
    super();
    this.configuration = configuration;
  }

  @Main
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
  }

  private void listenToHttpOnPort() {
    Connector connector = container.createConnector(configuration.getBindAddress(), configuration.getPort(), "http");
    container.addConnector(connector);
  }

  private void createContextForWar() {
    StandardContext context = new StandardContext();
    context.setDocBase(configuration.getApplication().getAbsolutePath());
    context.setPath(configuration.getContextPath());
    context.setLoader(new WebappLoader());
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
    host.setName("sticky-host");
    engine.addChild(host);
    engine.setDefaultHost(host.getName());
  }

  private void createEngine() {
    engine = container.createEngine();
    engine.setName("sticky-" + System.currentTimeMillis());
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