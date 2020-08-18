package CPSC.Final.Copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReflectionsTest {
	private static <T> void assertEqualSets(Collection<T> expected, Collection<T> actual) {
		assertNotNull( actual );
		List<T> aList = new ArrayList<>( actual );
		List<T> eList = new ArrayList<>( expected );
		while (!eList.isEmpty()) {
			T e = eList.remove(0);
			if (aList.isEmpty()) {
				fail( String.format( "missing results %s%nexpected %s%nactual %s%n", eList, expected, actual ));
			} else {
				assertTrue( aList.remove( e ), ()->String.format( "'%s' not found.%nexpected %s%nactual %s%n"+
						"remaining expected %s%nremaining actual %s",
						e, expected, actual, eList, aList ));
			}
		}
		if (!aList.isEmpty()) {
			fail( String.format( "excess results %s%nexpected %s%nactual %s", aList, expected, actual ));
		}
	}
	private static <T> void assertEqualLists(Collection<T> expected, Collection<T> actual) {
		assertNotNull( actual );
		List<T> aList = new ArrayList<>( actual );
		List<T> eList = new ArrayList<>( expected );
		int     index = 0;
		while (!eList.isEmpty()) {
			T e = eList.remove( 0 );
			if (aList.isEmpty()) {
				fail( String.format( "missing results %s%nexpected %s%nactual %s%n",
					 	              eList, expected, actual ));
			} else {
				T a = aList.remove( 0 );
				int i = index;
				assertEquals( e, a, () ->
				           String.format( "Unexpected result @ index %d%nexpected %s%nactual %s%n",
						   i, expected, actual ));
			}
			index++;
		}
		if (!aList.isEmpty()) {
			fail( String.format( "excess results %s%nexpected %s%nactual %s",
							      aList, expected, actual ));
		}
	}

	@ParameterizedTest
	@MethodSource("data_getSupers")
	void test_getSupers(Object obj, List<Class<?>> expected) {
		List<Class<?>> actual = Reflections.getSupers( obj );
		assertNotNull( actual );
		assertEqualLists( expected, actual );
	}
	static Stream<Arguments> data_getSupers() {
	    return Stream.of(
		    	Arguments.of( new Object(),      List.of( Object.class )),
		    	Arguments.of( "",                List.of( Object.class, String.class )),
		    	Arguments.of( 42,                List.of( Object.class, Number.class, Integer.class )),
		    	Arguments.of( new ArrayList<>(), List.of( Object.class, AbstractCollection.class, AbstractList.class, ArrayList.class )),
		    	Arguments.of( new JLabel(),      List.of( Object.class, Component.class, Container.class, JComponent.class, JLabel.class ))
	    		);
	}

	@ParameterizedTest
	@MethodSource("data_getMethods")
	void test_getMethods(Class<?> clazz, List<String> expected) {
		List<Method> actual = Reflections.getMethods( clazz );
		assertNotNull( actual );
		assertEqualSets( expected, actual.stream().map(Method::getName).collect(Collectors.toList()) );
	}
	protected static class OnePublic {
		public int foo;
		private char bar;
		private double meh = Math.PI;
		private boolean jinx;
		private static long blah = 42;
		public        void foo() { meh(); }
		protected     void bar() { foo = bar * meh > 0 ? 1 : 0; }
		private       void meh() { meh *= jinx ? 1 : 0; }
		public static long jinx() { return blah; }
	}
	static Stream<Arguments> data_getMethods() {
	    return Stream.of(
		    	Arguments.of( Object   .class, List.of( "wait", "wait", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll" )),
		    	Arguments.of( Math     .class, List.of() ),
		    	Arguments.of( Iterator .class, List.of( "remove","forEachRemaining","next","hasNext") ),
		    	Arguments.of( OnePublic.class, List.of( "foo" ))
	    		);
	}

	
	@ParameterizedTest
	@MethodSource("data_toString")
	void test_toString(Class<?> clazz, String expected) {
		String actual = Reflections.toString( clazz );
		assertNotNull( actual );
		assertEquals( expected, actual );
	}
	private class PrivateClass { }
	private static class PrivateStaticClass { }
	protected static class ProtectedStaticClass { }
	protected abstract class ProtectedAbstractClass { }
	public final static class PublicFinalStaticClass { }
	public abstract static class PublicAbstractStaticClass { }
	class NoneClass {}
	final class NoneFinalClass {}
	abstract class NoneAbstractClass {}
	interface NoneInterface {}
	abstract interface NoneAbstractInterface {}
	protected interface ProtectedInterface {}
	private abstract interface PrivateAbstractInterface {}
	public class NoDefaultConstructor {
		public NoDefaultConstructor(int a) {
		}
	}
	protected static enum ProtectedStaticEnum { T, M, A, O, L, S };
	private static   enum PrivateStaticEnum   { X, B, O };
	static           enum NoneStaticEnum      { FOO, BAR, JINX, MEH };
	
	static Stream<Arguments> data_toString() {
	    return Stream.of(
		    	Arguments.of( Object.class,                    "public class Object" ),
		    	Arguments.of( String.class,                    "public final class String" ),
		    	Arguments.of( PrivateClass.class,              "private class PrivateClass" ),
		    	Arguments.of( PrivateStaticClass.class,        "private static class PrivateStaticClass" ),
		    	Arguments.of( ProtectedStaticClass.class,      "protected static class ProtectedStaticClass" ),
		    	Arguments.of( ProtectedAbstractClass.class,    "protected abstract class ProtectedAbstractClass" ),
		    	Arguments.of( PublicFinalStaticClass.class,    "public final static class PublicFinalStaticClass" ),
		    	Arguments.of( PublicAbstractStaticClass.class, "public abstract static class PublicAbstractStaticClass" ),
		    	Arguments.of( NoneClass.class,                 "class NoneClass" ),
		    	Arguments.of( NoneFinalClass.class,            "final class NoneFinalClass" ),
		    	Arguments.of( NoneAbstractClass.class,         "abstract class NoneAbstractClass" ),
		    	Arguments.of( Iterator.class,                  "public interface Iterator" ),
		    	Arguments.of( NoneInterface.class,             "interface NoneInterface" ),
		    	Arguments.of( NoneAbstractInterface.class,     "interface NoneAbstractInterface" ),
		    	Arguments.of( ProtectedInterface.class,        "protected interface ProtectedInterface" ),
		    	Arguments.of( PrivateAbstractInterface.class,  "private interface PrivateAbstractInterface" ),
		    	Arguments.of( ProtectedStaticEnum.class,       "protected static enum ProtectedStaticEnum" ),
		    	Arguments.of( PrivateStaticEnum.class,         "private static enum PrivateStaticEnum" ),
		    	Arguments.of( NoneStaticEnum.class,            "static enum NoneStaticEnum" )
	    		);
	}
}
