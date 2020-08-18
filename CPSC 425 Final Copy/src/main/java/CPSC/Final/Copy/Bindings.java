package CPSC.Final.Copy;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Bindings {
	public static DoubleBinding getCircumference(DoubleProperty radius) {
		DoubleProperty two = new SimpleDoubleProperty(2);
		DoubleProperty pi = new SimpleDoubleProperty(Math.PI);
		DoubleBinding result = radius.multiply(two).multiply(pi);
		return result;
	}
	public static DoubleBinding getArea(DoubleProperty radius) {
		DoubleProperty pi = new SimpleDoubleProperty(Math.PI);
		DoubleBinding result = pi.multiply(radius).multiply(radius);
		return result;
	}
}
