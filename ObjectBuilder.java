package cache;

import javautils.ReflectWrapper;

@SuppressWarnings({"rawtypes"})
public class ObjectBuilder {
	private ReflectWrapper rw = new ReflectWrapper();
	protected ICachManager table = new SimpleCache();
	
	public void setCacheManager(ICachManager manager){
		table = manager;
	}
	
	public Object getInstanceOf(Class className){
		return rw.newInstance(className);
	}
	
	public Object getInstanceOf(Class className, Object [] params){
		String classID = getClassID(className, params);
		Object object = table.get(classID);
		if(null == object){
			if(null == params || 0 == params.length){
				object = rw.newInstance(className);
			}else{
				object = rw.newInstance(className, params);
			}
			if(null != object) table.put(classID, object);
		}
		return object;	
	}

	private String getClassID(Class className, Object[] params) {
		StringBuffer sb = new StringBuffer();
		sb.append(className.getCanonicalName());
		if(null != params){
			for(int i = 0; i < params.length; i++){
				sb.append(params[i].hashCode());
			}
		}
		return sb.toString();
	}
	
	private String getMethodID(Class _class, String method, Object[] params) {
		StringBuffer sb = new StringBuffer();
		sb.append(_class.getCanonicalName());
		sb.append(method);
		if(null != params){
			for(int i = 0; i < params.length; i++){
				sb.append(params[i].hashCode());
			}
		}
		return sb.toString();
	}	
	
	public Object call(Object obj, String methodName){
		return call(obj, methodName, null);
	}
	
	public Object call(Object obj,String methodName, Object [] params){
		if(null == obj || null == methodName) return null;
		Class className = obj.getClass();
		String methodID = getMethodID(className, methodName, params);
		Object object = table.get(methodID);
		if(null == object){
			if(null == params){
				object = rw.invoke(obj, methodName);
			}else{
				object = rw.invoke(obj, methodName, params);
			}
			if(null != object) table.put(methodID, object);
		}
		return object;
	}
	
	public Object call(Class _class, String methodName,Object [] params){
		return call(getInstanceOf(_class), methodName, params);
	}
	
	public Object call(Class _class, String methodName){
		return call(getInstanceOf(_class), methodName);
	}

}
