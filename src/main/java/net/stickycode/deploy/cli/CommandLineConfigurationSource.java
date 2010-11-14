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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.stickycode.configured.ConfigurationSource;

public class CommandLineConfigurationSource
    implements ConfigurationSource {

  private final Map<String, String> options = new HashMap<String, String>();
  private final Collection<String> commands = new LinkedList<String>();

  public CommandLineConfigurationSource(String... args) {
    super();
    for (String s : args) {
      if (s.startsWith("--"))
        addOption(s.substring(2));
      else
        addCommand(s);
    }

  }

  private void addOption(String option) {
    int split = option.indexOf('=');
    if (split == -1)
      addOption(option, null);
    else
      addOption(
          option.substring(0, split),
          option.substring(split + 1));
  }

  private void addOption(String key, String value) {
    options.put(key.replace('-', '.'), value);
  }

  private void addCommand(String s) {
    if (!Character.isJavaIdentifierStart(s.charAt(0)))
      throw new InvalidCommandException(s, s.charAt(0));

    for (char c : s.toCharArray()) {
      if (!Character.isJavaIdentifierPart(c))
        throw new InvalidCommandException(s, c);
    }

    commands.add(s);
  }

  @Override
  public boolean hasValue(String key) {
    return options.containsKey(key);
  }

  @Override
  public String getValue(String key) {
    return options.get(key);
  }

}
