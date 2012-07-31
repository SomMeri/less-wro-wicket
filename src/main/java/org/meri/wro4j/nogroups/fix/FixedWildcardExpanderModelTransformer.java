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
package org.meri.wro4j.nogroups.fix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.model.group.Group;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.transformer.WildcardExpanderModelTransformer;
import ro.isdc.wro.util.Function;

public class FixedWildcardExpanderModelTransformer extends WildcardExpanderModelTransformer {

  private static final Logger LOG = LoggerFactory.getLogger(FixedWildcardExpanderModelTransformer.class);

  public Function<Collection<File>, Void> createExpanderHandler(final Group group, final Resource resource,
      final String baseNameFolder) {
      LOG.debug("createExpanderHandler using baseNameFolder: {}\n for resource {}", baseNameFolder, resource);
      final Function<Collection<File>, Void> handler = new Function<Collection<File>, Void>() {
        public Void apply(final Collection<File> files) {
          if (baseNameFolder == null) {
            // replacing group with empty list since the original uri has no associated resources.
            //No BaseNameFolder found
            LOG.warn("The resource {} is probably invalid, removing it from the group.", resource);
            group.replace(resource, new ArrayList<Resource>());
          } else {
            final List<Resource> expandedResources = new ArrayList<Resource>();
            LOG.debug("baseNameFolder: {}", baseNameFolder);
            for (final File file : files) {
              final String resourcePath = getFullPathNoEndSeparator(resource);
              LOG.debug("\tresourcePath: {}", resourcePath);
              LOG.debug("\tfile path: {}", file.getPath());
              final String computedResourceUri = resourcePath
                + StringUtils.removeStart(file.getPath(), baseNameFolder).replace('\\', '/');

              final Resource expandedResource = Resource.create(computedResourceUri, resource.getType());
              LOG.debug("\texpanded resource: {}", expandedResource);
              expandedResources.add(expandedResource);
            }
            LOG.debug("\treplace resource {}", resource);
            group.replace(resource, expandedResources);
          }
          return null;
        }

    private String getFullPathNoEndSeparator(final Resource resource) {
      String result = FilenameUtils.getFullPathNoEndSeparator(resource.getUri());
      if (result!=null && 1==result.length() && 0==FilenameUtils.indexOfLastSeparator(result))
        return "";
      
      return result;
    }
      };
      return handler;
    }

}
