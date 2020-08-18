package CPSC.Final.Copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class GenieTest {
	@Nested
	class TestPerson {
		@Test
		public void testPersonReflection() {
			Class<?>   iClass      = Person.class;
			Class<?>   iParent     = iClass.getSuperclass();
			assertEquals( "java.lang.Object", iParent.getName() );

			Class<?>[] iInterfaces = iClass.getInterfaces();
			for (Class<?> i : iInterfaces) {
				assertTrue( i.getName().endsWith( "Observable" ), ()->String.format("Class should implement interface 'Observer' only [found: %s]", i.getName() ));
			}			
			Field[]  iFields = iClass.getDeclaredFields();
			for (Field f : iFields) {
				if (!f.isSynthetic()) {
					assertTrue ( Modifier.isPrivate( f.getModifiers() ), ()->String.format("Field '%s' should be private", f.getName() ));
					assertFalse( Modifier.isStatic ( f.getModifiers() ), ()->String.format("Field '%s' can't be static",   f.getName() ));
				}
			}
		}
		@Test
		public void testNewPersonHasNoWishUntilSpeaking() {
			Person person = new Person();
			String left   = person.getWish();
			assertEquals( "", left );

			person.makeWish( "I wish I can fly" );
			left = person.getWish();
			assertEquals( "I wish I can fly", left );
			assertFalse ( person.hasObservers() );
		}
		@Test
		public void testAddsRemovesObservers() {
			Person   person = new Person();
			Observer a      = new Observer() {
				@Override
				public void update(Observable observable) {
				}
			};
			Observer b      = new Observer() {
				@Override
				public void update(Observable observable) {
				}
			};
			assertFalse( person.hasObservers() );
			assertTrue ( person.addObserver( a ));
			assertTrue ( person.hasObservers() );

			assertFalse( person.addObserver( a ));
			assertTrue ( person.hasObservers() );
			
			assertTrue ( person.addObserver( b ));
			assertTrue ( person.hasObservers() );
			assertTrue ( person.removeObserver( a ));
			assertTrue ( person.removeObserver( b ));
		
			assertFalse( person.removeObserver( a ));
}
		@Test
		public void testThrowException_PersonSpeaksNullOrEmpty() {
			for (String s : new String[] { null, new String() }) {
				Throwable t = assertThrows( IllegalArgumentException.class, 
						()-> new Person().makeWish( s ));
				assertEquals( "wishes cannot be null or empty", t.getMessage() );
			}
		}
		@Test
		public void testPersonHasSeveralConcurrentGenies() {
			Person person = new Person();
			Genie  genie1 = new Genie ( person );
			Genie  genie2 = new Genie ( person );
			Genie  genie3 = new Genie ( person );

			for (Genie genie : new Genie[] { genie1, genie2, genie3 }) {
				Person master = genie.getMaster();
				int    last   = genie.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 3, last );
				assertTrue  ( person.hasObservers() );
			}			
			person.makeWish( "I wish I had a pony" );
			for (Genie genie : new Genie[] { genie1, genie2, genie3 }) {
				Person master = genie.getMaster();
				int    last   = genie.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 2, last );
				assertTrue  ( person.hasObservers() );
			}			
			person.makeWish( "I wish everybody had a pony" );
			for (Genie genie : new Genie[] { genie1, genie2, genie3 }) {
				Person master = genie.getMaster();
				int    last   = genie.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 1, last );
				assertTrue  ( person.hasObservers() );
			}{
				Person master;
				int    last;
				// ask genie1 only
				genie1.update( person );
				master = genie1.getMaster();
				last   = genie1.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );
				master = genie2.getMaster();
				last   = genie2.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 1, last );
				master = genie3.getMaster();
				last   = genie3.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 1, last );

				assertTrue  ( person.hasObservers() );
				// ask genie2 only
				genie2.update( person );
				master = genie1.getMaster();
				last   = genie1.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );
				master = genie2.getMaster();
				last   = genie2.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );
				master = genie3.getMaster();
				last   = genie3.getWishesLeft();
				assertSame  ( person, master );
				assertEquals( 1, last );

				assertTrue  ( person.hasObservers() );
				// ask genie3 only
				genie3.update( person );
				master = genie1.getMaster();
				last   = genie1.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );
				master = genie2.getMaster();
				last   = genie2.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );
				master = genie3.getMaster();
				last   = genie3.getWishesLeft();
				assertNull  ( master );
				assertEquals( 0, last );

				assertFalse ( person.hasObservers() );
			}			
		}
	}
	@Nested
	class TestGenie {
		@Test
		public void testGenieReflection() {
			Class<?>   iClass      = Genie.class;
			Class<?>   iParent     = iClass.getSuperclass();
			assertEquals( "java.lang.Object", iParent.getName() );

			Class<?>[] iInterfaces = iClass.getInterfaces();
			for (Class<?> i : iInterfaces) {
				assertTrue( i.getName().endsWith( "Observer" ), ()->String.format("Class should implement interface 'Observer' only [found: %s]", i.getName() ));
			}
			Field[]    iFields     = iClass.getDeclaredFields();
			for (Field f : iFields) {
				if (!f.isSynthetic()) {
					assertTrue ( Modifier.isPrivate( f.getModifiers() ), ()->String.format("Field '%s' should be private", f.getName() ));
					assertFalse( Modifier.isStatic ( f.getModifiers() ), ()->String.format("Field '%s' can't be static",   f.getName() ));
				}
			}
		}
		@Test
		public void testNewGenieThrowsExceptionWithNullMaster() {
			Throwable t = assertThrows( IllegalArgumentException.class, 
					()-> new Genie( null ));
			assertEquals( "master cannot be null", t.getMessage() );
		}
		@Test
		public void testNewGenieHasMasterAndThreeWishesLeft() {
			Person person = new Person();
			Genie  genie  = new Genie ( person );

			Person master = genie.getMaster();
			int    left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, left );
		}
		@Test
		public void testThrowException_GenieAskedNoWish() {
			Person person = new Person();
			Genie  genie  = new Genie ( person );

			Person master = genie.getMaster();
			int    left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, left );

			for (String no_wish : new String[] { "I wise", "I witch", "wish I" }) {
				Throwable t = assertThrows( IllegalArgumentException.class, 
						()->person.makeWish( no_wish ) );
				assertEquals( "wish not granted [must start with 'I wish']", t.getMessage() );
				left = genie.getWishesLeft();
				assertEquals( 3, left );
			}		
			person.makeWish( "I wish I can fly" );
			master = genie.getMaster();
			left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 2, left );
		}
		@Test
		public void testThrowsException_NoWishGrantedToNonPeople() {
			Person person = new Person();
			Genie  genie  = new Genie ( person );

			Person master = genie.getMaster();
			int    last   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, last );

			for (Observable non_person : new Observable[] { null, new Observable() {
				@Override public boolean addObserver   (Observer observer) { return false; }
				@Override public boolean removeObserver(Observer observer) { return false; }
				@Override public boolean hasObservers  ()                  { return false; }
			}}) {
				Throwable t = assertThrows( IllegalArgumentException.class, ()->genie.update( non_person ) );
				assertEquals( "wish not granted [not a person]", t.getMessage() );
				last = genie.getWishesLeft();
				assertEquals( 3, last );
			}		
			person.makeWish( "I wish to travel through space" );
			master = genie.getMaster();
			last   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 2, last );
		}
		@Test
		public void testThrowsException_WishesGrantedToMasterOnly() {
			Person person = new Person();
			Genie  genie  = new Genie ( person );

			Person master = genie.getMaster();
			int    left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, left );

			Person other = new Person();
			other.makeWish ( "I wish I can fly" );
			Throwable t = assertThrows( IllegalArgumentException.class, 
					()->genie.update( other ));
			assertEquals( "wish not granted [not my master]", t.getMessage() );

			master = genie.getMaster();
			left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, left );

			person.makeWish( "I wish people can live in peace" );
			master = genie.getMaster();
			left   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 2, left );
		}
		@Test
		public void testGenieGrantsThreeWishesAndBecomesFree() {
			Person person = new Person();
			Genie  genie  = new Genie ( person );

			Person master = genie.getMaster();
			int    last   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 3, last );
			assertTrue  ( person.hasObservers() );

			person.makeWish( "I wish I can fly" );
			master = genie.getMaster();
			last   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 2, last );
			assertTrue  ( person.hasObservers() );

			person.makeWish( "I wish people can play music in bands" );
			master = genie.getMaster();
			last   = genie.getWishesLeft();
			assertSame  ( person, master );
			assertEquals( 1, last );
			assertTrue  ( person.hasObservers() );

			person.makeWish( "I wish to have tons of ice cream" );
			master = genie.getMaster();
			last   = genie.getWishesLeft();
			assertNull  ( master );
			assertEquals( 0, last );
			assertFalse ( person.hasObservers() );
			
			Throwable t = assertThrows( IllegalArgumentException.class, ()-> genie.update( person ));
			assertEquals( "wish not granted [not my master]", t.getMessage() );
		}
	}
}
