package net.stickycode.deploy.cli;

import net.stickycode.deploy.signal.AbstractStickyShutdownHandler;
import net.stickycode.deploy.signal.StickySignalTrap;


public class ManualTrapChecker {

  public static void main(String[] args) {
    System.out.println("Running...");
    StickySignalTrap trap = new StickySignalTrap();
    trap.shutdown(new AbstractStickyShutdownHandler() {
      
      @Override
      public void shutdown() {
        throw new UnsupportedOperationException("not implemented");
      }
    });
    trap.noHangup();
    trap.waitForExit();
  }
  
}
