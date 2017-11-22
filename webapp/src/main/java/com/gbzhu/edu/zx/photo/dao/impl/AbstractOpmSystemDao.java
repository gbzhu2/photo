package com.gbzhu.edu.zx.photo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.gbzhu.edu.elp.common.util.KeyHolder;

/**
 * 管理系统业务基础dao
 * 
 * @author iflytek-clfu
 * 
 */
public abstract class AbstractOpmSystemDao {

	/**
	 * 每次批量添加数据
	 */
	private static final int DEFAULT_COUNT = 500;

	/**
	 * jdbcTemplate
	 */
	@Autowired()
	@Qualifier("jdbcTemplateOpmsystem")
	protected JdbcTemplate jdbcTemplate;

	/**
	 * namedParameterJdbcTemplate
	 */
	@Autowired()
	@Qualifier("namedParameterJdbcTemplateOpmsystem")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 获取日志记录
	 * 
	 * @return logger
	 */
	protected abstract Logger getLogger();

	/**
	 * 获取表名
	 * 
	 * @return 表名
	 */
	protected abstract String getTableName();

	/**
	 * 获取表关键字
	 * 
	 * @return 表关键字（比如主键）
	 */
	protected abstract String getTableKey();

	/**
	 * 通过id删除记录
	 * 
	 * @param id
	 *            记录的主键
	 */
	protected void deleteById(String id) {
		Assert.notNull(id, "id is required");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("DELETE FROM ").append(getTableName())
				.append(" WHERE ").append(getTableKey()).append("=?");

		getLogger().debug(" sql -->" + sqlBuilder.toString());
		this.jdbcTemplate.update(sqlBuilder.toString(), id);
	}

	/**
	 * 通过idList删除记录
	 * 
	 * @param idList
	 *            记录的主键列表
	 */
	protected void deleteByIdList(List<String> idList) {
		Assert.notNull(idList, "idList is required");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("DELETE FROM ").append(getTableName())
				.append(" WHERE ").append(getTableKey())
				.append(getWhereInPlaceHolder(idList.size()));

		getLogger().debug(" sql -->" + sqlBuilder.toString());
		this.jdbcTemplate.update(sqlBuilder.toString(), idList.toArray());
	}

	/**
	 * <p>
	 * <code>findById</code>通过Id查找单条记录
	 * </p>
	 * 
	 * @param rowMapper
	 *            rowMapper
	 * @param id
	 *            id
	 * @param <T>
	 *            泛型
	 * @return T
	 * @author clfu
	 */
	protected <T> T findById(RowMapper<T> rowMapper, String id) {
		Assert.notNull(id);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(this.getTableKey(), id);
		List<T> resultList = this.find(rowMapper, parameters);

		if (CollectionUtils.isEmpty(resultList)) {
			return null;
		}

		return resultList.get(0);
	}

	/**
	 * <p>
	 * <code>find</code>根据参数查找T集合
	 * </p>
	 * 
	 * @param rowMapper
	 *            rowMapper
	 * @param parameters
	 *            参数
	 * @param <T>
	 *            泛型
	 * @return T
	 * @author clfu
	 */
	protected <T> List<T> find(RowMapper<T> rowMapper,
			Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ")
				.append(getTableName());

		if (CollectionUtils.isEmpty(parameters)) {
			return this.jdbcTemplate.query(sql.toString(), rowMapper);
		}

		sql.append(" WHERE ");
		for (String key : parameters.keySet()) {
			sql.append(key).append(" =:").append(key).append(" AND ");
		}
		sql.delete(sql.lastIndexOf(" AND "), sql.length());

		getLogger().info(" sql -->" + sql.toString());
		SqlParameterSource paramSource = new MapSqlParameterSource(parameters);
		return this.namedParameterJdbcTemplate.query(sql.toString(),
				paramSource, rowMapper);
	}

