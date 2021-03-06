/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.meri.wro4j.nogroups;

import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.group.Group;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.ResourceType;

public class ConfigurationFreeModelFactory implements WroModelFactory {
  
  public WroModel create() {
    WroModel result = new WroModel();
    result.addGroup(createAllResourcesGroup());
    return result;
  }

  private Group createAllResourcesGroup() {
    Resource allLess = new Resource();
    allLess.setType(ResourceType.CSS);
    allLess.setUri("/**.less");

    Resource allCss = new Resource();
    allCss.setType(ResourceType.CSS);
    allCss.setUri("/**.css");

    Group group = new Group("fake");
    group.addResource(allCss);
    group.addResource(allLess);
    return group;
  }

  public void destroy() {
  }

}
