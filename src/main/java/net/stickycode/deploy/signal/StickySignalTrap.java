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
package net.stickycode.deploy.signal;


import sun.misc.Signal;
import sun.misc.SignalHandler;

import net.stickycode.deploy.signal.StickySignal.Code;


public class StickySignalTrap {

  public class StickySignalHandlerAdapter
      implements SignalHandler {

    StickySignalHandler handler;
    SignalHandler next;

    public StickySignalHandlerAdapter(StickySignalHandler stopHandler) {
      this.handler = stopHandler;
    }

    @Override
    public void handle(Signal s) {
      handler.handle(new StickySignal(s.getName(), s.getNumber()));
      if (next != null)
        next.handle(s);
    }

    public void setNext(SignalHandler next) {
      this.next = next;
    }

  }

  public StickySignalTrap shutdown(StickyShutdownHandler stopHandler) {
    trap(Code.INT, stopHandler);
    trap(Code.TERM, stopHandler);
    return this;
  }

  private void trap(Code code, StickySignalHandler stopHandler) {
    StickySignalHandlerAdapter adapter = new StickySignalHandlerAdapter(stopHandler);
    SignalHandler next = Signal.handle(new Signal(code.name()), adapter);
    if (next != SignalHandler.SIG_DFL && next != SignalHandler.SIG_IGN)
      adapter.setNext(next);
  }

  public void noHangup() {
    Signal.handle(new Signal(Code.HUP.name()), SignalHandler.SIG_IGN);
  }

  public void waitForExit() {
    try {
      synchronized (this) {
        this.wait();
      }
    }
    catch (InterruptedException e) {
      throw new WaitingMainThreadWokenUp(e);
    }
  }


}