	/**
	 * <p>
	 * <code>update</code>更新数据
	 * </p>
	 * 
	 * @param parameters
	 *            参数
	 * @param conditionParameters
	 *            条件
	 * @return 更新数量
	 * @author clfu
	 */
	protected int update(Map<String, Object> parameters,
			Map<String, Object> conditionParameters) {
		Assert.notEmpty(parameters, "parameters is required");
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(getTableName()).append(" SET ");
		for (String paramKey : parameters.keySet()) {
			sql.append(paramKey).append("=:").append(paramKey).append(" ,");
		}

		sql.delete(sql.length() - 1, sql.length());
		if (!(CollectionUtils.isEmpty(conditionParameters))) {
			sql.append(" WHERE ");
			for (String condKey : conditionParameters.keySet()) {
				sql.append(condKey).append("=:").append(condKey)
						.append(" AND ");
			}

			sql.delete(sql.lastIndexOf(" AND "), sql.length());
		}
		if (sql.indexOf("WHERE") == -1) {
			sql.delete(sql.length() - 1, sql.length());
		}
		getLogger().info(" sql -->" + sql.toString());
		conditionParameters.putAll(parameters);
		return this.namedParameterJdbcTemplate.update(sql.toString(),
				conditionParameters);
	}

	/**
	 * <p>
	 * <code>generateAddSql</code>生成添加sql字符串
	 * </p>
	 * 
	 * @param paramMap
	 *            参数
	 * @param updateOnDuplicatedKey
	 *            是否更新冲突的记录
	 * @param onDuplicateKeyUpdateCondition
	 *            冲突的记录更新条件
	 * @return sql字符串
	 * @author clfu
	 */
	private String generateAddSql(Map<String, Object> paramMap,
			boolean updateOnDuplicatedKey, String onDuplicateKeyUpdateCondition) {
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(getTableName()).append("(");
		for (String paramKey : paramMap.keySet()) {
			sql.append(paramKey).append(",");
		}

		if (getTableKey() != null
				&& !(paramMap.keySet().contains(getTableKey())))
			sql.append(getTableKey());
		else {
			sql.delete(sql.length() - 1, sql.length());
		}
		sql.append(") VALUES (");
		for (String paramKey : paramMap.keySet()) {
			sql.append(":").append(paramKey).append(",");
		}

		if (getTableKey() != null
				&& !(paramMap.keySet().contains(getTableKey())))
			sql.append(":").append(getTableKey());
		else
			sql.delete(sql.length() - 1, sql.length());
		sql.append(") ");

		if (updateOnDuplicatedKey) {
			sql.append("ON DUPLICATE KEY UPDATE ");
			if (onDuplicateKeyUpdateCondition == null) {
				sql.append(getTableKey()).append("='")
						.append(paramMap.get(getTableKey())).append("'");
			} else {
				sql.append(onDuplicateKeyUpdateCondition);
			}
		}

		return sql.toString();
	}

	/**
	 * <p>
	 * <code>add</code>添加一条数据
	 * </p>
	 * 
	 * @param paramMap
	 *            参数
	 * @return sql字符串
	 * @author clfu
	 */
	@SuppressWarnings("rawtypes")
	protected String add(Map<String, Object> paramMap) {
		Assert.notEmpty(paramMap, "parameters is required");

		if (paramMap.get(getTableKey()) == null) {
			paramMap.put(getTableKey(), KeyHolder.getKey());
		}

		for (Entry<String, Object> entry : paramMap.entrySet()) {
			if (entry.getValue() instanceof Enum) {
				Enum enumKey = (Enum) entry.getValue();
				paramMap.put(entry.getKey(), enumKey.name());
			}
		}

		getLogger().info("sql -->" + generateAddSql(paramMap, false, null));
		this.namedParameterJdbcTemplate.update(
				generateAddSql(paramMap, false, null), paramMap);
		return paramMap.get(getTableKey()).toString();
	}

