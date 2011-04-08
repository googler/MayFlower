package com.paladin.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.paladin.sys.db.QueryHelper;

public class RunDisk2DB {

	private static List<String[]> dataList2Write = new ArrayList<String[]>();
	private static String sql = "INSERT INTO HFILE(FILENAME, FILETYPE, FILEPATH) VALUES (?, ?, ?)";

	/**
	 * 建立索引
	 *
	 * @throws IOException
	 */
	public static void index() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("建立索引是很费时的操作哦！你确定要建立索引吗？（Y/N）");
		String input = scanner.nextLine();
		if ("Y".equalsIgnoreCase(input)) {
			File[] roots = File.listRoots();
			for (File file : roots)
				index(file);
			// 清除残余在list中的文件记录
			writeRec();
		} else {
			System.out.println("输错啦！不给力！");
		}
	}

	public static void main(String[] args) {
		index();
	}

	private static void index(File dir) {
		if (dir.listFiles() != null) {
			for (File f : dir.listFiles()) {
				StringBuilder data_line = new StringBuilder();
				String file_name = f.getName();
				data_line.append(file_name);
				String file_path = f.getPath();
				if (f.isFile()) {
					int index_of_dot = file_name.lastIndexOf('.');
					String file_type = "";
					if (index_of_dot > 0)
						file_type = file_name.substring(index_of_dot + 1, file_name.length()).toLowerCase();
					else
						file_type = "Any";
					if (!Constant.includeFileType.contains(file_type)) {
						data_line.setLength(0);
						continue;
					}
					writeRec(new String[] { file_name, file_type, file_path });
				} else {
					writeRec(new String[] { file_name, "DIR", file_path });
					index(f);
				}
				data_line.setLength(0);
			}
		}
	}

	private static void writeRec(String[] _par) {
		dataList2Write.add(_par);
		if (dataList2Write.size() > Constant.lines2write) {
			String[][] params = new String[dataList2Write.size()][];
			dataList2Write.toArray(params);
			QueryHelper.batch(sql, params);
			dataList2Write.clear();
		}
	}

	private static void writeRec() {
		if (dataList2Write.size() != 0) {
			String[][] params = new String[dataList2Write.size()][];
			dataList2Write.toArray(params);
			QueryHelper.batch(sql, params);
			dataList2Write.clear();
		}
	}
}

class Constant {
	public static int lines2write = 1000;// 每1,000条记录写一次文件
	public static String split = ":";
	public static List<String> includeFileType = new ArrayList<String>();
	// 不检索的文件类型
	static {
		String[] arrIncludeFileType = new String[] { "wav", "zip", "rar", "perl", "scala", "py", "psd", "python",
				"pdf", "mp3", "mpg", "mpeg", "license", "jsp", "js", "jpeg", "html", "htm", "css", "cmd", "csv", "txt",
				"ppt", "bat", "DIR", "bak", "jar", "exe", "doc", "xls", "png", "jpg", "gif", "ini", "dat", "bmp",
				"xml", "java", "csv", "log", "wps", "wsdl", "theme", "sql", "vm", "avi", "docx", "pptx", "tld",
				"properties", "xlsx", "wmv", "wma", "war", "tar", "gz", "asm", "rm", "rmvb", "srt", "settings",
				"thrift" };
		includeFileType = Arrays.asList(arrIncludeFileType);
	}
}