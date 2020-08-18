package CPSC.Final.Copy;

import java.util.Objects;

public class City {
	public final String name;
	public final String state;
	public final int    population;

	public City(String name, String state, int population) {
		this.name       = name;
		this.state      = state;
		this.population = population;
	}
	@Override
	public int hashCode() {
		return Objects.hash( name, state, population );
	}
	@Override
	public boolean equals(Object obj) {
		if (this != null) {
			if (this == obj) {
				return true;
			}
			if (getClass() == obj.getClass()) {
				City other = (City) obj;
				if (name .equals( other.name ) &&
					state.equals( other.state) &&
					population == other.population) {
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "City [name=" + name + ", state=" + state + ", population=" + population + "]";
	}
}
