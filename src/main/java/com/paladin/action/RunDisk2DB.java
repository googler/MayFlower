package com.paladin.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.paladin.sys.db.QueryHelper;

public class RunDisk2DB {

    private static List<String[]> dataList = new ArrayList<String[]>();
    private static String sql = "INSERT INTO HFILE(FILENAME, FILETYPE, FILEPATH) VALUES (?, ?, ?)";

    /**
     * 建立索引
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
            System.out.println("出错啦！不给力！");
        }
    }

    /**
     * 遍历文件系统，将数据插入DB
     *
     * @param dir Directory
     */
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
                    writeDataDB(new String[]{file_name, file_type, file_path});
                } else {
                    writeDataDB(new String[]{file_name, "DIR", file_path});
                    index(f);
                }
                data_line.setLength(0);
            }
        }
    }

    /**
     * 将数据定入DB,一次1000条
     *
     * @param _par DATA={filename+filetype+path}
     */
    private static void writeDataDB(String[] _par) {
        dataList.add(_par);
        if (dataList.size() > Constant.lines2write) {
            String[][] params = new String[dataList.size()][];
            dataList.toArray(params);
            QueryHelper.batch(sql, params);
            dataList.clear();
        }
    }

    private static void writeRec() {
        if (dataList.size() != 0) {
            String[][] params = new String[dataList.size()][];
            dataList.toArray(params);
            QueryHelper.batch(sql, params);
            dataList.clear();
        }
    }

    /**
     * 方法入口
     *
     * @param args nothing:-)
     */
    public static void main(String[] args) {
        index();
    }
}

class Constant {
    public static int lines2write = 1000;// 每1,000条记录写一次文件
    public static String split = ":";
    public static List<String> includeFileType = new ArrayList<String>();

    // 要检索的文件类型
    static {
        String[] arrIncludeFileType = new String[]{"wav", "zip", "rar", "perl", "scala", "py", "psd", "c", "cpp",
                "python", "pdf", "mp3", "mpg", "mpeg", "license", "jsp", "js", "jpeg", "html", "htm", "jpg",
                "css", "cmd", "csv", "txt", "ppt", "bat", "DIR", "bak", "jar", "exe", "doc", "xls", "png", "gif",
                "xml", "java", "csv", "log", "wps", "wsdl", "theme", "sql", "vm", "avi", "docx", "pptx", "tld",
                "properties", "xlsx", "wmv", "wma", "war", "tar", "gz", "asm", "rm", "rmvb", "srt", "settings",
                "ini", "dat", "bmp", "thrift"};
        includeFileType = Arrays.asList(arrIncludeFileType);
    }
}