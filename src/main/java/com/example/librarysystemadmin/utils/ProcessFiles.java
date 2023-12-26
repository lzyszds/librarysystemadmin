package com.example.librarysystemadmin.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;


public class ProcessFiles {
    public static String getFileMd5(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();

            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为32进制
            return new BigInteger(1, digest).toString(32);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /*
     *  图片压缩
     *  @param file 图片文件
     *  @param size 超出多少kb进行压缩 单位kb
     *  @return
     * */
    public static void imageReduce(File file, long size, String uploadPath) {
        try {
            // kb
            long fileSize = file.length() / 1024;
            //判断文件大小是否超出
            if (fileSize > size) {
                //超出则进行压缩
                Thumbnails.of(file).scale(1f).outputQuality(0.25f).toFile(uploadPath + file.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
