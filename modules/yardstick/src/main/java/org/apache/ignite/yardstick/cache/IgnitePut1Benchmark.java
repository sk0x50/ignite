package org.apache.ignite.yardstick.cache;

import java.util.Map;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.yardstick.cache.model.OneIndex;

public class IgnitePut1Benchmark extends IgniteCacheAbstractBenchmark<Integer, OneIndex> {
    /** {@inheritDoc} */
    @Override public boolean test(Map<Object, Object> ctx) throws Exception {
        int key = nextRandom(args.range());

        IgniteCache<Integer, OneIndex> cache = cacheForOperation();

        cache.put(key, new OneIndex(key));

        return true;
    }

    /** {@inheritDoc} */
    @Override protected IgniteCache<Integer, OneIndex> cache() {
        return ignite().cache("atomic-index-1");
    }
}
