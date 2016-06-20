package com.jec.utils.tool;

import java.util.regex.Pattern;

/**
 * Created by jeremyliu on 5/11/16.
 */
public class StringHelper {

    public static boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }
}
