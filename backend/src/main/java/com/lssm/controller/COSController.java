package com.lssm.controller;

import com.lssm.utils.CosUtils;
import com.lssm.utils.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.lssm.common.contstant.CosConstants.ExpiredTime;


@Slf4j
@RequestMapping("/api/cos")
@RestController
public class COSController {

    @Resource
    private CosUtils cosUtils;

    @PostMapping("/upload")
    public Result<Map<String,String>> uploadFile(MultipartFile file) {
        log.info("文件上传：{}", file);
        try{
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;
            String filePath = cosUtils.uploadFile(file.getInputStream(), objectName);

            // 返回文件路径和与签名url
            Map<String, String> result = new HashMap<>();
            result.put("filepath",filePath);
            result.put("url",cosUtils.getPresignedUrl(filePath, ExpiredTime));
            return Result.success(result);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("上传失败");
        }

    }
    @PostMapping("/batch-presigned-urls")
    public Result<Map<String, String>> getBatchPresignedUrls(@RequestBody  String[] keys) {
        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            result.put(key, cosUtils.getPresignedUrl(key, 3600));
        }
        return Result.success(result);
    }
    @GetMapping("hi")
    public String hi(){
        return "hi";
    }
}
