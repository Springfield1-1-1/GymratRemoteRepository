package com.springfield.gymrat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.common.context.UserContext;
import com.springfield.gymrat.dto.GymStoreSaveDTO;
import com.springfield.gymrat.dto.StoreQueryDTO;
import com.springfield.gymrat.service.GymStoreService;
import com.springfield.gymrat.vo.GymStoreVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import com.springfield.gymrat.config.OssConfig;

@Slf4j
@RestController
@RequestMapping("/api/gym-stores")
@CrossOrigin(origins = "*")
public class GymStoreController {

    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private GymStoreService gymStoreService;

    // 获取门店列表
    @GetMapping
    @Cacheable(cacheNames = "gymStoresCache", key = "'city:' + (#city != null ? #city : 'all')")
    public Result<List<GymStoreVO>> getStoresByCity(@RequestParam(required = false) String city) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        List<GymStoreVO> stores = gymStoreService.getStoresByCity(city);
        return Result.success("获取成功", stores);
    }

    // 获取门店详情
    @GetMapping("/{storeId}")
    @Cacheable(cacheNames = "gymStoreDetailCache", key = "#storeId")
    public Result<GymStoreVO> getStoreDetail(@PathVariable Long storeId) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        GymStoreVO store = gymStoreService.getStoreDetail(storeId);
        if (store == null) {
            return Result.error("门店不存在");
        }
        return Result.success("获取成功", store);
    }

    /**
     * 管理员：保存门店信息（新增或更新）
     */
    @PostMapping("/save")
    @CacheEvict(cacheNames = {"gymStoresCache", "gymStoreDetailCache", "gymStoreListCache"}, allEntries = true)
    public Result<Void> saveStore(@RequestBody @Valid GymStoreSaveDTO dto) {
        boolean success = gymStoreService.saveStore(dto);
        if (success) {
            return Result.success("操作成功", null);
        } else {
            return Result.error("操作失败");
        }
    }

    /**
     * 管理员：删除门店
     */
    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = {"gymStoresCache", "gymStoreDetailCache", "gymStoreListCache"}, allEntries = true)
    public Result<Void> deleteStore(@PathVariable Long id) {
        boolean success = gymStoreService.deleteStore(id);
        if (success) {
            return Result.success("删除成功", null);
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 管理员：分页查询门店列表
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "gymStoreListCache", key = "#root.methodName + ':' + #page.current + ':' + #page.size + ':' + (#queryDTO.city != null ? #queryDTO.city : '') + ':' + (#queryDTO.keyword != null ? #queryDTO.keyword : '')", condition = "#page.current == 1 and #page.size == 10")
    public Result<PageResult<GymStoreVO>> getStoreList(
            Page<GymStoreVO> page,
            StoreQueryDTO queryDTO) {
        PageResult<GymStoreVO> result = gymStoreService.getStoreList(page, queryDTO);

        log.info("=== [DEBUG] 查询结果：总数={}, 记录数={}", result.getTotal(), result.getRecords().size());
        if (!result.getRecords().isEmpty()) {
            GymStoreVO firstStore = result.getRecords().get(0);
            log.info("=== [DEBUG] 第一条记录：id={}, storeName={}, status={}",
                    firstStore.getId(),
                    firstStore.getStoreName(),
                    firstStore.getStatus());
        }

        return Result.success(result);
    }

    /**
     * 上传门店图片
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
            String filePath = "门店图片/" + fileName;

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

                log.info("门店图片上传成功，URL: {}", url);

                return Result.success(url);
            } finally {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            log.error("上传门店图片失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
