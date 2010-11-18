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

import java.lang.reflect.Field;

import net.stickycode.exception.PermanentException;


@SuppressWarnings("serial")
public class ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossible
    extends PermanentException {

  public ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossible(Object target, Field field) {
    super("The field '{}' on '{}' is a primitive type '{}'. In order to provide a simple convention default values " +
    		"for configured fields are specified field initialisers, this means that primitive fields default values cannot be determined." +
    		"I chose to make this an error to ensure that the developer has thought carefully about whether or not a value should have a default. "
    		, field.getName(), target.getClass().getName(), field.getType().getSimpleName());
  }

}
