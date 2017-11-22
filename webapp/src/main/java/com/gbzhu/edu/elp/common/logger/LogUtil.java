package com.gbzhu.edu.elp.common.logger;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.gbzhu.edu.elp.common.util.JSONUtils;

/**
 * 日志辅助工具类。
 * @author znyin
 *
 */
public class LogUtil {
	
	/**
	 */
	private static final Logger userActionLogger = LoggerFactory.getLogger("userAction");
	
	/**
	 */
	private static final Logger perfLogger = LoggerFactory.getLogger("perf");
	
	/**
	 * 记录一用户行为日志。
	 * <p><pre>
	 * 所有信息会组成一个map，然后将其序列化成json，再写入日志中。
	 * map中有几个key的名称是固定的：userId(用户Id)、action(行为类型)、platform(行为所属平台)、refer(行为所关联的URL)。
	 * </pre>
	 * @param userId 用户Id
	 * @param action 行为类型
	 * @param platform 行为所属平台
	 * @param otherInfos 其他信息
	 */
	public static void userAction(String userId,String action,Platform platform,Map otherInfos){
		Map map=new HashMap();
		map.put("userId", userId);
		map.put("action", action);
		map.put("platform", platform.name());
		map.putAll(otherInfos);
		String json=JSONUtils.toJSONString(map);
		userActionLogger.info(json);
	}
	
	/**
	 * 记录一用户行为日志。
	 * <p><pre>
	 * 所有信息会组成一个map，然后将其序列化成json，再写入日志中。
	 * map中有几个key的名称是固定的：userId(用户Id)、action(行为类型)、platform(行为所属平台)、refer(行为所关联的URL)。
	 * </pre>
	 * @param userId 用户Id
	 * @param action 行为类型
	 * @param platform 行为所属平台
	 * @param refer 行为所关联的URL
	 * @param otherInfos 其他信息
	 */
	public static void userAction(String userId,String action,Platform platform,String refer,Map otherInfos){
		Map map=new HashMap();
		map.put("userId", userId);
		map.put("action", action);
		map.put("platform", platform.name());
		map.put("refer", refer);
		map.putAll(otherInfos);
		String json=JSONUtils.toJSONString(map);
		userActionLogger.info(json);
	}
	
	/**
	 * 记录一性能日志。
	 * @param opdesc 操作描述
	 * @param duration 耗时
	 */
	public static void perf(String opdesc,long duration){
		Assert.hasLength(opdesc,"opdesc must has length.");
		String msg=String.format("%s %d", opdesc,duration);
		perfLogger.info(msg);
	}

}
