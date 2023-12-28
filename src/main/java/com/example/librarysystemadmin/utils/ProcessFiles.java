package com.example.librarysystemadmin.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;


public class ProcessFiles {
    public static final Logger log = LoggerFactory.getLogger(ProcessFiles.class);

    static ApiResponse<String> apiResponse;

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

    public static Boolean verifyFileConfig(MultipartFile file) {
        // 参数验证
        if (file == null || file.isEmpty()) {
            apiResponse.setErrorResponse(400, "文件不能为空");
            return false;
        }
        //验证文件格式
        String type = file.getContentType();
        if (!type.matches("image/(jpg|png|gif|jpeg|webp)")) {
            apiResponse.setErrorResponse(400, "文件格式不正确");
            return false;
        }
        //验证文件大小
        if (file.getSize() > 1024 * 1024 * 2) {
            apiResponse.setErrorResponse(400, "文件大小不能超过2M");
            return false;
        }
        return true;
    }

    //检测文件夹是否存在 不存在则创建
    public static Boolean createDir(String uploadPath) {
        //创建文件夹
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("创建文件夹失败,请检查路径是否正确或者是否有权限");
                return false;
            }
        }
        return true;
    }

    //检索文件夹下是否有同名文件
    public static Boolean checkFileExist(String uploadPath, String fileName) {
        File[] files = new File(uploadPath).listFiles();
        for (File f : files) {
            if (f.getName().equals(fileName)) {
                return false;
            }
        }
        return true;
    }

    /*
     * 图片压缩
     * @param file 文件 File
     * @param size 文件大小 kb
     * @param ratio 压缩比例  0f-1f
     * @param uploadPath 上传路径
     * @return
     * */
    public static void imageReduce(File file, long size, float ratio, String uploadPath) {
        try {
            // kb
            long fileSize = file.length() / 1024;
            //判断文件大小是否超出
            if (fileSize > size) {
                //超出则进行压缩
                Thumbnails.of(file).scale(1f).outputQuality(ratio).toFile(uploadPath + file.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
