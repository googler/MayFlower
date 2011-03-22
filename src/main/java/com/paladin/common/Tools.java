package com.paladin.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tools {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullString(String str) {
		if (str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("NULL"))
			return true;
		else
			return false;
	}

	public static String null2String(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 判断两个字符串是否为空,其实可以用不定参数函数代替
	 * 
	 * @param str
	 * @param str2
	 * @return
	 */
	public static boolean isNullString(String str, String str2) {
		return isNullString(str) && isNullString(str2);
	}

	/**
	 * 压缩字符串中的空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String compressBlank(String str) {
		str = str.trim();
		if (Tools.isNullString(str))
			return "";
		StringBuilder str_bu = new StringBuilder();
		char[] str_arr = str.toCharArray();
		for (int i = 0; i < str_arr.length; i++) {
			if (!isBlank(str_arr[i]))
				str_bu.append(str_arr[i]);
			else if (isBlank(str_arr[i]) && i + 1 < str_arr.length && !isBlank(str_arr[i + 1]))
				str_bu.append((char) 32);
		}
		return str_bu.toString();
	}

	/**
	 * 判断某字符是否是空白字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isBlank(char c) {
		return (int) c == 9 || (int) c == 32;
	}

	/** check tag */
	public static String checkTag(String _tags) {
		// 替换全角逗号分隔符
		_tags = _tags.replace("，", ",");
		// 删除重复的tag
		List<String> tag_list = new ArrayList<String>();
		for (String tag : _tags.split(",")) {
			String _tag = Tools.compressBlank(tag);// 压缩标签中的空格
			if (!Tools.isNullString(_tag) && !tag_list.contains(_tag))
				tag_list.add(_tag);
		}
		String tag = Arrays.toString((Object[]) tag_list.toArray());
		return tag.substring(1, tag.length() - 1);
	}
	
	public static void main(String[] args) {

	}
}
