package org.tron.common.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.stream.Collectors;
import org.tron.common.parameter.CommonParameter;

public class CacheManager {

  private static final Map<CacheType, CypherCache<?, ?>> CACHES  = Maps.newConcurrentMap();

  public static <K, V> CypherCache<K, V> allocate(CacheType name) {
    CypherCache<K, V> cache = new CypherCache<>(name, CommonParameter.getInstance()
        .getStorage().getCacheStrategy(name));
    CACHES.put(name, cache);
    return cache;
  }

  public  static <K, V> CypherCache<K, V> allocate(CacheType name, String strategy) {
    CypherCache<K, V> cache = new CypherCache<>(name, strategy);
    CACHES.put(name, cache);
    return cache;
  }

  public  static <K, V> CypherCache<K, V> allocate(CacheType name, String strategy,
                                                 CacheLoader<K, V> loader) {
    CypherCache<K, V> cache = new CypherCache<>(name, strategy, loader);
    CACHES.put(name, cache);
    return cache;
  }


  public static void release(CypherCache<?, ?> cache) {
    cache.invalidateAll();
  }

  public static Map<String, CacheStats> stats() {
    return CACHES.values().stream().collect(Collectors.toMap(c -> c.getName().toString(),
        CypherCache::stats));
  }

}
