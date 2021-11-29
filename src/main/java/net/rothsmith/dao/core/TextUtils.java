/*
 * (c) 2014 Rothsmith, LLC, All Rights Reserved
 */
package net.rothsmith.dao.core;

import java.io.File;
import java.math.BigDecimal;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience operations for handling text.
 * 
 * @author drothauser
 * 
 */
public final class TextUtils {

	/**
	 * Logger for TextUtils.
	 */
	private static final Logger LOGGER =
	    LoggerFactory.getLogger(TextUtils.class);

	/**
	 * This method converts a string to camel case.
	 * 
	 * @param string
	 *            String to convert to camel case.
	 * @return The string converted to camel case
	 */
	public static String convertToCamelCase(final String string) {

		if (StringUtils.isEmpty(string)) {
			throw new IllegalArgumentException(
			    "Input string should not be null or empty");
		}

		String temp =
		    string.replaceAll("[^a-zA-Z0-9_\\-\\s]+", "").trim().toLowerCase();
		if (StringUtils.isEmpty(temp)) {
			throw new IllegalArgumentException(
			    "Input string should contain alphanumeric characters");
		}

		String[] tokens = StringUtils.split(temp, "_ -");
		for (int i = 0; i < tokens.length; i++) {
			if (i != 0) {
				tokens[i] = StringUtils.capitalize(tokens[i]);
			}
		}

		String camelCase = StringUtils.join(tokens);

		return camelCase;

	}

	/**
	 * This method transforms a string to a format suitable for Java constants
	 * i.e. all uppercase with underscore delimiters.
	 * 
	 * @param string
	 *            String to convert
	 * @return a string suitable for Java constants
	 */
	public static String convertToConstant(final String string) {

		if (StringUtils.isEmpty(string)) {
			throw new IllegalArgumentException(
			    "Input string should not be null or empty");
		}

		String temp = string.replaceAll("[^a-zA-Z_\\-\\s]+", "").trim();
		if (StringUtils.isEmpty(temp)) {
			throw new IllegalArgumentException(
			    "Input string should contain alphanumeric characters");
		}

		temp = temp.replaceAll("([A-Z_\\-\\s])(?=[a-z])", "_$1");

		String[] tokens = StringUtils.split(temp, "_ -");
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = StringUtils.upperCase(tokens[i]);
		}

		String constant = StringUtils.join(tokens, "_");

		return constant;

	}

	/**
	 * Convert a string to proper case. For example:<br>
	 * &quot;THE TEST STRING&quot; becomes &quot;The Test String&quot;.
	 * <p>
	 * If the name parameter is null and empty String (&apos;&apos;) is
	 * returned. Also, leading and trailing single and double quotes are
	 * removed.
	 * </p>
	 * 
	 * @param string
	 *            the string to convert
	 * @return the string converted to proper case.
	 */
	public static String properCase(final String string) {

		return WordUtils.capitalize(StringUtils.defaultIfEmpty(
		    StringUtils.strip(StringUtils.lowerCase(string), "\"'"), ""));
	}

	/**
	 * This method checks if a string is Base64 encoded.
	 * 
	 * @param string
	 *            the string to check
	 * 
	 * @return true if the string is Base64 encoded, else false
	 */
	public static boolean isBase64(final String string) {

		// CHECKSTYLE:OFF Magic number 4 ok here
		return StringUtils.lowerCase(StringUtils.defaultString(string)).matches(
		    "^[a-z0-9\\+\\/\\s]+\\={1,2}$") && string.length() % 4 == 0;

	}

	/**
	 * This method replaces the line feeds and tabs in a text string with a
	 * blank. If the text field is null, and empty string is returned.
	 * 
	 * @param text
	 *            the text to remove the line feeds from
	 * @return the text devoid of line feeds
	 */
	public static String removeNewlines(final String text) {
		return StringUtils.defaultString(text).replaceAll("[\n\r\t]", " ");
	}

	/**
	 * This method parses a number string and returns it in a BigDecimal object.
	 * 
	 * @param numberString
	 *            the number string to parse
	 * @return a {@link BigDecimal} object. If the number cannot be parsed null
	 *         is returned.
	 */
	public static BigDecimal parseNumberString(final String numberString) {

		String numStr =
		    StringUtils.trim(StringUtils.defaultString(numberString))
		        .replaceAll("[$\\,]+", "");

		BigDecimal bigDecimal = null;

		try {
			bigDecimal = new BigDecimal(numStr);
		} catch (NumberFormatException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
				    String.format("'%s' cannot be parsed: %s", numStr, e));
			}
		}

		return bigDecimal;
	}

	/**
	 * Private constructor to thwart instantiation.
	 */
	private TextUtils() {

	}

	/**
	 * This method builds a fully qualified Java class file name.
	 * 
	 * @param baseDir
	 *            base directory e.g. 'src'
	 * @param packageName
	 *            Java package
	 * @param className
	 *            Java class name
	 * @return the fully qualified Java class file name.
	 */
	public static String buildFileName(String baseDir, String packageName,
	    String className) {

		// Build fully qualified output name
		String outputDir = baseDir + File.separatorChar
		    + StringUtils.replaceChars(packageName, '.', File.separatorChar);

		// If the package directory does not exist, create it
		if (!new File(outputDir).exists()) {
			boolean ok = new File(outputDir).mkdirs();
			if (!ok) {
				throw new IllegalArgumentException(String
				    .format("Unable to create directory - '%s'", outputDir));
			}
		}

		return FilenameUtils
		    .normalize(outputDir + File.separatorChar + className + ".java");
	}

}
