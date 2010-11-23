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
package net.stickycode.deploy.sample.babysteps;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class BabyStepsTest {

  @Test
  public void steps() {
    BabySteps s = new BabySteps();
    assertThat(s.crawl()).isEqualTo("crawling is step 1");
    assertThat(s.walk()).isEqualTo("walking is step 2");
    assertThat(s.run()).isEqualTo("running is step 3");
  }
}
