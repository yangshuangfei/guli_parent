package com.stitch.service.oss.service;

import java.io.InputStream;

public interface FileService {
    /**
     * 文件上传
     * @param inputStream 输入流
     * @param module 模式
     * @param originalFilename 原始文件名
     * @return 文件的url地址
     */
    String upload(InputStream inputStream,String module,String originalFilename);

    void removeFile(String url);
}
