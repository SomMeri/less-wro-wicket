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

import javax.servlet.http.HttpServletRequest;

import ro.isdc.wro.model.group.DefaultGroupExtractor;
import ro.isdc.wro.model.group.GroupExtractor;
import ro.isdc.wro.model.resource.ResourceType;

public class ConfigurationFreeGroupExtractor implements GroupExtractor {

  private final DefaultGroupExtractor defaultExtractor = 
      new DefaultGroupExtractor();

  /**
   * Everything that follows context path is considered a group name.
   */
  public String getGroupName(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String uri = getUri(request);
    if (uri.startsWith(contextPath))
      uri = uri.substring(contextPath.length());
    
    return uri;
  }

  private String getUri(HttpServletRequest request) {
    // exactly the same implementation as in the default group extractor
    String includeUriPath = (String) 
        request.getAttribute(DefaultGroupExtractor.ATTR_INCLUDE_PATH);
    String uri = request.getRequestURI();
    uri = includeUriPath != null ? includeUriPath : uri;
    return uri;
  }

  /**
   * If the default extractor is unable to find the resource type,
   * check whether it is a .less file. Less files are considered 
   * style sheets.
   */
  public ResourceType getResourceType(HttpServletRequest request) {
    ResourceType resourceType = 
        defaultExtractor.getResourceType(request);
    
    if (resourceType==null && isLessFile(request)) {
      resourceType = ResourceType.CSS;
    }
    return resourceType;
  }

  private boolean isLessFile(HttpServletRequest request) {
    return request.getRequestURI().toUpperCase().endsWith(".LESS");
  }

  public boolean isMinimized(HttpServletRequest request) {
    return defaultExtractor.isMinimized(request);
  }

  public String encodeGroupUrl(String groupName, ResourceType resourceType, boolean minimize) {
    return groupName;
  }

}
