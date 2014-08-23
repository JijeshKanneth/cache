package cache;

import java.util.Hashtable;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

public class PageVars extends ObjectBuilder{
	private String pageClass = null;
	private PageVars(){}
	public static PageVars get(PageContext page){
		PageVars temp = (PageVars) page.getSession().getAttribute("_page_vars");
		if(null == temp){
			temp = new PageVars();
			temp.pageClass = page.getPage().getClass().getCanonicalName();
			page.getSession().setAttribute("_page_vars", temp);
		}else{
			if(!StringUtils.equals(temp.pageClass, page.getPage().getClass().getCanonicalName())){
				temp.table = new Hashtable<String, Object>();
				temp.pageClass = page.getPage().getClass().getCanonicalName();
			}
		}
		return temp;
	}
	
	public void clearVars(){
		this.table = new Hashtable<String, Object>();
	}
}
