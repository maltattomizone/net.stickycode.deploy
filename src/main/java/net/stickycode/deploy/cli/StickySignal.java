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

public class StickySignal {

  public enum Code {
    INT,
    HUP,
    TERM,
    ALRM,
    PIPE,
    POLL,
    PROF,
    USR1,
    USR2,
    VTALRM,
    ABRT,
    FPE,
    ILL,
    QUIT
  }

  private final Code code;
  private final int number;

  public StickySignal(String name, int number) {
    this.code = Code.valueOf(name);
    this.number = number;
  }

  public Code getCode() {
    return code;
  }

  public int getNumber() {
    return number;
  }

}
