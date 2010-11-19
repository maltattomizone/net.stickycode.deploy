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

import net.stickycode.stereotype.Configured;

public class DeploymentConfiguration {

  @Configured
  private File application;

  @Configured
  private Integer port = 8080;

  @Configured
  private String bindAddress = "localhost";

  @Configured
  private String contextPath = "";

  @Configured
  private File workingDirectory = new File(System.getProperty("user.dir"));

  public DeploymentConfiguration() {
    super();
  }

  public File getApplication() {
    return application;
  }

  public int getPort() {
    return port;
  }

  public String getBindAddress() {
    return bindAddress;
  }

  public String getContextPath() {
    return contextPath;
  }

  public File getWorkingDirectory() {
    return workingDirectory;
  }
}
