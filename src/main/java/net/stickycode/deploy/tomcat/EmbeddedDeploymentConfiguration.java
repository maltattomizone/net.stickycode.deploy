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

import java.io.File;

public class EmbeddedDeploymentConfiguration
    implements DeploymentConfiguration {

  private Integer port = 8080;

  private String bindAddress = "localhost";

  private String contextPath = "";

  private File workingDirectory = new File(System.getProperty("user.dir"));

  public EmbeddedDeploymentConfiguration() {
    super();
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public String getBindAddress() {
    return bindAddress;
  }

  @Override
  public String getContextPath() {
    return contextPath;
  }

  @Override
  public File getWorkingDirectory() {
    return workingDirectory;
  }

  @Override
  public String getDocumentBase() {
    return "embedded";
  }

}
