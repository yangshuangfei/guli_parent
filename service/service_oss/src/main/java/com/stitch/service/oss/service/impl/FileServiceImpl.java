package com.stitch.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.stitch.service.oss.service.FileService;
import com.stitch.service.oss.util.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        //读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        if (!ossClient.doesBucketExist(bucketname)) {
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        //构建目录
        String folder = new DateTime().toString("yyyy/MM/dd");
        UUID uuid = UUID.randomUUID();//文件名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));//文件扩展名
        String key = module + "/" + folder + "/" + uuid + fileExtension;
        log.info(key);
        ossClient.putObject(bucketname, key, inputStream);
        ossClient.shutdown();

        //返回url
        return "https://" + bucketname + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        //读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        if (!ossClient.doesBucketExist(bucketname)) {
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        //删除文件
        String host = "https://"+ bucketname +"."+endpoint+"/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketname,objectName);

        //关闭OSSClient
        ossClient.shutdown();
    }
}
