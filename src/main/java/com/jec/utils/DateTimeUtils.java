package com.jec.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateTimeUtils {

	public DateTimeUtils() {
		// TODO Auto-generated constructor stub
	}

	public static java.util.Date SqlDate2UtilDate(java.sql.Date sqlDate,
			java.util.Date utilDate) {
		return null;
	}

	public static java.sql.Date UtilDate2SqlDate(java.sql.Date sqlDate,
			java.util.Date utilDate) {
		return null;
	}

	public static String Date2String(java.util.Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
}
