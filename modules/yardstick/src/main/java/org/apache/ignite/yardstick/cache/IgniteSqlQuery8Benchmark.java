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

package org.apache.ignite.yardstick.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.cache.Cache;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.yardstick.cache.model.EightThIndices;
import org.yardstickframework.BenchmarkConfiguration;

import static org.yardstickframework.BenchmarkUtils.println;

/**
 * Ignite benchmark that performs query operations.
 */
public class IgniteSqlQuery8Benchmark extends IgniteCacheAbstractBenchmark<Integer, EightThIndices> {
    /** */
    private static final String FIELD_NAME_PREFIX = "intField_";

    /** */
    private static final int NUMBER_OF_INDICES = 8_000;

    /** {@inheritDoc} */
    @Override public void setUp(BenchmarkConfiguration cfg) throws Exception {
        super.setUp(cfg);

        loadCachesData();
    }

    /** {@inheritDoc} */
    @Override protected void loadCacheData(String cacheName) {
        try (IgniteDataStreamer<Integer, EightThIndices> dataLdr = ignite().dataStreamer(cacheName)) {
            for (int i = 0; i < args.range(); i++) {
                if (i % 100 == 0 && Thread.currentThread().isInterrupted())
                    break;

                dataLdr.addData(i, new EightThIndices(i));

                if (i % 100000 == 0)
                    println(cfg, "Populated entries: " + i);
            }
        }
    }

    /** {@inheritDoc} */
    @Override public boolean test(Map<Object, Object> ctx) throws Exception {
        int minValue = nextRandom(args.range());
        int maxValue = minValue + 1000;

        int indexId = nextRandom(NUMBER_OF_INDICES);
        String field = FIELD_NAME_PREFIX + Integer.toString(indexId);

        Collection<Cache.Entry<Integer, EightThIndices>> entries = executeQuery(field, minValue, maxValue);

//        for (Cache.Entry<Integer, OneIndex> entry : entries) {
//            Person p = (Person)entry.getValue();
//
//            if (p.getSalary() < salary || p.getSalary() > maxSalary)
//                throw new Exception("Invalid person retrieved [min=" + salary + ", max=" + maxSalary +
//                        ", person=" + p + ']');
//        }

        return true;
    }

    /**
     * @param minValue Min value.
     * @param maxValue Max value.
     * @return Query result.
     * @throws Exception If failed.
     */
    private Collection<Cache.Entry<Integer, EightThIndices>> executeQuery(String fieldName, double minValue, double maxValue) throws Exception {
        IgniteCache<Integer, EightThIndices> cache = cacheForOperation(true);

        SqlQuery qry = new SqlQuery(EightThIndices.class, fieldName + " >= ? and " + fieldName + " <= ?");

        qry.setArgs(minValue, maxValue);

        return cache.query(qry).getAll();
    }

    /** {@inheritDoc} */
    @Override protected IgniteCache<Integer, EightThIndices> cache() {
        return ignite().cache("atomic-index-8");
    }
}
