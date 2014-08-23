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
		if(object == null){
			try {
				if(params == null || params.length == 0){
					object = className.newInstance();
				}else{
					Constructor cons = findConstructor(className, params);
					if(cons != null) object = cons.newInstance(params);
				}
				if(object != null) table.put(classID, object);
			} catch (InstantiationException e) {
				System.out.println("Instantiation Exception ");
			} catch (IllegalAccessException e) {
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
		if(params != null){
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
		if(params != null){
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
		Class className = obj.getClass();
		String methodID = getMethodID(className, methodName, params);
		Object object = table.get(methodID);
		if(object == null){
			Method meth = findMethod(methodName,params,className);
			if(meth != null){
				 try {
					object = meth.invoke(obj, params);
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				} catch (InvocationTargetException e) {
				}
			}
			if(object != null) table.put(methodID, object);
		}
		return object;
	}
	
	private Method findMethod(String method, Object[] params, Class className) {
		Method [] meths = className.getMethods();
		for(Method meth : meths){
			if(StringUtils.equals(meth.getName(), method)){
				if(params == null || params.length == 0) return meth;
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
