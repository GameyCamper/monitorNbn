/**
 * 
 */
package me.ineson.monitor_nbn.shared;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Set of helpful utilities.
 * 
 * @author Peter
 *
 */
public final class GeneralUtils {

	/**
	 * @param strings
	 * @param prefix
	 * @return
	 */
	public static boolean hasStringStartingWithPrefix(List<String> strings, String prefix) {
		return Objects.nonNull(getStringStartingWithPrefix(strings, prefix));
	}

	/**
	 * @param strings
	 * @param preFix
	 * @return
	 */
	public static String getStringStartingWithPrefix(List<String> strings, String preFix) {
		if( StringUtils.isBlank(preFix) || CollectionUtils.isEmpty(strings)) {
			return null;
		}

		return strings.stream().filter(line -> line != null && line.startsWith(preFix)).findAny().orElse(null); 
	}
	
	/**
	 * @param searchString
	 * @param strings
	 * @return
	 */
	public static boolean hasStringContainsIgnoreCase(List<String> strings, String searchString) {
		if (StringUtils.isBlank(searchString) || CollectionUtils.isEmpty(strings)) {
			return false;
		}

		return strings.stream()
				.filter(line -> line != null && Strings.CI.indexOf(line, searchString) != StringUtils.INDEX_NOT_FOUND)
				.findAny()
				.isPresent();
	}

}
