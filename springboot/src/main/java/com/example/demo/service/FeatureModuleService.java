package com.example.demo.service;

import com.example.demo.dto.FeatureDTO;
import com.example.demo.dto.FeatureConfigDTO;
import com.example.demo.entity.Application;
import com.example.demo.entity.DeploymentUnit;
import com.example.demo.entity.Feature;
import com.example.demo.entity.FeatureConfig;

import java.util.List;

public interface FeatureModuleService {
    
    // 应用相关
    List<Application> getAllApplications();
    Application getApplicationById(Long id);
    Application createApplication(Application application);
    Application updateApplication(Application application);
    void deleteApplication(Long id);
    
    // 部署单元相关
    List<DeploymentUnit> getAllDeploymentUnits();
    DeploymentUnit getDeploymentUnitById(Long id);
    DeploymentUnit createDeploymentUnit(DeploymentUnit deploymentUnit);
    DeploymentUnit updateDeploymentUnit(DeploymentUnit deploymentUnit);
    void deleteDeploymentUnit(Long id);
    
    // 特性相关
    List<Feature> getFeaturesByApplicationAndDeploymentUnit(String applicationCode, String deploymentUnitCode);
    Feature getFeatureById(Long id);
    Feature createFeature(Feature feature);
    Feature updateFeature(Feature feature);
    void deleteFeature(Long id);
    FeatureDTO getFeatureWithConfigs(Long featureId);
    
    // 配置相关
    List<FeatureConfig> getConfigsByFeatureId(Long featureId);
    FeatureConfig getConfigById(Long id);
    FeatureConfig createConfig(FeatureConfig featureConfig);
    FeatureConfig updateConfig(FeatureConfig featureConfig);
    void deleteConfig(Long id);
} 