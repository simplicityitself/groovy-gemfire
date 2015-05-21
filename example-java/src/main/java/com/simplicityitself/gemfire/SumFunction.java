package com.simplicityitself.gemfire;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

public class SumFunction extends FunctionAdapter {

    @Override
    public void execute(FunctionContext functionContext) {

        if (!(functionContext instanceof RegionFunctionContext)) {
            throw new IllegalArgumentException("SumFunction can only be executed against data");
        }

        RegionFunctionContext rfc =  (RegionFunctionContext) functionContext;

        Region<Object, CustomerOrder> localRegion = PartitionRegionHelper.getLocalDataForContext(rfc);

        double total = 0;

        for (CustomerOrder item : localRegion.values()) {
            total += item.getOrderTotal();
        }

        rfc.getResultSender().lastResult(total);
    }

    @Override
    public String getId() {
        return "si-sum-order-total";
    }
}
