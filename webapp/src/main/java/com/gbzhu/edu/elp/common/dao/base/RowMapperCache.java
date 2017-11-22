package com.gbzhu.edu.elp.common.dao.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 属性映射缓存
 * @author 金丙传
 *
 */
public class RowMapperCache {
	/**
	 * 对象和属性之间映射缓存，key 对象名称，value 属性数组
	 */
	private static Map<String, PropertyMapper[]> entityCache = new ConcurrentHashMap<String, PropertyMapper[]>();

	/**
	 * @return the entityCache
	 */
	public static Map<String, PropertyMapper[]> getEntityCache() {
		return entityCache;
	}
	/**
	 * 根据实体名称获取映射的属性
	 * @param key 实体对象名称
	 * @return 映射的数组
	 */
	public static PropertyMapper[] getEntityCache(String key){
		return entityCache.get(key);
	}

	/**
	 * 缓存某个对象中的属性映射
	 * @param key 实体对象名称
	 * @param propertyMapper 映射的数组
	 */
	public static void setEntityCache(String key, PropertyMapper[] propertyMapper) {
		entityCache.put(key, propertyMapper);
	}

}
