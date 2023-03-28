package com.lyjava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    /**
     * 根据原始文件名生成一个新的文件名（包括：年月日作为文件目录，uuid作为文件名）
     * @param fileName 原始文件名
     * @return
     */
    public static String generateFilePath(String fileName){
        //根据日期生成路径   2022/1/15/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}