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

import net.stickycode.deploy.cli.StickyCommandLine;
import net.stickycode.deploy.tomcat.TomcatDeployer;
import net.stickycode.deploy.tomcat.TomcatShutdownHandler;

public class Deploy {

  public static void main(String[] args) throws InterruptedException {
    StickyCommandLine cli = new StickyCommandLine(args);
    DeploymentConfiguration configuration = new DeploymentConfiguration();
    cli.configure(configuration);
    final TomcatDeployer deployer = new TomcatDeployer(configuration);
    cli.launch(deployer, new TomcatShutdownHandler(deployer));
  }

}
