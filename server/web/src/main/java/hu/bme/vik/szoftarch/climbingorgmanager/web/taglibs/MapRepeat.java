package hu.bme.vik.szoftarch.climbingorgmanager.web.taglibs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapRepeat {
	/**
	 * Converts a Map to a List filled with its entries. This is needed since
	 * very few if any JSF iteration components are able to iterate over a map.
	 */
	public static <T, S> List<Map.Entry<T, S>> mapToList(Map<T, S> map) {

		if (map == null) {
			return null;
		}

		List<Map.Entry<T, S>> list = new ArrayList<Map.Entry<T, S>>();
		list.addAll(map.entrySet());

		return list;
	}
}
