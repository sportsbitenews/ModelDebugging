package org.gemoc.gemoc_language_workbench.extensions.k3.dsa.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.emf.ecore.EObject;
import org.gemoc.gemoc_language_workbench.api.dsa.IDSAExecutor;
import org.gemoc.gemoc_language_workbench.api.dsa.IDSAExecutorCommand;

/**
 * Executor that is able to find the helper class associated with a given object
 * It also works for aspect on EMF:
 * 	- In case of EObject, (target or parameter)  it is also able to find the appropriate interface when looking for the method
 * @author dvojtise
 *
 */
public class K3DSAAspectExecutor implements IDSAExecutor {

	protected ClassLoader classLoader;
	protected String bundleSymbolicName;
	
	public K3DSAAspectExecutor(ClassLoader classLoader, String bundleSymbolicName) {
		this.classLoader = classLoader;
		this.bundleSymbolicName = bundleSymbolicName;
	}

	@Override
	public Object execute(Object target, String methodName,
			ArrayList<Object> parameters) {
		IDSAExecutorCommand command = this.getCommand(target, methodName, parameters);
		if(command != null){
			try {
				return command.execute();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		NoSuchMethodException e = new NoSuchMethodException("cannot find applicable method "+methodName+" and matching parameters on "+target);
		e.printStackTrace();
		// TODO should return the fact that it cannot be executed via an exception (NoSuchMethodException ? or custom ?)
		return null;
	}

	@Override
	public IDSAExecutorCommand getCommand(Object target, String methodName,
			ArrayList<Object> parameters) {
		
		
		
		Class<?> staticHelperClass = getStaticHelperClass(target);
		if(staticHelperClass == null) return null; // no applicable static class found
		
		Class<?>[] parameterTypes = new Class<?>[parameters != null?parameters.size()+1:1];
		ArrayList<Class<?>> parameterTypesList = new ArrayList<Class<?>>();
		parameterTypesList.add(getInterfaceClassOfEObjectOrClass(target)); // add the target as first param
		if(parameters != null){
			for(Object param : parameters){
				parameterTypesList.add(getInterfaceClassOfEObjectOrClass(param));
			}
		}
		parameterTypes = parameterTypesList.toArray(parameterTypes);
		try {
			Method method = staticHelperClass.getMethod(methodName, parameterTypes);
			
			return new K3DSAAspectExecutorCommand(staticHelperClass, target, method, parameters);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/** search static class by name  (future version should use a map of available aspects, and deals with it as a list of applicable static classes)
	 *
	 */
	protected Class<?> getStaticHelperClass(Object target){
		
		String searchedAspectizedClassCanonicalName = getInterfaceClassOfEObjectOrClass(target).getCanonicalName();
				
		String searchedPropertyFileName = "/META-INF/xtend-gen/"+bundleSymbolicName+".k3_aspect_mapping.properties";
		Properties properties = new Properties();
		InputStream inputStream =classLoader.getResourceAsStream(searchedPropertyFileName);
		if(inputStream == null){
			try {
				inputStream = org.eclipse.core.runtime.Platform.getBundle(bundleSymbolicName).getEntry(searchedPropertyFileName).openStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		String possibleStaticClassName= null;
		try {
			if(inputStream != null){
				properties.load(inputStream);
				possibleStaticClassName = properties.getProperty(searchedAspectizedClassCanonicalName);
			}
		} catch (IOException e) {
			// TODO report for debug that no mapping was found
			return null;
		}
		if(possibleStaticClassName == null) {
			return null;
			
		}
		
		try {
			return Class.forName(possibleStaticClassName, true, classLoader);
		} catch (ClassNotFoundException e) {}
		return null;
	}
	
	/**
	 * returns the class of o or the interface that o implements in the case of EObjects
	 * @param o
	 * @return
	 */
	public Class<?> getInterfaceClassOfEObjectOrClass(Object o){
		if(o instanceof EObject){
			/*String targetClassCanonicalName = o.getClass().getCanonicalName();
			String searchedAspectizedClassCanonicalName = targetClassCanonicalName;
			// apply special rules to retrieve the Ecore interface instead of the Impl 
			String searchedAspectizedClasPackageName = targetClassCanonicalName.substring(0, targetClassCanonicalName.lastIndexOf("."));
			searchedAspectizedClasPackageName = searchedAspectizedClasPackageName.substring(0, searchedAspectizedClasPackageName.lastIndexOf(".impl"));
			searchedAspectizedClassCanonicalName = searchedAspectizedClasPackageName+"."+((EObject)o).eClass().getName();*/
			Class<?> interfaces[] = o.getClass().getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				Class<?> interfac = interfaces[i];
				if(interfac.getSimpleName().equals(((EObject)o).eClass().getName())){
					return interfaces[i];
				}
			}
			return o.getClass();
			
		}
		else{
			return o.getClass();
		}
			
	}
	
	
	/**
	 * Command that is able to execute the required method
	 *
	 */
	public class K3DSAAspectExecutorCommand implements IDSAExecutorCommand{

		protected Class<?> staticHelperClass;
		protected Method method;
		protected ArrayList<Object> staticParameters = new ArrayList<Object>();
		
		public K3DSAAspectExecutorCommand(Class<?> staticHelperClass, Object target, Method method,
				ArrayList<Object> parameters) {
			this.staticHelperClass = staticHelperClass;
			this.method = method;
			this.staticParameters.add(target);
			if(parameters != null){
				this.staticParameters.addAll(parameters);
			}
		}

		@Override
		public Object execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object[] args = new Object[0];
			if(staticParameters != null){
				args =staticParameters.toArray();
			}
			return method.invoke(null, args); 
		}
		
	}
	
	
	

}
