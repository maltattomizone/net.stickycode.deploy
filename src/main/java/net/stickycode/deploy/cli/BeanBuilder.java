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

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import net.stickycode.reflector.AnnotatedFieldSettingProcessor;
import net.stickycode.reflector.Reflector;
import net.stickycode.reflector.ValueSource;

public class BeanBuilder<T> {

  private final class Dependencies
      implements ValueSource {

    Collection<Object> values = new HashSet<Object>();

    @Override
    public Object get(Class<?> type) {
      for (Object value : values)
        if (type.isAssignableFrom(value.getClass()))
          return value;

      throw new MissingBeanException(type, values);
    }

    public void add(Object object) {
      values.add(object);
    }
  }

  private Class<T> type;
  private T instance;
  private Dependencies dependencies = new Dependencies();

  public BeanBuilder(Class<T> type) {
    this.type = type;
  }

  private T construct() {
    try {
      return type.newInstance();
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public BeanBuilder<T> inject(Object mock) {
    dependencies.add(mock);
    return this;
  }

  public T build() {
    T instance = construct();
    new Reflector()
        .forEachField(new AnnotatedFieldSettingProcessor(dependencies, Inject.class))
        .process(instance);
    return instance;
  }
}
