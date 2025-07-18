package com.example.demo.controller;

import com.example.demo.dto.FeatureDTO;
import com.example.demo.entity.Application;
import com.example.demo.entity.DeploymentUnit;
import com.example.demo.entity.Feature;
import com.example.demo.entity.FeatureConfig;
import com.example.demo.service.FeatureModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feature-module")
@CrossOrigin(origins = "*")
public class FeatureModuleController {
    
    @Autowired
    private FeatureModuleService featureModuleService;
    
    // 应用相关接口
    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = featureModuleService.getAllApplications();
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application application = featureModuleService.getApplicationById(id);
        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(application);
    }
    
    @PostMapping("/applications")
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        Application created = featureModuleService.createApplication(application);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/applications/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application application) {
        application.setId(id);
        Application updated = featureModuleService.updateApplication(application);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        featureModuleService.deleteApplication(id);
        return ResponseEntity.ok().build();
    }
    
    // 部署单元相关接口
    @GetMapping("/deployment-units")
    public ResponseEntity<List<DeploymentUnit>> getAllDeploymentUnits() {
        List<DeploymentUnit> deploymentUnits = featureModuleService.getAllDeploymentUnits();
        return ResponseEntity.ok(deploymentUnits);
    }
    
    @GetMapping("/deployment-units/{id}")
    public ResponseEntity<DeploymentUnit> getDeploymentUnitById(@PathVariable Long id) {
        DeploymentUnit deploymentUnit = featureModuleService.getDeploymentUnitById(id);
        if (deploymentUnit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deploymentUnit);
    }
    
    @PostMapping("/deployment-units")
    public ResponseEntity<DeploymentUnit> createDeploymentUnit(@RequestBody DeploymentUnit deploymentUnit) {
        DeploymentUnit created = featureModuleService.createDeploymentUnit(deploymentUnit);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/deployment-units/{id}")
    public ResponseEntity<DeploymentUnit> updateDeploymentUnit(@PathVariable Long id, @RequestBody DeploymentUnit deploymentUnit) {
        deploymentUnit.setId(id);
        DeploymentUnit updated = featureModuleService.updateDeploymentUnit(deploymentUnit);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/deployment-units/{id}")
    public ResponseEntity<Void> deleteDeploymentUnit(@PathVariable Long id) {
        featureModuleService.deleteDeploymentUnit(id);
        return ResponseEntity.ok().build();
    }
    
    // 特性相关接口
    @GetMapping("/features")
    public ResponseEntity<List<Feature>> getFeaturesByApplicationAndDeploymentUnit(
            @RequestParam(required = false) String applicationCode,
            @RequestParam(required = false) String deploymentUnitCode) {
        List<Feature> features = featureModuleService.getFeaturesByApplicationAndDeploymentUnit(applicationCode, deploymentUnitCode);
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/features/{id}")
    public ResponseEntity<FeatureDTO> getFeatureById(@PathVariable Long id) {
        FeatureDTO feature = featureModuleService.getFeatureWithConfigs(id);
        if (feature == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(feature);
    }
    
    @PostMapping("/features")
    public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) {
        Feature created = featureModuleService.createFeature(feature);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/features/{id}")
    public ResponseEntity<Feature> updateFeature(@PathVariable Long id, @RequestBody Feature feature) {
        feature.setId(id);
        Feature updated = featureModuleService.updateFeature(feature);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureModuleService.deleteFeature(id);
        return ResponseEntity.ok().build();
    }
    
    // 配置相关接口
    @GetMapping("/features/{featureId}/configs")
    public ResponseEntity<List<FeatureConfig>> getConfigsByFeatureId(@PathVariable Long featureId) {
        List<FeatureConfig> configs = featureModuleService.getConfigsByFeatureId(featureId);
        return ResponseEntity.ok(configs);
    }
    
    @GetMapping("/configs/{id}")
    public ResponseEntity<FeatureConfig> getConfigById(@PathVariable Long id) {
        FeatureConfig config = featureModuleService.getConfigById(id);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }
    
    @PostMapping("/configs")
    public ResponseEntity<FeatureConfig> createConfig(@RequestBody FeatureConfig featureConfig) {
        FeatureConfig created = featureModuleService.createConfig(featureConfig);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/configs/{id}")
    public ResponseEntity<FeatureConfig> updateConfig(@PathVariable Long id, @RequestBody FeatureConfig featureConfig) {
        featureConfig.setId(id);
        FeatureConfig updated = featureModuleService.updateConfig(featureConfig);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        featureModuleService.deleteConfig(id);
        return ResponseEntity.ok().build();
    }
    
    // 初始化数据接口
    @PostMapping("/init-data")
    public ResponseEntity<Map<String, String>> initData() {
        try {
            // 初始化应用数据
            Application app1 = new Application();
            app1.setName("应用A");
            app1.setCode("app1");
            app1.setDescription("第一个应用");
            featureModuleService.createApplication(app1);
            
            Application app2 = new Application();
            app2.setName("应用B");
            app2.setCode("app2");
            app2.setDescription("第二个应用");
            featureModuleService.createApplication(app2);
            
            Application app3 = new Application();
            app3.setName("应用C");
            app3.setCode("app3");
            app3.setDescription("第三个应用");
            featureModuleService.createApplication(app3);
            
            Application app4 = new Application();
            app4.setName("应用D");
            app4.setCode("app4");
            app4.setDescription("第四个应用");
            featureModuleService.createApplication(app4);
            
            // 初始化部署单元数据
            DeploymentUnit unit1 = new DeploymentUnit();
            unit1.setName("部署单元1");
            unit1.setCode("unit1");
            unit1.setDescription("第一个部署单元");
            featureModuleService.createDeploymentUnit(unit1);
            
            DeploymentUnit unit2 = new DeploymentUnit();
            unit2.setName("部署单元2");
            unit2.setCode("unit2");
            unit2.setDescription("第二个部署单元");
            featureModuleService.createDeploymentUnit(unit2);
            
            DeploymentUnit unit3 = new DeploymentUnit();
            unit3.setName("部署单元3");
            unit3.setCode("unit3");
            unit3.setDescription("第三个部署单元");
            featureModuleService.createDeploymentUnit(unit3);
            
            DeploymentUnit unit4 = new DeploymentUnit();
            unit4.setName("部署单元4");
            unit4.setCode("unit4");
            unit4.setDescription("第四个部署单元");
            featureModuleService.createDeploymentUnit(unit4);
            
            // 初始化特性数据
            Feature feature1 = new Feature();
            feature1.setName("用户管理");
            feature1.setCode("USER_MANAGEMENT");
            feature1.setDescription("用户信息的增删改查功能");
            feature1.setApplicationCode("app1");
            feature1.setDeploymentUnitCode("unit1");
            featureModuleService.createFeature(feature1);
            
            Feature feature2 = new Feature();
            feature2.setName("权限控制");
            feature2.setCode("PERMISSION_CONTROL");
            feature2.setDescription("系统权限管理功能");
            feature2.setApplicationCode("app1");
            feature2.setDeploymentUnitCode("unit1");
            featureModuleService.createFeature(feature2);
            
            Feature feature3 = new Feature();
            feature3.setName("数据统计");
            feature3.setCode("DATA_STATISTICS");
            feature3.setDescription("数据统计和分析功能");
            feature3.setApplicationCode("app2");
            feature3.setDeploymentUnitCode("unit2");
            featureModuleService.createFeature(feature3);
            
            // 初始化配置数据
            FeatureConfig config1 = new FeatureConfig();
            config1.setFeatureId(1L);
            config1.setKey("user.max_count");
            config1.setValue("1000");
            config1.setDescription("最大用户数量");
            featureModuleService.createConfig(config1);
            
            FeatureConfig config2 = new FeatureConfig();
            config2.setFeatureId(1L);
            config2.setKey("user.default_role");
            config2.setValue("user");
            config2.setDescription("默认用户角色");
            featureModuleService.createConfig(config2);
            
            FeatureConfig config3 = new FeatureConfig();
            config3.setFeatureId(2L);
            config3.setKey("permission.enabled");
            config3.setValue("true");
            config3.setDescription("是否启用权限控制");
            featureModuleService.createConfig(config3);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "数据初始化成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "数据初始化失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 