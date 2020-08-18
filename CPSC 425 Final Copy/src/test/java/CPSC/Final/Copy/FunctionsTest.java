package CPSC.Final.Copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FunctionsTest {
	private static final String       FILENAME = "cities.csv";
	private static       List<City>   CITIES   = null;
	private static       Path         PATH;

	private static Path getPath(String filename) throws URISyntaxException {
		URL url = FunctionsTest.class.getResource(filename);
		return url == null ? null : Path.of( url.toURI() );
	}
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
	private static <K,V> void assertEqualMaps(Map<K,V> expected, Map<K,V> actual) {
		assertNotNull( actual );
		Set<K>  aKeys = actual  .keySet();
		Set<K>  eKeys = expected.keySet();
		assertEqualSets( eKeys, aKeys );
		for (K key : eKeys) {
			V aValue = actual  .get( key );
			V eValue = expected.get( key );
			assertEquals( eValue, aValue, ()->String.format("key '%s'", key ));
		}
	}

	@BeforeAll
	static void fileFound() throws IOException {
		try {
			PATH   = getPath( FILENAME );
			CITIES = Files.lines( PATH, StandardCharsets.ISO_8859_1 ).map( s -> s.split(", ")).map( a -> new City(a[0],a[1],Integer.parseInt(a[2]))).collect(Collectors.toList());
		} catch (URISyntaxException e) {
			PATH   = null;
			e.printStackTrace();
		}
	}
	@BeforeEach
	void testFileFound() {
		assertNotNull( PATH, ()->String.format("'%s' not found (place it on the same folder as this test file)", FILENAME ));
	}
	
	@ParameterizedTest
	@MethodSource("data_mostPopulated")
	void test_mostPopulated(Stream<City> stream, Optional<City> expected) {
		assertNotNull( Functions.mostPopulated );
		Optional<City> actual = Functions.mostPopulated.apply( stream );
		assertNotNull( actual );
		assertEquals ( expected, actual );
	}
	static Stream<Arguments> data_mostPopulated() {
	    return Stream.of(
	    	Arguments.of( Stream  .empty(), 
	    			      Optional.empty() ),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "VA" ).contains( c.state )),
	    			      Optional.of( new City("Virginia Beach","VA",447021 ))),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "CA" ).contains( c.state )),
  			              Optional.of( new City("Los Angeles","CA",3857799 ))),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "FL", "GA" ).contains( c.state )),
  			              Optional.of( new City("Jacksonville","FL",836507 ))),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "TX", "NM", "WA" ).contains( c.state )),
		                  Optional.of( new City("Houston","TX",2160821 ))),
	    	Arguments.of( CITIES.stream(),
		                  Optional.of( new City("New York","NY",8336697 )))
	    );
	}

	@ParameterizedTest
	@MethodSource("data_stateCities")
	void test_stateCities(Stream<City> stream, String state, List<City> expected) {
		assertNotNull( Functions.cities );
		Set<City>      actual = Functions.cities.apply( stream, state );
		assertNotNull( actual );
		assertEqualSets( expected, actual );
	}
	static Stream<Arguments> data_stateCities() {
	    return Stream.of(
	    	Arguments.of( Stream.empty(), 
  			              "AL",
	    			      List.of() ),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "NV" ).contains( c.state )),
	    	              "NV",
	    	              CITIES.stream().filter( c->List.of( "NV" ).contains( c.state )).collect( Collectors.toList() )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "AK","OR","MT" ).contains( c.state )),
  	                      "OR",
  	                      CITIES.stream().filter( c->List.of( "OR" ).contains( c.state )).collect( Collectors.toList() ) ),
	    	Arguments.of( CITIES.stream(),
	    			      "MA",
	    			      CITIES.stream().filter( c->List.of( "MA" ).contains( c.state )).collect( Collectors.toList() ) ),
	    	Arguments.of( CITIES.stream(),
  			              "NC",
  			              CITIES.stream().filter( c->List.of( "NC" ).contains( c.state )).collect( Collectors.toList() ) )
	    );
	}

	@ParameterizedTest
	@MethodSource("data_stateAverage")
	void test_stateAverage(Stream<City> stream, List<String> states, OptionalDouble expected) {
		assertNotNull( Functions.average );
		OptionalDouble actual = Functions.average.apply( stream, states );
		assertNotNull( actual );
		if (expected.isEmpty()) {
			assertTrue  ( actual.isEmpty() );
		} else {
			assertTrue  ( actual.isPresent() );
		    assertEquals( expected.getAsDouble(), actual.getAsDouble(), 0.001 );
		}
	}
	static Stream<Arguments> data_stateAverage() {
	    return Stream.of(
	    	Arguments.of( Stream        .empty(), 
			              List.of(),
	    			      OptionalDouble.empty() ),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "HI" ).contains( c.state )),
	    			      List.of( "HI" ),
	    	              OptionalDouble.of( 345610.0 )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "OK","SC" ).contains( c.state )),
	    			      List.of( "OK","WV" ),
  	                      OptionalDouble.of( 188489.75 )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "RI","CA","ID" ).contains( c.state )),
	    			      List.of( "RI","ID" ),
  	                      OptionalDouble.of( 100144.33333333333 )),
	    	Arguments.of( CITIES.stream(),
	    			      List.of( "NJ","NY","MA" ),
	    			      OptionalDouble.of( 259924.7358490566 ))
	    );
	}
	
	@ParameterizedTest
	@MethodSource("data_citiesPerState")
	void test_citiesPerState(Stream<City> stream, Map<String,Long> expected) {
		assertNotNull( Functions.citiesPerState );
		Map<String,Long> actual = Functions.citiesPerState.apply( stream );
		assertNotNull  ( actual );
		assertEqualMaps( expected, actual );
	}
	static Stream<Arguments> data_citiesPerState() {
	    return Stream.of(
		  	Arguments.of( Stream.empty(), 
			              Map.of() ),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "KS" ).contains( c.state )),
	    	              Map.of( "KS", 8L )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "WI","WV" ).contains( c.state )),
  	                      Map.of( "WI", 12L, "WV", 1L )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "MI","MN","PA" ).contains( c.state )),
  	                      Map.of( "MI", 24L, "MN", 17L, "PA", 8L )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "NE","IL","ME","MD" ).contains( c.state )),
  	                      Map.of( "NE", 3L,"IL", 29L,"ME", 1L ,"MD", 5L ))
	    );
	}

	@ParameterizedTest
	@MethodSource("data_inRange")
	void test_inRange(Stream<City> stream, int low, int hi, List<String> expected) {
		assertNotNull( Functions.inRange );
		List<String>   actual = Functions.inRange.apply( stream ).apply( low ).apply( hi );
		assertNotNull( actual );
		assertEqualLists( expected, actual );
	}
	static Stream<Arguments> data_inRange() {
	    return Stream.of(
			Arguments.of( Stream.empty(),
					      0, 1_000_000,
			              List.of() ),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "AZ" ).contains( c.state )),
				          100_000, 200_000,
				          List.of("Surprise,AZ", // 121_287
				        		  "Peoria,AZ",   // 159_789
				        		  "Tempe,AZ"     // 166_842
				        		  )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "CA","CO" ).contains( c.state )),
				          140_000, 150_000,
				          List.of("Lakewood,CO",     // 145_516
                                  "Sunnyvale,CA",    // 146_197
				        		  "Torrance,CA",     // 147_027
				        		  "Escondido,CA",    // 147_575
				        		  "Fort Collins,CO", // 148_612
				        		  "Hayward,CA"       // 149_392
				        		  )),
	    	Arguments.of( CITIES.stream().filter( c->List.of( "TN","NM","NC" ).contains( c.state )),
				          500_000, 800_000,
				          List.of("Albuquerque,NM",        // 555_417
				        		  "Nashville-Davidson,TN", // 624_496
				        		  "Memphis,TN",            // 655_155
				        		  "Charlotte,NC"           // 775_202
				        		  )),
	    	Arguments.of( CITIES.stream(),
				          1_000_000, 10_000_000,
				          List.of("Dallas,TX",       // 1_241_162
				        		  "San Diego,CA",    // 1_338_348
				        		  "San Antonio,TX",  // 1_382_951
				        		  "Phoenix,AZ",      // 1_488_750
				        		  "Philadelphia,PA", // 1_547_607
				        		  "Houston,TX",      // 2_160_821
				        		  "Chicago,IL",      // 2_714_856
				        		  "Los Angeles,CA",  // 3_857_799
				        		  "New York,NY"      // 8_336_697
				        		  ))
	    );
	}
}
