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

import net.stickycode.deploy.signal.StickySignalTrap;

public class EmbeddedTomcat {

  public static void main(String[] args) {
    System.out.println("Configuring Embedded Tomcat ");
    DeploymentConfiguration configuration = new EmbeddedDeploymentConfiguration();
    final TomcatDeployer deployer = new TomcatDeployer(configuration, new EmbeddedWebappLoader());
    System.out.println("Starting Embedded Tomcat");
    launch(deployer, new TomcatShutdownHandler(deployer));
  }

  private static void launch(TomcatDeployer deployer, TomcatShutdownHandler tomcatShutdownHandler) {
    long time = System.currentTimeMillis();
    deployer.deploy();
    System.out.println("started in " + (System.currentTimeMillis() - time) + "ms");
    System.out.println("CTRL-C to exit");

    StickySignalTrap trap = signalTrap();
    trap.shutdown(tomcatShutdownHandler);
    trap.noHangup();
    trap.waitForExit();
  }

  public static StickySignalTrap signalTrap() {
    return new StickySignalTrap();
  }

}
