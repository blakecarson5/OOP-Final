package CPSC.Final.Copy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Reflections {
	public static List<Class<?>> getSupers(Object obj) {
		List<Class<?>> supers = new ArrayList<Class<?>>();
		List<Class<?>> result = new ArrayList<Class<?>>();
		
		Class<?> myclass = obj.getClass();
		supers.add(myclass);
		Class<?> superclass = myclass.getSuperclass();

		while (superclass != null) {
			supers.add(superclass);
			myclass = superclass;
			superclass = myclass.getSuperclass();
		}
		
		for(int i = supers.size() - 1; i >= 0; i--)
			result.add(supers.get(i));
		
		return result;
	}
	public static List<Method> getMethods(Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		Method[] m = clazz.getDeclaredMethods();
		
		for(int i = 0; i < m.length; i++) {
			if(!(Modifier.isStatic(m[i].getModifiers())) && Modifier.isPublic(m[i].getModifiers()))
				methods.add(m[i]);
		}
		
		return methods;
	}
	public static String toString(Class<?> clazz) {
		String result = "";
		
		if(Modifier.isPublic(clazz.getModifiers()))
			result += "public";
		else if(Modifier.isPrivate(clazz.getModifiers()))
			result += "private";
		else if(Modifier.isProtected(clazz.getModifiers()))
			result += "protected";
		
		if(!(clazz.isInterface())) {
			if(Modifier.isFinal(clazz.getModifiers()) && !(clazz.isEnum()))
				result += " final";
			
			if(Modifier.isAbstract(clazz.getModifiers()))
				result += " abstract";
			
			if(Modifier.isStatic(clazz.getModifiers()))
				result += " static";
		}
		
		if(clazz.getClass() instanceof Class<?> && !(clazz.isInterface()) && !(clazz.isEnum()))
			result += " class";
		else if(clazz.getClass() instanceof Class<?> && !(clazz.isInterface()) && clazz.isEnum())
			result += " enum";
		else if(clazz.getClass() instanceof Class<?> && clazz.isInterface() && !(clazz.isEnum()))
			result += " interface";
		
		result += " " + clazz.getSimpleName();
		
		return result.trim();
	}	
}