package com.gbzhu.edu.elp.common.dao.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.gbzhu.edu.elp.common.util.KeyHolder;

/**
 * <strong>
 * dao basic operations
 * </strong>
 * <p>
 * this class implements operation for CRUD,if you don't want to use it,please extends another class ---AbstractBaseDao
 * <p>
 * usage:
 * <p>please reference test case
 * @author 金丙传
 * 
 */
public abstract class AbstractBaseOperationsDao extends AbstractBaseDao implements InitializingBean {
	/**
	 * 动态表
	 */
	private String table;
	/**
	 * 获得数据库表的名称
	 * @return 表名
	 */
	protected abstract String getTableName();
	
	/**
	 * 获得表的主键
	 * @return 数据库表主键名称
	 */
	protected abstract String getTableKey();
	
	/**
	 * 日志记录
	 * @return 日志
	 */
	protected abstract Logger getLog();
	
	/**
	 * 每次批量添加数据
	 */
	private final int defaultCount = 50;
	
	/**
	 * 获取动态表
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * 动态改变表名称
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * delete record via id
	 * @param id 主键
	 */
	protected void deleteById(String id){
		Assert.notNull(id,"id is required");
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(this.getTable() == null?this.getTableName():this.getTable()).append(" where  ").append(this.getTableKey());
		sql.append("=?");
		getLog().info(" sql -->"+sql.toString());
		this.setTable(null);
		jdbcTemplate.update(sql.toString(), id);
	}
	/**
	 * query entity by id
	 * @param cls 需要查找的试题
	 * @param id 主键
	 * @param <T> 还回实体
	 * @return 实体
	 */
	@SuppressWarnings("unchecked")
	protected <T> T findById(Class<T> cls,String id){
		Assert.notNull(id,"id is required");
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(this.getTable() == null?this.getTableName():this.getTable()).append(" where  ").append(this.getTableKey());
		sql.append("=?");
		getLog().info(" sql -->"+sql.toString());
		this.setTable(null);
		return (T)jdbcTemplate.queryForObject(sql.toString(), new ClazzRowMapper<T>(cls),id);
	}
	
	/**
	 * query entity list
	 * @param cls 还回实体
	 * @param parameters 参数映射
	 * @param <T> 还回实体
	 * @return 列表
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> find(Class<T> cls,Map<String,Object> parameters){
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(this.getTable() == null?this.getTableName():this.getTable());
		if(CollectionUtils.isEmpty(parameters)){
			return (List<T>)jdbcTemplate.query(sql.toString(), new ClazzRowMapper<T>(cls));
		}else{
			sql.append(" where ");
			for(String key:parameters.keySet()){
				sql.append(key).append(" =:").append(key).append("  and ");
			}
			sql.delete(sql.lastIndexOf(" and "), sql.length());
		}
		this.setTable(null);
		getLog().info("  sql -->"+sql.toString());
		SqlParameterSource paramSource = new MapSqlParameterSource(parameters);  
		return (List<T>)namedParameterJdbcTemplate.query(sql.toString(), paramSource, new ClazzRowMapper<T>(cls));
	}
	
	/**
	 * update entity record
	 * @param parameters 需要修改参数键值
	 * @param conditionParameters 条件查询键值
	 * @return 修改记录数
	 */
	protected int update(Map<String,Object> parameters,Map<String,Object> conditionParameters){
		Assert.notEmpty(parameters,"parameters is required");
		StringBuilder sql = new StringBuilder("update ");
		sql.append(this.getTable() == null?this.getTableName():this.getTable()).append(" set ");
		for(String key:parameters.keySet()){
			sql.append(key).append("=:").append(key).append(" ,");
		}
		sql.delete(sql.length()-1, sql.length());
		if(!CollectionUtils.isEmpty(conditionParameters)){
			sql.append(" where ");
			for(String key:conditionParameters.keySet()){
				sql.append(key).append("=:").append(key).append("  and ");
			}
			sql.delete(sql.lastIndexOf(" and "), sql.length());
		}
		if(sql.indexOf("where") == -1){
			sql.delete(sql.length()-1, sql.length());
		}
		getLog().info("sql -->"+sql.toString());
		this.setTable(null);
		conditionParameters.putAll(parameters);
		return namedParameterJdbcTemplate.update(sql.toString(),conditionParameters);
	}
	
