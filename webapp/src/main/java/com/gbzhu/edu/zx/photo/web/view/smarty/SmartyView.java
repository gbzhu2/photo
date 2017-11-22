package com.gbzhu.edu.zx.photo.web.view.smarty;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;

import com.gbzhu.edu.elp.common.util.HttpClientUtils;
import com.gbzhu.edu.elp.common.util.JSONUtils;
import com.gbzhu.edu.zx.photo.web.dto.OpmUserDto;
import com.gbzhu.edu.zx.photo.web.util.WebUtil;

/**
 * 基于smarty模板的视图。
 * 
 * @author znyin
 * 
 */
public class SmartyView extends AbstractTemplateView {

	/**
	 * rootpath
	 */
	private static String webRootPath;

	/**
	 * smarty的模板引擎
	 */
	private static Engine smartyEngine;

	/**
	 * web请求地址的根。
	 */
	private static String webRootUrl;

	/**
	 * 是否已经初始化好了。
	 */
	private static boolean isInited = false;

	/**
	 * 初始化并发锁
	 */
	private static Object lock = new Object();

	
	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!isInited) {
			synchronized (lock) {
				if (!isInited) {
					init(request);
				}
			}
		}

		Template template = smartyEngine.getTemplate(this.getUrl());
		Context context = new Context();
		for (Entry<String, Object> entry : model.entrySet()) {
			context.set(entry.getKey(), entry.getValue());
		}
		context.set("SmartyBasePath", HttpClientUtils
				.getFullUrl(
						(String) getApplicationContext().getBean("webRootUrl"),
						request));
	
		try {
			OpmUserDto currentUser = WebUtil.getCurrentUser(request);
			context.set("currentUser", JSONUtils.toJSONString(currentUser));
			context.set("currentUserObj", currentUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		response.setCharacterEncoding(smartyEngine.getEncoding());
		template.merge(context, response.getOutputStream());
	}

	/**
	 * 初始化。
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	synchronized private void init(HttpServletRequest request) {
		extractConfigs(request);
		autodetectEngine();

		isInited = true;
	}

	/**
	 * 从spring的ApplicationContext中得到smarty的模板引擎。
	 */
	private void autodetectEngine() {
		try {
			if (smartyEngine == null) {
				smartyEngine = BeanFactoryUtils.beanOfTypeIncludingAncestors(
						getApplicationContext(), Engine.class, true, false);
				smartyEngine.setTemplatePath(webRootPath
						+ smartyEngine.getTemplatePath());
			}
		} catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single org.lilystudio.smarty4j.Engine bean in this web application context "
							+ "(may be inherited): org.lilystudio.smarty4j.Engine is the usual implementation. "
							+ "This bean may be given any name.", ex);
		}
	}

	/**
	 * 提取一些配置信息，放到smarty的context中。
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	private void extractConfigs(HttpServletRequest request) {
		if (webRootPath == null) {
			webRootPath = request.getSession().getServletContext()
					.getRealPath("/");
		}

		if (webRootUrl == null) {
			webRootUrl = (String) getApplicationContext().getBean("webRootUrl");
		}
	}

}
