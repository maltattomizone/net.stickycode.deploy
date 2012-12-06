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
package net.stickycode.deploy.tomcat;

import java.beans.PropertyChangeListener;

import org.apache.catalina.Container;
import org.apache.catalina.Loader;


public class EmbeddedWebappLoader
    implements Loader {

  private Container container;

  @Override
  public void backgroundProcess() {
  }

  @Override
  public ClassLoader getClassLoader() {
    return EmbeddedWebappLoader.class.getClassLoader();
  }

  @Override
  public Container getContainer() {
    return container;
  }

  @Override
  public void setContainer(Container container) {
    this.container = container;
  }

  @Override
  public boolean getDelegate() {
    return false;
  }

  @Override
  public void setDelegate(boolean delegate) {
  }

  @Override
  public String getInfo() {
    return "StickEmbeddedWebappLoader/X"; // XXX load the version from pom
  }

  @Override
  public boolean getReloadable() {
    return false;
  }

  @Override
  public void setReloadable(boolean reloadable) {
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
  }

  @Override
  public void addRepository(String repository) {
  }

  @Override
  public String[] findRepositories() {
    return new String[0];
  }

  @Override
  public boolean modified() {
    return false;
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
  }

}
