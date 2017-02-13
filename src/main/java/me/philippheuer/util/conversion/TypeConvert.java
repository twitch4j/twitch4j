package me.philippheuer.util.conversion;

public class TypeConvert {
	// -- String Array Operations -- //
	/**
	 * Remove the first item from a String array
	 * @param arr String array to manipulate
	 * @return Array without first element
	 */
	public static String[] removeFirstArrayEntry(String[] arr) {
		String[] toReturn = new String[arr.length - 1];
		for(int i = 1; i < arr.length; i++) {
			toReturn[i - 1] = arr[i];
		}
		return toReturn;
	}

	/**
	 * Remove the first item from a String array
	 * @param arr String array to manipulate
	 * @return Array without first element
	 */
	public static String[] removeLastArrayEntry(String[] arr) {
		String[] toReturn = new String[arr.length - 1];
		for(int i = 0; i < arr.length - 1; i++) {
			toReturn[i] = arr[i];
		}
		return toReturn;
	}

	/**
	 * Combine a string array into a single String
	 * @param arr String array
	 * @param delimiter String delimiter
	 * @return String of combination
	 */
	public static String combineStringArray(String[] arr, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for(String s : arr) {
			sb.append(s + delimiter);
		}
		return sb.toString().trim();
	}
}
