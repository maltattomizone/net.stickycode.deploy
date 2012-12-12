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

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.naming.NamingEntry;
import org.apache.naming.resources.BaseDirContext;
import org.apache.naming.resources.Resource;
import org.apache.naming.resources.ResourceAttributes;

public class EmbeddedResources
    extends BaseDirContext
    implements DirContext {

  @Override
  public void unbind(String name)
      throws NamingException {
  }

  @Override
  public void rename(String oldName, String newName)
      throws NamingException {
  }

  @Override
  public NamingEnumeration list(String name)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public void destroySubcontext(String name)
      throws NamingException {
  }

  @Override
  public Object lookupLink(String name)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public String getNameInNamespace()
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public void modifyAttributes(String name, int mod_op, Attributes attrs)
      throws NamingException {
  }

  @Override
  public void modifyAttributes(String name, ModificationItem[] mods)
      throws NamingException {
  }

  @Override
  public void bind(String name, Object obj, Attributes attrs)
      throws NamingException {
  }

  @Override
  public void rebind(String name, Object obj, Attributes attrs)
      throws NamingException {
  }

  @Override
  public DirContext createSubcontext(String name, Attributes attrs)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public DirContext getSchema(String name)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public DirContext getSchemaClassDefinition(String name)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public NamingEnumeration search(String name, Attributes matchingAttributes, String[] attributesToReturn)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public NamingEnumeration search(String name, Attributes matchingAttributes)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public NamingEnumeration search(String name, String filter, SearchControls cons)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  public NamingEnumeration search(String name, String filterExpr, Object[] filterArgs, SearchControls cons)
      throws NamingException {
    throw new OperationNotSupportedException();
  }

  @Override
  protected Attributes doGetAttributes(String name, String[] attrIds)
      throws NamingException {
    ResourceAttributes resourceAttributes = new ResourceAttributes();
    resourceAttributes.setName(name);
    return resourceAttributes;
  }

  @Override
  protected String doGetRealPath(String arg0) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  protected List<NamingEntry> doListBindings(String name)
      throws NamingException {
    InputStream in = getClass().getClassLoader().getResourceAsStream(name);
    if (in == null)
      return Collections.emptyList();

    return Collections.singletonList(new NamingEntry(name, new Resource(in), NamingEntry.ENTRY));
  }

  @Override
  protected Object doLookup(String name) {
    return getClass().getClassLoader().getResourceAsStream(name.substring(1));
  }

}
