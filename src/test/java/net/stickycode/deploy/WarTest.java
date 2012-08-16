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

import org.junit.Test;

import net.stickycode.deploy.tomcat.DeploymentConfiguration;
import net.stickycode.deploy.tomcat.TomcatDeployer;

public class WarTest {

  @Test
  public void war() {
    DeploymentConfiguration configuration = new DeploymentConfiguration();
    configuration.setWorkingDirectory(new File("target/tomcat"));
    configuration.setPort(9999);
    configuration
        .setWar(new File(
            "target/sticky-helloworld-war-1.3-application.war"));
    TomcatDeployer deployer = new TomcatDeployer(configuration);
    deployer.deploy();
    deployer.stop();
  }
}
