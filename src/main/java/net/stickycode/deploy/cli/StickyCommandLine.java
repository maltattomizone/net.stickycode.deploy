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
package net.stickycode.deploy.cli;

import java.util.Arrays;
import java.util.HashSet;

import net.stickycode.coercion.Coercions;
import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.configured.ConfigurationSource;
import net.stickycode.configured.ConfigurationSystem;
import net.stickycode.configured.ConfiguredConfiguration;
import net.stickycode.configured.ConfiguredFieldProcessor;
import net.stickycode.configured.FieldNameKeyBuilder;
import net.stickycode.configured.InlineConfigurationRepository;
import net.stickycode.configured.InvokingAnnotatedMethodProcessor;
import net.stickycode.configured.source.EnvironmentConfigurationSource;
import net.stickycode.configured.source.SystemPropertiesConfigurationSource;
import net.stickycode.deploy.signal.StickyShutdownHandler;
import net.stickycode.deploy.signal.StickySignalTrap;
import net.stickycode.reflector.Reflector;

public class StickyCommandLine {

  private final ConfigurationSystem configurationSystem;
  private final ConfigurationRepository configurationRepository = new InlineConfigurationRepository();

  public StickyCommandLine(String... args) {
    this.configurationSystem = new BeanBuilder<ConfigurationSystem>(ConfigurationSystem.class)
        .inject(new HashSet<ConfigurationSource>(Arrays.asList(
            new CommandLineConfigurationSource(args),
            new SystemPropertiesConfigurationSource(),
            new EnvironmentConfigurationSource())))
        .inject(new Coercions())
        .inject(configurationRepository)
        .inject(new FieldNameKeyBuilder())
        .build();
  }

  public void launch(Object application, StickyShutdownHandler shutdownHandler) {
    new Reflector()
        .forEachMethod(new InvokingAnnotatedMethodProcessor(Main.class))
        .process(application);

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
    ConfiguredConfiguration configuration = new ConfiguredConfiguration(target);
    new Reflector()
        .forEachField(new ConfiguredFieldProcessor(configuration))
        .process(target);
    configurationRepository.register(configuration);
    configurationSystem.configure();
  }

  public void execute(Object deployer) {
  }

}
