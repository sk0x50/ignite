package org.apache.ignite.yardstick.cache;

import java.util.Map;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.yardstick.cache.model.EightThIndices;

public class IgnitePut8Benchmark extends IgniteCacheAbstractBenchmark<Integer, EightThIndices> {
    /** {@inheritDoc} */
    @Override public boolean test(Map<Object, Object> ctx) throws Exception {
        int key = nextRandom(args.range());

        IgniteCache<Integer, EightThIndices> cache = cacheForOperation();

        cache.put(key, new EightThIndices(key));

        return true;
    }

    /** {@inheritDoc} */
    @Override protected IgniteCache<Integer, EightThIndices> cache() {
        return ignite().cache("atomic-index-8");
    }
}
