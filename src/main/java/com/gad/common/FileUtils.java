/*
 * @(#) FileUtils.java ,2017年06月09日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * Created by caojianlong(caojianlong@zbj.com) on 2017/6/9.
 */
public class FileUtils {

    /**
     * 创建文件
     *
     * @param zipPath
     * @return
     */
    public static File createFile(String zipPath) throws IOException {
        File zip = new File(zipPath);
        if (!zip.getParentFile().exists()) {
            zip.getParentFile().mkdirs();
        }
        zip.deleteOnExit();
        zip.createNewFile();
        return zip;
    }


    /**
     * 将某个目录内的文件全部压缩成为zip
     *
     * @param docPath
     * @param zip
     */
    public static void doZip(String docPath, File zip) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        try {
            doCompress(docPath, out);
        } finally {
            out.flush();
            out.closeEntry();
            out.close();
        }
    }


    /**
     * 下载文件
     *
     * @param urlStr   文件下载地址
     * @param fileName 保存的文件名(需包含扩展名)
     * @param savePath 保存的目录
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();

        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 压缩
     *
     * @param pathname
     * @param out
     * @throws IOException
     */
    public static void doCompress(String pathname, ZipOutputStream out) throws IOException {
        File dir = new File(pathname);

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                doCompress(file.getPath(), out);
            }
        } else {
            doCompress(dir, out);
        }

    }

    /**
     * 压缩单个文件
     *
     * @param file
     * @param out
     * @throws IOException
     */
    public static void doCompress(File file, ZipOutputStream out) throws IOException {
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(file);
            String name = file.getName();
            name = URLDecoder.decode(name, "UTF-8");
            out.putNextEntry(new ZipEntry(name));
            int len = 0;
            // 读取文件的内容,打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
            fis.close();
        }
    }

    /**
     * 读取源码文件
     * @param path
     * @return
     */
    public static String readFile(String path) {
        StringBuilder builder = new StringBuilder();
        try {
            Scanner in = new Scanner(new File(path));

            while (in.hasNextLine()) {
                builder.append(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}
