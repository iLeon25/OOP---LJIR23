package hr.fer.oop.hpi;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HPIProcessor {

	public static Function<Stream<String>, Set<HousePriceIndex>> loadFromStream = lines -> 
		lines.map(s -> s.split("#")).map(array -> {
			String region = array[1];
			RegionType rt = null;
			String time = array[2];
			double value = Double.parseDouble(array[3]);
			
			switch (Integer.parseInt(array[0])) {
				case 1 -> {
					rt = RegionType.COUNTRY;
				}
				case 2 -> {
					rt = RegionType.LARGE_REGION;
					
				}
				case 3 -> {
					rt = RegionType.SMALL_REGION;
					
				}
				case 4 -> {
					rt = RegionType.CITY;
				}
			}
			return new HousePriceIndex(region, rt, time, value);
		}).collect(Collectors.toSet());
	
	
	public static BiFunction<Set<HousePriceIndex>, RegionType, Map<String, Double>> collectionToMap = (set, regionType) -> {
		return set.stream().filter((a) -> a.getRegionType() == regionType)
				.collect(Collectors.toMap((a) -> a.getRegion().concat("#").concat(a.getTime()),
										  (b) -> b.getValue()));
	};
	
	public static BiFunction<Map<String, Double>, String, Collection<Entry<String, Double>>> getValuesForTime = (map, time) -> 
		map.entrySet().stream().filter((a) -> a.getKey().contains(time))
							   .map((entry) -> new SimpleEntry<>(entry.getKey().split("#")[0], entry.getValue()))
							   .sorted(new Comparator<Entry<String, Double>>() {

								@Override
								public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
									double r = Double.compare(o1.getValue(), o2.getValue());
									
									if (r < 0) {
										return -1;
										
									} else if (r > 0) {
										return 1;
									}
									
									return 0;
								}   
							}).collect(Collectors.toList());
	}
	
	
	
	
	
	
