package CPSC.Final.Copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EarthTest {
	private static final String CLASS_NAME = "Earth";
	
	private Class<?> getClazz(String name) {
		Class<?> result = null;
		try {
			Package pkg  = getClass().getPackage();
			String  path = (pkg == null || pkg.getName().isEmpty()) ? "" : pkg.getName()+".";
			result = Class.forName( path + name );
		} catch (ClassNotFoundException e) {
			fail( String.format( "Class %s not found", name ));
		}
		return result;
	}
	private List<Field> getStaticFields() {
		List<Field> result = new ArrayList<>();
		Class<?>    cls    = getClazz( CLASS_NAME );
		Field[]     fields = cls.getDeclaredFields();
		for (Field f : fields) {
			if (!f.isSynthetic()) {
				int modifier = f.getModifiers();
				if (Modifier.isStatic ( modifier ))  {
					result.add( f );
				}
			}
		}
		return result;
	}
	private static Object  initial = null;
	private static boolean wasRead = false;
	@BeforeEach
	public void resetStaticFieldInitialValue() {
		try {
			List<Field> fields = getStaticFields();
			if (fields.size() == 1) {
				Field field = fields.get(0);
				field.setAccessible( true );
				if (wasRead) {
					field.set( null, initial );
				} else {
					initial = field.get( null );
					wasRead    = true;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
	}

	@Test
	public void testFieldsArePrivate() {
		Class<?> cls   = getClazz( CLASS_NAME );
		for (Field field : cls.getDeclaredFields()) {
			if (!field.isSynthetic()) {
				int modifier = field.getModifiers();
				assertTrue( Modifier.isPrivate( modifier ), String.format( "field \"%s\" should be private", field.getName()) );
			}
		}
	}
	private Constructor<?> getPrivateConstructor() {
		Constructor<?>    result       = null;
		Class<?>          cls          = getClazz( CLASS_NAME );
		Constructor<?>[]  constructors = cls.getDeclaredConstructors();
		for (Constructor<?> c : constructors) {
			int modifier = c.getModifiers();
			if (Modifier.isPrivate( modifier )) {
				if (result == null) {
					result = c;
				} else {
					fail( "More than one private constructor found" );
				}
			} else {
				fail( "non-private constructor found" );
			}
		}
		return result;
	}
	@Test
	public void testOnePrivateConstructor() {
		Constructor<?> constructor  = getPrivateConstructor();
		assertNotNull( constructor, "No private constructor found" );
	}
	@Test
	public void testOneStaticFieldEager() {
		try {
			List<Field> fields = getStaticFields();
			assertEquals( 1, fields.size(), "More than one static field found" );
			Field field = fields.get(0);
			field.setAccessible( true );
			Object value = field.get( null );
			assertNotNull( value, "static field is null" );
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail( "error accessing static field " );
		}
	}
	@Test
	public void testGetInstanceSameObject() {
		Object a = Earth.getInstance();
		assertEquals ( getClazz( CLASS_NAME ), a.getClass(), ()->"getInstance() doesn't return a "+CLASS_NAME+" object" );
		Object b = Earth.getInstance();
		assertNotNull( a,      "getInstance() returns null" );
		assertTrue   ( a == b, "getInstance() returns different objects" );
	}
	@Test
	public void testToString() {
		Earth a = Earth.getInstance();
		int   b = a.plantTree();
		assertNotNull( a, "getInstance() returns null" );
		for (int i = b; i < b+3; i++) {
			String actual   = a.toString();
			String expected = String.format( "Planet Earth [trees=%d]", i );
			assertEquals ( expected, actual );
			a.plantTree();
		}
	}
	@Test
	public void testPlantTree() {
		Earth a        = Earth.getInstance();
		int   actual   = a.plantTree();
		int   expected = actual;
		assertNotNull( a, "getInstance() returns null" );
		for (int i = 0; i < 3; i++) {
			assertEquals ( expected, actual );
			actual = a.plantTree();
			expected++;
		}
	}
}
