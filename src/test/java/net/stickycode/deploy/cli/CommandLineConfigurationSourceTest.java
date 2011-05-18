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

import static org.fest.assertions.Assertions.assertThat;


public class CommandLineConfigurationSourceTest {

  @Test
  public void noParameters() {
    assertThat(source().hasValue("bob")).isFalse();
    assertThat(source().getValue("bob")).isNull();
    assertThat(source().getCommands()).isEmpty();
  }

  @Test
  public void keyOnly() {
    assertThat(source("--bob").hasValue("bob")).isTrue();
    assertThat(source("--bob").getValue("bob")).isEqualTo("true");
  }

  @Test
  public void keysOnly() {
    assertThat(source("--bob", "--alice").hasValue("bob")).isTrue();
    assertThat(source("--bob", "--alice").hasValue("alice")).isTrue();
    assertThat(source("--alice", "--bob").getValue("bob")).isEqualTo("true");
    assertThat(source("--alice", "--bob").getValue("alice")).isEqualTo("true");
  }

  @Test
  public void negatedKeyOnly() {
    assertThat(source("--no-bob").hasValue("bob")).isTrue();
    assertThat(source("--no-bob").getValue("bob")).isEqualTo("false");
    assertThat(source("--no-bob", "--no-alice").getValue("alice")).isEqualTo("false");
  }

  @Test
  public void value() {
    assertThat(source("--bob=alice").hasValue("bob")).isTrue();
    assertThat(source("--bob=alice").getValue("bob")).isEqualTo("alice");
  }

  @Test
  public void valueWithHyphens() {
    assertThat(source("--bob-parker=alice").hasValue("bob.parker")).isTrue();
    assertThat(source("--bob-parker=alice").getValue("bob.parker")).isEqualTo("alice");
    assertThat(source("--bob-parker=alice", "--bob").getValue("bob.parker")).isEqualTo("alice");
  }

  @Test(expected=DuplicatedCommandLineOptionException.class)
  public void multipleSettingsForOneKeyFail() {
    source("--bob", "--bob");
  }

  @Test(expected=InvalidCommandException.class)
  public void invalidCommand() {
    source("abc-asdf");
  }

  @Test(expected=InvalidCommandException.class)
  public void hashInCommand() {
    source("abc#asdf");
  }

  @Test(expected=InvalidCommandException.class)
  public void invalidCommandStart() {
    source("#asdf");
  }

  @Test
  public void commands() {
    assertThat(source("runit").hasValue("runit")).isFalse();
    assertThat(source("runit").getCommands()).containsOnly("runit");
    assertThat(source("runit", "itrun").getCommands()).containsOnly("runit", "itrun");

    source("ab$c", "ab_cd", "a$", "c_", "_d"); // although if you wrote code that this would work for, well, you should go back to perl
  }

  private CommandLineConfigurationSource source(String... args) {
    return new CommandLineConfigurationSource(args);
  }

}
