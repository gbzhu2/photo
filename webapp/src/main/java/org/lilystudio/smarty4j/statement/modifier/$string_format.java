package org.lilystudio.smarty4j.statement.modifier;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.expression.StringExpression;
import org.lilystudio.smarty4j.statement.Modifier;
import org.lilystudio.smarty4j.statement.ParameterCharacter;

/**
 * 字符格式转换
 * 
 * <pre>
 * {$now|string_format:'%d'}
 * </pre>
 * 
 * @version 1.0.0, 2010/10/01
 * @author simon4545
 * @since Smarty 1.0
 */
public class $string_format extends Modifier {

	/** 参数定义 */
	private static ParameterCharacter[] definitions = {
			new ParameterCharacter(ParameterCharacter.STRING,
					new StringExpression("%d")),
			new ParameterCharacter(ParameterCharacter.STRING, null) };
	/** 允许使用的地区信息列表 */
	private static Map<String, Locale> locales = new HashMap<String, Locale>();

	static {
		locales.put("CANADA", Locale.CANADA);
		locales.put("CANADA_FRENCH", Locale.CANADA_FRENCH);
		locales.put("CHINA", Locale.CHINA);
		locales.put("CHINESE", Locale.CHINESE);
		locales.put("ENGLISH", Locale.ENGLISH);
		locales.put("FRANCE", Locale.FRANCE);
		locales.put("FRENCH", Locale.FRENCH);
		locales.put("GERMAN", Locale.GERMAN);
		locales.put("GERMANY", Locale.GERMANY);
		locales.put("ITALIAN", Locale.ITALIAN);
		locales.put("ITALY", Locale.ITALY);
		locales.put("JAPAN", Locale.JAPAN);
		locales.put("JAPANESE", Locale.JAPANESE);
		locales.put("KOREA", Locale.KOREA);
		locales.put("KOREAN", Locale.KOREAN);
		locales.put("PRC", Locale.PRC);
		locales.put("SIMPLIFIED_CHINESE", Locale.SIMPLIFIED_CHINESE);
		locales.put("TAIWAN", Locale.TAIWAN);
		locales.put("TRADITIONAL_CHINESE", Locale.TRADITIONAL_CHINESE);
		locales.put("UK", Locale.UK);
		locales.put("US", Locale.US);
	}

	/** 当前变量调节器使用的地域对象 */
	private Locale locale;

	@Override
	public void init(Template parent, boolean ransack, List<IExpression> values)
			throws ParseException {
		super.init(parent, ransack, values);
		if (getParameter(1) != null) {
			locale = locales.get(getParameter(2).toString());
			if (locale == null) {
				throw new ParseException("不支持的地区");
			}
		}
		locale = Locale.getDefault();
	}

	@Override
	public Object execute(Object obj, Context context, Object[] values) {
		String source = obj.toString();
		if (source.length() == 0) {
			return source;
		}
		String format = (String) values[0];
		return String.format(format, obj);
	}

	public ParameterCharacter[] getDefinitions() {
		return definitions;
	}
}