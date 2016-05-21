package com.capitalone.dashboard.util;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ClientUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientUtil.class);

	/**
	 * Utility method used to sanitize / canonicalize a String-based response
	 * artifact from a source system. This will return a valid UTF-8 strings, or
	 * a "" (blank) response for any of the following cases:
	 * "NULL";"Null";"null";null;""
	 *
	 * @param nativeRs
	 *            The string response artifact retrieved from the source system
	 *            to be sanitized
	 * @return A UTF-8 sanitized response
	 */
	public static String sanitizeResponse(String nativeRs) {
		if (StringUtils.isEmpty(nativeRs)) {
			return "";
		}

		byte[] utf8Bytes;
		CharsetDecoder cs = StandardCharsets.UTF_8.newDecoder();

		if ("null".equalsIgnoreCase(nativeRs))
			return "";
		utf8Bytes = nativeRs.getBytes(StandardCharsets.UTF_8);
		try {
			cs.decode(ByteBuffer.wrap(utf8Bytes));
			return new String(utf8Bytes, StandardCharsets.UTF_8);
		} catch (CharacterCodingException e) {
			return "[INVALID NON UTF-8 ENCODING]";
		}
	}

	public static String toCanonicalDate(long nativeRs) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date(nativeRs));
	}

	/**
	 * Canonicalizes a given JSONArray to a basic List object to avoid the use
	 * of JSON parsers.
	 *
	 * @param list
	 *            A given JSONArray object response from the source system
	 * @return The sanitized, canonical List<String>
	 */
	public static List<String> toCanonicalList(List<String> list) {
		return list.stream().map(ClientUtil::sanitizeResponse).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param nativeRs
	 * @return
	 */
	public static String sprintDateCanonicalDate(String sprintDate) {
		if (org.apache.commons.lang3.StringUtils.isBlank(sprintDate)) {
			return null;
		}
		// 06 Aug 15
		Date parsed;
		try {
			DateFormat dateFormat1 = new SimpleDateFormat("dd MMM yy");
			dateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
			parsed = dateFormat1.parse(sprintDate);
		} catch (ParseException e) {
			// try next format i.e. "Jan 07", appending current year to it
			String sprintDateDifFormat = sprintDate + " " + Calendar.getInstance().get(Calendar.YEAR);
			try {
				DateFormat dateFormat2 = new SimpleDateFormat("MMM dd yyyy");
				dateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
				parsed = dateFormat2.parse(sprintDateDifFormat);
			} catch (ParseException e1) {
				LOGGER.error("Could not parse date {1} with either formats", sprintDateDifFormat);
				return null;
			}
		}
		return toCanonicalDate(parsed.getTime());
	}

}
