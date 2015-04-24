package com.simplicityitself.geodedocument.store

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.Region
import com.gemstone.gemfire.cache.RegionShortcut
import com.gemstone.gemfire.cache.query.QueryService
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
    return getRegion(region)[id]
  }

  def putById(String region, String id, def object) {
    getRegion(region).put(id, object)
  }

  Collection query(String region, Map params) {

    def query = "SELECT * from /${region}"

    if (params) {
      query += " WHERE "
    }

    def paramObjects = []

    params.eachWithIndex { entry, idx ->
      query += " ${entry.key}=\$${idx}"
      paramObjects << entry.value
    }

    def q = queryService.newQuery(query)

    if (params) {
      return q.execute(paramObjects)
    } else {
      return q.execute()
    }
  }

  Region getRegion(String name) {
    Region regionMap = cache.getRegion(name)
    if (regionMap == null) {
      regionMap = cache.createRegionFactory(RegionShortcut.PARTITION_REDUNDANT).create(name)
      log.warn("Created new Region[${name}] - Partitioned|Redundant")
    }
    regionMap
  }
}
