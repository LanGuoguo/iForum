package com.fate.restful.et.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author WangGang
 * 2016年06月06日15:56:40
 */
public class Exception2String {
	public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return e.toString();
        }
    }
}
