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

import org.junit.Test;

import net.stickycode.configured.ConfigurationValueNotFoundForKeyException;
import net.stickycode.stereotype.Configured;

import static org.fest.assertions.Assertions.assertThat;


public class StickyCommandLineTest {

  private static class Sample {
    @Configured
    String value;

    @Configured
    Boolean doSomething = false;
  }

  @Test(expected=ConfigurationValueNotFoundForKeyException.class)
  public void noConfiguration() {
    sample();
  }

  @Test
  public void booleanConfiguration() {
    Sample target = sample("--value");
    assertThat(target.value).isEqualTo("true");
    assertThat(target.doSomething).isFalse();
  }

  @Test
  public void configurationValue() {
    Sample target = sample("--value=blah", "--doSomething");
    assertThat(target.value).isEqualTo("blah");
    assertThat(target.doSomething).isTrue();
  }

  @Test
  public void reversedBoolean() {
    Sample target = sample("--value=blah", "--no-doSomething");
    assertThat(target.value).isEqualTo("blah");
    assertThat(target.doSomething).isFalse();
  }

  private Sample sample(String... args) {
    Sample target = new Sample();
    new StickyCommandLine(args).configure(target);
    return target;
  }

}
