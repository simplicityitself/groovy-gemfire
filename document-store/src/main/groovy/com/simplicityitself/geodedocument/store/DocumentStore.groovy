package com.simplicityitself.geodedocument.store

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.PartitionAttributes
import com.gemstone.gemfire.cache.RegionShortcut
import com.gemstone.gemfire.cache.Scope
import com.gemstone.gemfire.cache.query.QueryService
import com.gemstone.gemfire.internal.tools.gfsh.app.command.task.data.PartitionAttributeInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@Slf4j
class DocumentStore {

  @Autowired
  Cache cache

  QueryService queryService

  @PostConstruct
  void populateQueryService() {
    queryService = cache.queryService
  }

  def findById(String region, String id) {
    def regionMap = cache.getRegion(region)
    if (regionMap == null) {
      regionMap = cache.createRegionFactory(RegionShortcut.PARTITION_REDUNDANT).create(region)
      log.warn("Created new Region[${region}] - Partitioned|Redundant")
    }
    return regionMap[id]
  }

  def putById(String region, String id, def object) {

    cache.getRegion("region")
  }

  def query(String query) {

  }
}
