package cache;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author JK
 * @version 1.1
 */
public class SessionVars extends ObjectBuilder{
	private SessionVars(){
	}
	
	public static SessionVars get(HttpSession session){
		SessionVars temp = (SessionVars) session.getAttribute("_session_vars");
		if(null == temp){
			temp = new SessionVars();
			session.setAttribute("_session_vars", temp);
		}
		return temp;
	}
	
	@Deprecated
	public Object getInstanceOf(@SuppressWarnings("rawtypes") Class className, HttpServletRequest request){
		return getInstanceOf(className, new Object []{ request });
	}
}
