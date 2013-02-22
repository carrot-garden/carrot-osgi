package com.carrotgarden.osgi.anno.scr.util;

public class Util {

	public static void assertZeroCount(int count, String message) {
		if (count == 0) {
			return;
		}
		throw new IllegalStateException(message);
	}

	public static boolean isValidText(final String text) {
		return text != null && text.length() > 0;
	}

	public static String unbindName(final String bindName) {
	
		if (bindName.startsWith("add")) {
			return bindName.replaceFirst("add", "remove");
		}
	
		return "un" + bindName;
	
	}

	private Util() {

	}

}
