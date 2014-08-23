package cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ObjectBuilder {
	protected Hashtable<String, Object> table = new Hashtable<String, Object>();
	
	public Object getInstanceOf(Class className){
		Object [] nullArray = null;
		return getInstanceOf(className, nullArray);
	}
	
	public Object getInstanceOf(Class className, Object [] params){
		String classID = getClassID(className, params);
		Object object = table.get(classID);
		if(null == object){
			try {
				if(null == params || 0 == params.length){
					object = className.newInstance();
				}else{
					Constructor cons = findConstructor(className, params);
					if(null != cons) object = cons.newInstance(params);
				}
				if(null != object) table.put(classID, object);
			} catch (InstantiationException ie) {
				System.out.println("Instantiation Exception "+ie.getMessage());
			} catch (IllegalAccessException iae) {
				System.out.println("Illegal Access Exception ");
			} catch (IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
			} catch (SecurityException e) {
			}
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
	
	private Constructor findConstructor(Class className, Object[] params) {
		Constructor [] cons = className.getDeclaredConstructors();
		for(Constructor c : cons){
			Class [] args = c.getParameterTypes();
			int noOfArgs = args.length;
			if(noOfArgs == params.length){
				boolean found = true;
				for(int i = 0; i < noOfArgs; i++){
					if(TypeUtils.getWrapper(args[i]) != params[i].getClass()){
						found = false;
					}
				}
				if(found) return c;
			}
		}
		return null;
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
		Object [] nullArray = null;
		return call(obj, methodName, nullArray);
	}
	
	public Object call(Object obj,String methodName, Object [] params){
		if(null == obj) return null;
		Class className = obj.getClass();
		String methodID = getMethodID(className, methodName, params);
		Object object = table.get(methodID);
		if(null == object){
			Method meth = findMethod(methodName,params,className);
			if(null != meth){
				try {
					object = meth.invoke(obj, params);
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				} catch (InvocationTargetException e) {
				}
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
	
	private Method findMethod(String method, Object[] params, Class className) {
		Method [] meths = className.getMethods();
		for(Method meth : meths){
			if(StringUtils.equals(meth.getName(), method)){
				if(null == params || 0 == params.length) return meth;
				Class [] args = meth.getParameterTypes();
				int noOfArgs = args.length;
				if(noOfArgs == params.length){
					boolean found = true;
					for(int i = 0; i < noOfArgs; i++){
						if(TypeUtils.getWrapper(args[i]) != params[i].getClass()){
							found = false;
						}
					}
					if(found) return meth;
				}
			}
		}
		return null;
	}
}
