package CPSC.Final.Copy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Functions {
	BiFunction<Stream<City>,String,Set<City>>            cities         = (s, state) -> s.filter(city -> city.state.equals(state))
			.collect(Collectors.toSet());
	Function<Stream<City>,Optional<City>>                mostPopulated  = s -> s.max((a,b) -> a.population - b.population);
	BiFunction<Stream<City>,List<String>,OptionalDouble> average        = (s, states) -> s.mapToDouble(city -> city.population).average();
	Function<Stream<City>,Map<String,Long>>              citiesPerState = s -> s.map(x -> x.state)
			.collect(Collectors.groupingBy(a -> a, Collectors.counting()));
	Function<Stream<City>,Function<Integer,Function<Integer,List<String>>>> inRange = null;
}