	/**
	 * 
	 * @param paramMap
	 *            参数
	 * @param onDuplicateKeyUpdateCondition
	 *            条件
	 * @return key
	 */
	@SuppressWarnings("rawtypes")
	protected String addOrUpdate(Map<String, Object> paramMap,
			String onDuplicateKeyUpdateCondition) {
		Assert.notEmpty(paramMap, "parameters is required");
		Assert.notNull(paramMap.get(getTableKey()),
				"primary key must be specified");

		for (Entry<String, Object> entry : paramMap.entrySet()) {
			if (entry.getValue() instanceof Enum) {
				Enum enumKey = (Enum) entry.getValue();
				paramMap.put(entry.getKey(), enumKey.name());
			}
		}

		String sql = generateAddSql(paramMap, true,
				onDuplicateKeyUpdateCondition);
		namedParameterJdbcTemplate.update(sql, paramMap);
		return paramMap.get(getTableKey()).toString();
	}

	/**
	 * <p>
	 * <code>addBatch</code>批量添加数据
	 * </p>
	 * 
	 * @param parametersMaps
	 *            参数
	 * @return 添加数量
	 * @author clfu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int addBatch(Map<String, Object>[] parametersMaps) {
		Assert.notEmpty(parametersMaps, "parametersMaps is required");
		List list = CollectionUtils.arrayToList(parametersMaps);
		return addBatch(list);
	}

	/**
	 * <p>
	 * <code>addBatch</code>批量添加数据
	 * </p>
	 * 
	 * @param parameterCollection
	 *            参数
	 * @return 添加数量
	 * @author clfu
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int addBatch(List<Map<String, Object>> parameterCollection) {
		Assert.notEmpty(parameterCollection, "parameterCollection is required");
		Map<String, Object> parameterMap = (Map<String, Object>) parameterCollection
				.get(0);
		String sqlString = generateAddSql(parameterMap, false, null);
		int size = parameterCollection.size();
		int count = size / DEFAULT_COUNT;

		for (Map<String, Object> param : parameterCollection) {
			for (Entry<String, Object> entry : param.entrySet()) {
				if (entry.getValue() instanceof Enum) {
					Enum enumKey = (Enum) entry.getValue();
					param.put(entry.getKey(), enumKey.name());
				}
			}
		}

		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				Map[] batchValues = new HashMap[DEFAULT_COUNT];
				for (int j = 0; j < DEFAULT_COUNT; ++j) {
					batchValues[j] = ((Map) parameterCollection.get(i
							* DEFAULT_COUNT + j));
				}
				this.namedParameterJdbcTemplate.batchUpdate(sqlString,
						batchValues);
			}
			if (size % DEFAULT_COUNT != 0) {
				Map[] batchValues = new HashMap[size - (count * DEFAULT_COUNT)];
				int j = 0;
				for (int i = count * DEFAULT_COUNT; i < size; ++i) {
					batchValues[(j++)] = ((Map) parameterCollection.get(i));
				}
				this.namedParameterJdbcTemplate.batchUpdate(sqlString,
						batchValues);
			}
		} else {
			this.namedParameterJdbcTemplate.batchUpdate(sqlString,
					(Map[]) parameterCollection
							.toArray(new HashMap[parameterCollection.size()]));
		}
		return size;
	}

	/**
	 * <p>
	 * <code>getWhereInPlaceHolder</code> 获取where条件中in的占位符"?"条件，该方法包含IN关键字
	 * </p>
	 * eg: getWhereInPlaceHolder(4) = > IN(?,?,?,?); 如果 size = 1 返回" = ? "
	 * 
	 * @author clfu
	 * @param size
	 *            占位符个数
	 * @return String
	 */
	protected String getWhereInPlaceHolder(int size) {
		Assert.isTrue(size > 0, "IN condition params must greater than 0");
		if (size == 1) {
			return " = ? ";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" IN(");
			for (int i = 0; i < size; i++) {
				sb.append("?,");
			}
			sb.delete(sb.lastIndexOf(","), sb.length());
			sb.append(") ");
			return sb.toString();
		}
	}

}
