package com.simplicityitself.gemfire

import com.gemstone.gemfire.cache.execute.Function
import com.gemstone.gemfire.cache.execute.FunctionContext
import com.gemstone.gemfire.cache.execute.RegionFunctionContext
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper
import com.gemstone.gemfire.pdx.PdxInstance

class GroovySumFunction implements Function  {

  @Override
  void execute(FunctionContext fc) {
    if (!(fc instanceof RegionFunctionContext)) {
      throw new IllegalArgumentException("groovySum can only be called against a region");
    }

    def field = fc.arguments

    def region = PartitionRegionHelper.getLocalDataForContext(fc)

    def sum = region.values().collect {
      if (it instanceof PdxInstance) {
        it.getField(field)
      } else {
        it."${field}"
      }
    }.sum()

    fc.resultSender.lastResult(sum)
  }

  @Override
  boolean hasResult() {
    return true
  }

  @Override
  String getId() {
    return "si-sum"
  }

  @Override
  boolean optimizeForWrite() {
    return false
  }

  @Override
  boolean isHA() {
    return false
  }
}


