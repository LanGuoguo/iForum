package com.fate.restful.et.common.bean;

/**
 * 返回code常量定义
 * @author WangGang
 * 2016年06月06日11:44:15
 */
public class ResultCode {
	public static final int HTTP_SUCCESS = 1100;
	
	public static final int HTTP_SYSTEM_ERROR = 1111;
	
	public static final int HTTP_OUT_OF_PARAM = 1200;
	public static final int HTTP_INVALID_PARAM = 1201;
	
	public static final int HTTP_EXECUTE_DB_ERROR = 1300;
	
	public static final int HTTP_DB_NO_CONTENT = 1400;
	public static final int HTTP_DB_DUPLICATE = 1404;
	
	public static final int DEFAULT_PAGE_NUM = 1;
	public static final int DEFAULT_PAGE_SIZE = 10;
}
