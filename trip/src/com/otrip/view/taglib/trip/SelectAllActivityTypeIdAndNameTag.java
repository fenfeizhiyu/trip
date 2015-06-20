package com.otrip.view.taglib.trip;   

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.otrip.controls.trip.configure.ActivityTypeController;
import com.otrip.domain.system.SysDictionaryType;
import com.otrip.domain.trip.OtActiType;
import com.otrip.service.system.DictionaryTypeService;
import com.otrip.service.trip.ActiTypeService;
import com.otrip.view.SystemUtils;
import com.otrip.view.taglib.dictionary.SelectAllDictionaryTypeIdAndNameTag;

/**   
 * Filename:    SelectAllActivityTypeIdAndNameTag.java   
 * Copyright:   Copyright (c)2015  
 * Company:     otrip  
 * @version:    1.0   
 * @since:  JDK 1.7.0_21  
 * Create at:   2015年3月28日 下午4:25:05   
 * Description:  
 *   
 * Modification History:   
 * Date    Author      Version     Description   
 * ----------------------------------------------------------------- 
 * 2015年3月28日 zhongJc      1.0     1.0 Version   
 */
public class SelectAllActivityTypeIdAndNameTag extends BodyTagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9062697414302885363L;
	static Logger log=Logger.getLogger(SelectAllActivityTypeIdAndNameTag.class);
	
	private String selid;
	private String selname;
	
	
	
	public String getSelid() {
		return selid;
	}



	public void setSelid(String selid) {
		this.selid = selid;
	}



	public String getSelname() {
		return selname;
	}



	public void setSelname(String selname) {
		this.selname = selname;
	}



	/**
	 * @see 标示不处理标签体，直接调用后面的方法doEndTag
	 */
	@Override
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return SKIP_BODY;
	}
	
	
	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		setActiTypeService();
		List<OtActiType> typeList=null;
		try {
			typeList = actiTypeService.findAll();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StringBuffer outBuf=new StringBuffer("<select name=\""+selname+"\" id=\""+selid+"\" >");
		outBuf.append("<option value='0'>"+SystemUtils.getMessage("taglib.common.select.default.option.pls", null, (HttpServletRequest) pageContext.getRequest())+"</option>");
		if(typeList!=null&&typeList.size()>0){
			for( OtActiType dt : typeList){
				if(dt!=null&&StringUtils.isNotBlank(dt.getFsId())&&StringUtils.isNotBlank(dt.getFsName())){
					outBuf.append("<option value=\""+dt.getFsId()+"\">"+dt.getFsName()+"</option>");
				}
			}
		}
		outBuf.append("</select>");
		log.info(" dictionary type select :"+outBuf.toString());
		JspWriter out =null;   
        
        try {
        	out = pageContext.getOut();   
			out.write(outBuf.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(" has errpr:"+e.getLocalizedMessage());
		}
		return EVAL_PAGE;
	}
	
	
	
	
	
	/*****************************************************************************
	 * 引入数据字典类型服务类
	 * */
	private	ActiTypeService actiTypeService;
	public void setActiTypeService() {
		log.info("注入dictionaryTypeService");
        ServletContext servletContext = pageContext.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        actiTypeService=(ActiTypeService) wac.getBean("actiTypeService");
	}
}
