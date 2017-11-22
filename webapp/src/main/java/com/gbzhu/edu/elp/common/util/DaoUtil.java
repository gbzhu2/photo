package com.gbzhu.edu.elp.common.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dao工具类。
 * @author znyin
 *
 */
public class DaoUtil {
	
	/**
	 * 判断ResultSet中是否包含指定的列。
	 * @param rs ResultSet
	 * @param columnLabel 列名
	 * @return true:包含；false:不包含。
	 */
	public static boolean isColumnExist(ResultSet rs,String columnLabel){
		try{
			rs.findColumn(columnLabel);
			return true;
		}catch (SQLException e) {
			return false;
		}
	}

}