	/**
	 * 获取生成的sql语句
	 * @param paramMap 
	 * @return sql
	 */
	private String generateAddSql(Map<String,Object> paramMap) {
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(this.getTable() == null?this.getTableName():this.getTable()).append("(");
		for(String key:paramMap.keySet()){
			sql.append(key).append(",");
		}
		if(!paramMap.keySet().contains(getTableKey())){
			sql.append(getTableKey());
		}else{
			sql.delete(sql.length()-1, sql.length());
		}
		sql.append(") values (");
		for(String key:paramMap.keySet()){
			sql.append(":").append(key).append(",");
		}
		if(!paramMap.keySet().contains(getTableKey())){
			sql.append(":").append(getTableKey());
		}else
		  sql.delete(sql.length()-1, sql.length());
		sql.append(")");		
		return sql.toString();
	}
	
	/**
	 * create new record.
	 * primary key will auto generate,if you not manually set value to it
	 * @param paramMap 字段键值
	 * @return 修改记录数
	 */
	protected String add(Map<String,Object> paramMap){
		Assert.notEmpty(paramMap,"parameters is required");
		for(String key:paramMap.keySet()){		
			if(paramMap.get(key) instanceof Enum){
				Enum enum1 = (Enum)paramMap.get(key);
				paramMap.put(key, enum1.name());
			}
		}
		if(!paramMap.keySet().contains(getTableKey())){
			paramMap.put(getTableKey(), KeyHolder.getKey());
		}
		this.setTable(null);
		getLog().info("sql -->"+generateAddSql(paramMap).toString());
		namedParameterJdbcTemplate.update(generateAddSql(paramMap).toString(), paramMap);
		return paramMap.get(getTableKey()).toString();
	}
	
	/**
	 * 批量添加数据,集合中包含了map,key为属性，value为属性值
	 * @param parametersMaps map数组
	 * @return 更新记录数
	 */
	@SuppressWarnings("unchecked")
	public int addBatch(Map<String, Object>[] parametersMaps) {
		Assert.notEmpty(parametersMaps, "parametersMaps is required");
		List<Map<String, Object>> list = CollectionUtils.arrayToList(parametersMaps);
		return addBatch(list);
	}
	
	/**
	 * 批量添加数据,集合中包含了map,key为属性，value为属性值
	 * @param parameterCollection 集合
	 * @return 更新数量
	 */
	@SuppressWarnings("unchecked")
	protected int addBatch(List<Map<String,Object>> parameterCollection) {
		Assert.notEmpty(parameterCollection, "parameterCollection is required");
		Map<String, Object> parameterMap = parameterCollection.get(0);
		String sqlString = generateAddSql(parameterMap).toString();
		int size = parameterCollection.size();
		int count = size/defaultCount;
		for(Map<String, Object> map:parameterCollection){
		  for(String key:map.keySet()){		
				if(map.get(key) instanceof Enum){
					Enum enum1 = (Enum)map.get(key);
					map.put(key, enum1.name());
				}
		  }
		}
		if(count>0){
			for(int i =0;i<count;i++){
				Map<String,Object>[] batchValues = new HashMap[defaultCount];
				for(int j=0;j<defaultCount;j++){
					batchValues[j]=parameterCollection.get(i*defaultCount+j);
				}
				super.namedParameterJdbcTemplate.batchUpdate(sqlString, batchValues);
			}
			if(size%defaultCount != 0){
				Map<String,Object>[] batchValues = new HashMap[size-count*defaultCount];
				int j = 0;
				for(int i =count*defaultCount;i<size;i++){
					batchValues[j++]=parameterCollection.get(i);
				}
				super.namedParameterJdbcTemplate.batchUpdate(sqlString, batchValues);
			}			
		}else {			
			super.namedParameterJdbcTemplate.batchUpdate(sqlString, parameterCollection.toArray(new HashMap[parameterCollection.size()]));
		}
		return size;
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(jdbcTemplate == null || namedParameterJdbcTemplate == null){
			getLog().error("property 'jdbcTemplate' and 'namedParameterJdbcTemplate' is required", new NullPointerException());
			throw new NullPointerException("property 'jdbcTemplate' and 'namedParameterJdbcTemplate' is required");	
		}			
	}
	
}
