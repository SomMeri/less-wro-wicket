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

import java.util.List;

import org.meri.wro4j.nogroups.fix.FixedWildcardExpanderModelTransformer;

import ro.isdc.wro.extensions.manager.ExtensionsConfigurableWroManagerFactory;
import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.group.GroupExtractor;
import ro.isdc.wro.util.Transformer;

public class ConfigurationFreeManagerFactory extends ExtensionsConfigurableWroManagerFactory {

  @Override
  protected GroupExtractor newGroupExtractor() {
    return new ConfigurationFreeGroupExtractor();
  }

  @Override
  protected WroModelFactory newModelFactory() {
    return new ConfigurationFreeModelFactory();
  }

  @Override
  protected List<Transformer<WroModel>> newModelTransformers() {
    //We need to return the list of correct transformersand have them as  
    //a private property of the super class.
    List<Transformer<WroModel>> modelTransformers = 
        super.newModelTransformers();
    //replace default WildcardExpanderModelTransformer with FixedWildcardExpanderModelTransformer
    modelTransformers.clear();
    addModelTransformer(new FixedWildcardExpanderModelTransformer());
    addModelTransformer(new ResourcesToGroupsModelTransformer());
    return modelTransformers;
  }
}
