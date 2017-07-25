package com.adcampign.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.adcampign.model.AdCampignRequest;

// Create Simple Cache object with the help of HashMap...
public class AdCache {

	final static Logger logger = Logger.getLogger(AdCache.class);

	private AdCache() {
	}

	private static AdCache adCache = null;

	private static Map<String, AdCampignRequest> adCacheMap = null;

	public static AdCache getInstance() {

		if (adCache == null) {
			synchronized (AdCache.class) {
				if (adCache == null) {
					adCache = new AdCache();
					adCacheMap = new HashMap<String, AdCampignRequest>();
				}
			}

		}
		return adCache;
	}

	// PUT method
	public void put(String key, AdCampignRequest value) {
		synchronized (adCacheMap) {
			adCacheMap.put(key, value);
		}
	}

	// Get Cache Objects Size()
	public int size() {
		synchronized (adCacheMap) {
			return adCacheMap.size();
		}
	}

	public Map<String, AdCampignRequest> getAdCacheMap() {
		synchronized (adCacheMap) {
			return adCacheMap;
		}
	}

	// GET method
	public AdCampignRequest get(String key) {
		synchronized (adCacheMap) {
			AdCampignRequest c = (AdCampignRequest) adCacheMap.get(key);

			return c;
		}
	}

	// REMOVE method
	public void remove(String key) {
		synchronized (adCacheMap) {
			adCacheMap.remove(key);
		}
	}
}
