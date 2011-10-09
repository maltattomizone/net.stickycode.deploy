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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

import net.stickycode.deploy.bootstrap.StickyEmbedder;

public class EmbeddedTomcatFunctionalTest {

  @Test
  public void helloWorld() {
    assertThat(System.getProperty("buildDirectory"))
        .describedAs("in eclipse set -DbuildDirectory=target-eclipse as a default VM argument for the JRE used to run tests")
        .isNotNull();
    StickyEmbedder e = new StickyEmbedder("--debug") {

      @Override
      protected File deriveApplicationFile() {
        return new File(System.getProperty("buildDirectory")
            + "/dependency/sticky-helloworld-war-application.war");
      }
    };
    e.initialise();
    // e.launch();

  }

}
