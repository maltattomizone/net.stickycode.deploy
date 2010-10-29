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
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Embedded;
import org.junit.Test;

public class WarTest {

  @Test
  public void war() throws LifecycleException, IOException, ServletException, InterruptedException {
    Embedded embedded = createContainer();

    Engine engine = embedded.createEngine();
    engine.setName("sticky-engine");

    StandardHost host = new StandardHost();
    host.setAppBase("/tmp/ug");
    host.setName("sticky-host");
    host.setUnpackWARs(false);
    host.setName("sticky-host");

    engine.setDefaultHost(host.getName());

    File warPath = new File("/home/michael/.m2/repository/net/stickycode/examples/sticky-helloworld-war/1.2/sticky-helloworld-war-1.2-application.war");

    StandardContext context = new StandardContext();
    context.setDocBase(warPath.getAbsolutePath());
    context.setPath("");
    context.setLoader(new WebappLoader());
    context.setProcessTlds(false);
    context.setTldNamespaceAware(false);
    context.setAntiResourceLocking(false);
//    context.setDefaultWebXml("META-INF/sticky/stripped-web.xml");

    ContextConfig listener = new ContextConfig();
    listener.setDefaultWebXml("META-INF/sticky/stripped-web.xml");
    context.addLifecycleListener(listener);

    host.addChild(context);

    engine.addChild(host);

    embedded.addEngine(engine);

    Connector connector = embedded.createConnector("localhost", 9999, "http");
    embedded.addConnector(connector);

    embedded.start();
    Thread.sleep(10000);
    embedded.stop();
  }

  private Embedded createContainer() {
    Embedded embedded = new Embedded();
    embedded.setName("sticky-container");
    embedded.setUseNaming(true);
    return embedded;
  }

}
