package com.jec.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
		return df.format(date);
	}

	public static Date String2Date(String date){
		try{
			return df.parse(date);
		}catch (ParseException e){
			return null;
		}
	}

	public static Long String2TimeStamp(String date){
		try{
			return Timestamp.valueOf(date).getTime();
		}catch(Exception e){
			return null;
		}
	}
}
