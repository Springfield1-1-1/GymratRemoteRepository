package com.springfield.gymrat.controller;

import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.service.EquipmentCategoryService;
import com.springfield.gymrat.service.EquipmentService;
import com.springfield.gymrat.vo.CategoryVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.springfield.gymrat.dto.EquipmentSaveDTO;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import com.springfield.gymrat.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentCategoryService categoryService;

    @Autowired
    private OssConfig ossConfig;

    /**
     * 获取器械分类列表
     */
    @GetMapping("/categories")
    public Result<List<CategoryVO>> getCategories() {
        List<CategoryVO> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }

    /**
     * 分页查询器械列表
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "equipmentListCache", key = "'list:' + #categoryCode + ':' + #keyword + ':' + #page + ':' + #pageSize", condition = "#page == 1 and #pageSize == 10")
    public Result<PageResult<com.springfield.gymrat.vo.EquipmentVO>> getEquipmentList(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        EquipmentQueryDTO query = new EquipmentQueryDTO();
        query.setCategoryCode(categoryCode);
        query.setKeyword(keyword);
        query.setPage(page);
        query.setPageSize(pageSize);

        PageResult<com.springfield.gymrat.vo.EquipmentVO> result = equipmentService.getEquipmentPage(query);
        return Result.success(result);
    }

    /**
     * 根据分类ID统计器械数量
     */
    @GetMapping("/count")
    public Result<Long> countByCategoryId(@RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String categoryCode) {
        Long count;
        if (categoryId != null) {
            count = equipmentService.countByCategoryId(categoryId);
        } else if (categoryCode != null) {
            count = equipmentService.countByCategoryCode(categoryCode);
        } else {
            count = equipmentService.count();
        }
        return Result.success(count);
    }

    /**
     * 保存器械信息（新增或更新）
     */
    @PostMapping("/save")
    @CacheEvict(cacheNames = "equipmentListCache", allEntries = true)
    public Result<Void> saveEquipment(@RequestBody @Valid EquipmentSaveDTO dto) {
        boolean success = equipmentService.saveEquipment(dto);
        if (success) {
            return Result.success("保存成功", null);
        } else {
            return Result.error("操作失败");
        }
    }

    /**
     * 删除器械
     */
    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = "equipmentListCache", allEntries = true)
    public Result<Void> deleteEquipment(@PathVariable Integer id) {
        boolean success = equipmentService.deleteEquipment(id);
        if (success) {
            return Result.success("删除成功", null);
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 上传器械图片
     */
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
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
            String filePath = "器材图片/" + fileName;

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

                log.info("器械图片上传成功，URL: {}", url);

                return Result.success(url);
            } finally {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            log.error("上传器械图片失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
