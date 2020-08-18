package CPSC.Final.Copy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

class BindingsTest {

	@ParameterizedTest
	@MethodSource("data_getCircumference")
	void test_getCircumference(double expected, DoubleProperty radius) {
		DoubleBinding  actual = Bindings.getCircumference( radius );
		assertNotNull( actual );
		assertEquals ( expected,      actual.getValue(), 0.0001 );

		radius.setValue( 1 );
		assertEquals   ( 2 * Math.PI, actual.getValue(), 0.0001 );
	}
	static Stream<Arguments> data_getCircumference() {
	    return Stream.of(
		    	Arguments.of(  0.0,            new SimpleDoubleProperty( 0.0 )),
		    	Arguments.of( 31.415926535898, new SimpleDoubleProperty( 5.0 )),
		    	Arguments.of( 60.946897479642, new SimpleDoubleProperty( 9.7 ))
        		);
	}

	@ParameterizedTest
	@MethodSource("data_getArea")
	void test_getArea(double expected, DoubleProperty radius) {
		DoubleBinding  actual = Bindings.getArea( radius );
		assertNotNull( actual );
		assertEquals ( expected,  actual.getValue(), 0.0001 );

		radius.setValue( 1 );
		assertEquals   ( Math.PI, actual.getValue(), 0.0001 );
	}
	static Stream<Arguments> data_getArea() {
	    return Stream.of(
		    	Arguments.of(   0.0,            new SimpleDoubleProperty( 0.0 ) ),
		    	Arguments.of(  78.539816339745, new SimpleDoubleProperty( 5.0 ) ),
		    	Arguments.of( 295.59245277626,  new SimpleDoubleProperty( 9.7 ) )
        		);
	}
}