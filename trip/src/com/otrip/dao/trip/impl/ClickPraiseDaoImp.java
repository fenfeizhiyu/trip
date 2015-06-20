package com.otrip.dao.trip.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.otrip.dao.GenericHibernateDao;
import com.otrip.dao.trip.ClickPraiseDao;
import com.otrip.domain.trip.OtActiClickPraise;
import com.otrip.pojo.PraiseActiVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("clickPraiseDao")
public class ClickPraiseDaoImp extends GenericHibernateDao<OtActiClickPraise,String> implements ClickPraiseDao
{
    public List<PraiseActiVo> getTopPraiseActi(int topNum)
    {
    	if(topNum>0)
    	{
	    	String sql="select a.fs_id,a.fs_no,a.fs_title,a.fn_day,b.praNum from "
	    			+ " (select count(fs_id) as praNum ,fs_acti_id from ot_acti_click_praise group by fs_acti_id limit 0,"+topNum+") b"
	    			+ " inner join (select fs_id,fs_no,fs_title,fn_day from ot_acti_info ) a on a.fs_id=b.fs_acti_id order by praNum desc";
	    	List list=super.findBySQL(sql);
	    	List<PraiseActiVo> returnList=new ArrayList();
	    	if(list.size()>0)
	    	{
	    		for(int i=0;i<list.size();i++)
	    		{
	    			Map map=(Map)list.get(i);
	    			returnList.add(this.getPraiseActiVo(map));
	    		}
	    		return returnList;
	    	}
    	}
    	return null;
    }
    
    public PraiseActiVo getPraiseActiByNo(String actiNo)
    {
    	if(StringUtils.isNotBlank(actiNo))
    	{
	    	String sql=" select a.fs_id,a.fs_no,a.fs_title,a.fn_day,b.praNum from "
	    			+ " (select fs_id,fs_no,fs_title,fn_day from ot_acti_info where"
	    			+ "  fs_no='"+actiNo+"') a inner join "
	    			+ " (select count(fs_id) as praNum,fs_acti_id from ot_acti_click_praise "
	    			+ " group by fs_acti_id) b"
	    			+ "  on a.fs_id=b.fs_acti_id";
	    	List list=super.findBySQL(sql);
    	if(list.size()==1)
    	{
    		Map map=(Map)list.get(0);
    		return this.getPraiseActiVo(map);
    	}
    	}
    	return null;
    }
    
    public PraiseActiVo getPraiseActiByTitle(String actiTile)
    {
    	if(StringUtils.isNotBlank(actiTile))
    	{
	    	String sql=" select a.fs_id,a.fs_no,a.fs_title,a.fn_day,b.praNum from "
	    			+ " (select fs_id,fs_no,fs_title,fn_day from ot_acti_info where"
	    			+ "  fs_title='"+actiTile+"') a inner join "
	    			+ " (select count(fs_id) as praNum,fs_acti_id from ot_acti_click_praise "
	    			+ " group by fs_acti_id) b"
	    			+ "  on a.fs_id=b.fs_acti_id";
	    	List list=super.findBySQL(sql);
    	if(list.size()==1)
    	{
    		Map map=(Map)list.get(0);
    		return this.getPraiseActiVo(map);
    	}
    	}
    	return null;
    }
    
    private PraiseActiVo getPraiseActiVo(Map map)
    {
    	PraiseActiVo pav=new PraiseActiVo();
    	pav.setActiDays(map.get("fn_day")+"");
    	pav.setActiId(map.get("fs_id")+"");
    	pav.setActiNo(map.get("fs_no")+"");
    	pav.setActiTitle(map.get("fs_title")+"");
    	pav.setPraiseNum(map.get("praNum")+"");
    	return pav;
    }
}
