package com.OrderMate.controller.admin;

import com.OrderMate.constant.MessageConstant;
import com.OrderMate.result.Result;
import com.OrderMate.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * ClassName: CommonController
 * Package: com.OrderMate.controller.admin
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-22 14:34
 */
/*
* 通用接口*/
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传",file);

        try {
            // 获取文件原始名
            String originalFilename = file.getOriginalFilename();
            // 获取原始文件名后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败:{}",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
