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
   	lines.map(line -> line.split("#"))
   	.map(array -> {
   		RegionType regionType = null;
   		
   		switch (Integer.parseInt(array[0])) {
   			case 1 -> {
   				regionType = RegionType.COUNTRY;
   				
   			}
   			case 2 -> {
   				regionType = RegionType.LARGE_REGION;
   				
   			}
   			case 3 -> {
   				regionType = RegionType.SMALL_REGION;
   				
   			}
   			case 4 -> {
   				regionType = RegionType.CITY;
   			}
   		}
   		return new HousePriceIndex(array[1], regionType, array[2], Double.parseDouble(array[3]));
   		
   		
   	}).collect(Collectors.toSet());  	
   

   public static BiFunction<Set<HousePriceIndex>, RegionType, Map<String, Double>> collectionToMap = (set, regionType) -> {
   	return set.stream().filter(a -> a.getRegionType() == regionType)
   			.collect(Collectors.toMap(a -> a.getRegion().concat("#").concat(a.getTime()), a -> a.getValue()));

   };

   public static BiFunction<Map<String, Double>, String, Collection<Entry<String, Double>>> getValuesForTime = (map, time) -> {
   	return map.entrySet().stream().filter(entry -> entry.getKey().split("#")[1].equals(time))
   	.map(entry -> new SimpleEntry<> (entry.getKey().split("#")[0], entry.getValue())).sorted(Comparator.comparing(Entry::getValue))
   	.collect(Collectors.toList());
   };
}
