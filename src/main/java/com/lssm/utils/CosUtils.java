package com.lssm.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class CosUtils {
    @Value("${tencent.cos.secret-id}")
    private String secretId;
    @Value("${tencent.cos.secret-key}")
    private String secretKey;
    @Value("${tencent.cos.region}")
    private String region;
    @Value("${tencent.cos.bucket}")
    private String bucket;
    @Value("${tencent.cos.prefix}")
    private String prefix;

    private COSClient getClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        return new COSClient(cred, clientConfig);
    }
    //上传文件
    public String uploadFile(InputStream Is, String fileName ) {
        COSClient cosClient = getClient();
        try{
            String key = prefix + fileName;
            PutObjectRequest request = new PutObjectRequest(bucket, key, Is, null);
            cosClient.putObject(request);
            return key;
        }finally{
            cosClient.shutdown();
        }
    }

    //生成临时访问链接（私有读时用）
    public String getPresignedUrl(String key, long expireSeconds) {
        COSClient cosClient = getClient();
        try{
            Date expiration = new Date(System.currentTimeMillis()+ expireSeconds *  1000L);
            GeneratePresignedUrlRequest request =
                    new GeneratePresignedUrlRequest(bucket, key);
            URL url = cosClient.generatePresignedUrl(request.withExpiration(expiration));
            return url.toString();
        }finally{
            cosClient.shutdown();
        }
    }
}
