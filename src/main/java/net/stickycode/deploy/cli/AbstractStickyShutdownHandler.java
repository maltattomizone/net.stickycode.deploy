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

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractStickyShutdownHandler
    implements StickySignalHandler, StickyShutdownHandler {

  private AtomicInteger stopping = new AtomicInteger(0);

  public void handle(StickySignal s) {
    int count = stopping.incrementAndGet();
    switch (count) {
    case 1:
      shutdown(s);
    case 2:
      forceShutdownWarning(s);
      break;
    default:
      forceShutdown(s);
    }
  }

  protected void forceShutdown(StickySignal s) {
    System.err.println("Forcing exit without proper shutdown on " + s.getCode());
    forceShutdown();
    System.exit(1);
  }

  @Override
  public void forceShutdown() {
  }

  protected void forceShutdownWarning(StickySignal s) {
    System.err.println("Third time is the charm of the impatient. I will force the exit next time you " + s.getCode());
    forceShutdownWarning();
  }

  @Override
  public void forceShutdownWarning() {
  }

  protected void shutdown(StickySignal s) {
    System.out.println("Cleanly shutting down on " + s.getCode());
    shutdown();
    System.exit(0);
  }
}
