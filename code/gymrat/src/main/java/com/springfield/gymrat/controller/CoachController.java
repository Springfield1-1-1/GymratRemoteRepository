package com.springfield.gymrat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.config.OssConfig;
import com.springfield.gymrat.dto.CoachSaveDTO;
import com.springfield.gymrat.dto.CoachQueryDTO;
import com.springfield.gymrat.service.CoachService;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.CoachVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    private final OssConfig ossConfig;

    /**
     * 根据门店ID查询教练列表
     */
    @GetMapping
    public Result<PageResult<CoachVO>> getCoachesByStore(
            Page<CoachVO> page,
            @RequestParam(required = false) Long storeId) {
        // 构建查询条件
        CoachQueryDTO queryDTO = new CoachQueryDTO();
        queryDTO.setStoreId(storeId);

        PageResult<CoachVO> result = coachService.getCoachList(page, queryDTO);
        return Result.success(result);
    }

    /**
     * 管理员：保存教练信息（新增或更新）
     */
    @PostMapping("/save")
    public Result<Void> saveCoach(@RequestBody @Valid CoachSaveDTO dto) {
        boolean success = coachService.saveCoach(dto);
        if (success) {
            return Result.success("操作成功", null);
        } else {
            return Result.error("操作失败");
        }
    }

    /**
     * 管理员：删除教练
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCoach(@PathVariable Long id) {
        boolean success = coachService.deleteCoach(id);
        if (success) {
            return Result.success("删除成功", null);
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 管理员：分页查询教练列表
     */
    @GetMapping("/list")
    public Result<PageResult<CoachVO>> getCoachList(
            Page<CoachVO> page,
            CoachQueryDTO queryDTO) {
        PageResult<CoachVO> result = coachService.getCoachList(page, queryDTO);
        return Result.success(result);
    }

    /**
     * 上传教练头像
     */
    @PostMapping("/upload-avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        log.info("OSS 配置信息 - accessKeyId: {}***, endpoint: {}, bucketName: {}",
                ossConfig.getAccessKeyId() != null ? ossConfig.getAccessKeyId().substring(0, Math.min(8, ossConfig.getAccessKeyId().length())) : "null",
                ossConfig.getEndpoint(),
                ossConfig.getBucketName());

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";

            String fileName = System.currentTimeMillis() + "_" + fileExtension;
            String filePath = "教练头像/" + fileName;

            com.aliyun.oss.ClientBuilderConfiguration conf = new com.aliyun.oss.ClientBuilderConfiguration();
            conf.setMaxConnections(100);

            com.aliyun.oss.OSS ossClient = new com.aliyun.oss.OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret(),
                    conf);

            try {
                ossClient.putObject(ossConfig.getBucketName(), filePath, file.getInputStream());

                String url = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + filePath;

                log.info("教练头像上传成功，URL: {}", url);

                return Result.success(url);
            } finally {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            log.error("上传教练头像失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
