package com.gbzhu.edu.elp.common.dao.base;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
/**
 * 类映射
 * @author 金丙传
 *
 * @param <T>
 */
public class ClazzRowMapper<T> implements RowMapper {
	
	/**
	 * 
	 */
	private Class<T> t;

	/**
	 * constructor
	 * 
	 * @param clazz
	 *            cls
	 */
	public ClazzRowMapper(Class<T> clazz) {
		this.t = clazz;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Object obj = BeanUtils.instantiate(t);
		PropertyMapper[] pm ;
		if(RowMapperCache.getEntityCache(this.t.getSimpleName()) != null){
			pm = RowMapperCache.getEntityCache(this.t.getSimpleName());
		}else{
			pm = getPropertyMapper();
		}
		ResultSetMetaData rsmd = rs.getMetaData();
		int len = rsmd.getColumnCount();
		for(int i =1;i<=len;i++){
			String columnName = rsmd.getColumnName(i);
			Method writeMethod = getMethod(columnName,pm);
			try {
				if(writeMethod != null && rs.getObject(columnName) != null)
				  writeMethod.invoke(obj, rs.getObject(columnName));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {		
				e.printStackTrace();
			} catch (InvocationTargetException e) {				
				e.printStackTrace();
			}
		}
		return (T) obj;
	}
	/**
	 * 获得某个字段的set方法
	 * @param columnName 表字段
	 * @param pm 
	 * @return set方法对象
	 */
	private Method getMethod(String columnName,PropertyMapper[] pm){
		for(PropertyMapper p:pm){
			if(p.getPropertyName().equalsIgnoreCase(columnName))
				return p.getWriteMethod(); 
		}
		return null;
	}
	
	/**
	 * 获得某个属性的映射对象
	 * @return PropertyMapper
	 */
	private PropertyMapper[] getPropertyMapper(){
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(t);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			Assert.notEmpty(pds," have no property");
			List<PropertyMapper> pmList = new ArrayList<PropertyMapper>();
			for(PropertyDescriptor pd:pds){
				if(!pd.getName().equals("class")){
					PropertyMapper pMapper = new PropertyMapper();
					pMapper.setPropertyName(pd.getName());
					pMapper.setWriteMethod(pd.getWriteMethod());
					pmList.add(pMapper);
				}
			}
			PropertyMapper[] arr = new PropertyMapper[pmList.size()];
			arr = pmList.toArray(arr);
			RowMapperCache.setEntityCache(this.t.getSimpleName(),arr);
			return arr;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
