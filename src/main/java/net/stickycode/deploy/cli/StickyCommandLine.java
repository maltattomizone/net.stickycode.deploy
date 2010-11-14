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
package net.stickycode.deploy.cli;

import net.stickycode.configured.ConfigurationSystem;
import net.stickycode.configured.source.EnvironmentConfigurationSource;
import net.stickycode.configured.source.SystemPropertiesConfigurationSource;
import net.stickycode.reflector.Reflector;


public class StickyCommandLine {

  private final ConfigurationSystem configuration = new ConfigurationSystem();

  public StickyCommandLine(String[] args) {
    configuration.add(new CommandLineConfigurationSource(args));
    configuration.add(new SystemPropertiesConfigurationSource());
    configuration.add(new EnvironmentConfigurationSource());
  }

  public void launch(Object application, StickyShutdownHandler shutdownHandler) {
    System.out.println("CTRL-C to exit");

    StickySignalTrap trap = signalTrap();
    trap.shutdown(shutdownHandler);
    trap.noHangup();
    trap.waitForExit();
  }

  public void waitForExit() throws InterruptedException {
    synchronized (this) {
      this.wait();
    }
  }

  public StickySignalTrap signalTrap() {
    return new StickySignalTrap();
  }

  public void configure(Object target) {
    Reflector reflector = new Reflector();
    reflector.forEachField(new OptionFieldProcessor(configuration));
    reflector.process(target);
    configuration.configure();
  }

  public void execute(Object deployer) {
  }

}
