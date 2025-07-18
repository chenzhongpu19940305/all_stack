package com.example.demo.service.impl;

import com.example.demo.dto.FeatureConfigDTO;
import com.example.demo.dto.FeatureDTO;
import com.example.demo.entity.Application;
import com.example.demo.entity.DeploymentUnit;
import com.example.demo.entity.Feature;
import com.example.demo.entity.FeatureConfig;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.DeploymentUnitMapper;
import com.example.demo.mapper.FeatureConfigMapper;
import com.example.demo.mapper.FeatureMapper;
import com.example.demo.service.FeatureModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeatureModuleServiceImpl implements FeatureModuleService {
    
    @Autowired
    private ApplicationMapper applicationMapper;
    
    @Autowired
    private DeploymentUnitMapper deploymentUnitMapper;
    
    @Autowired
    private FeatureMapper featureMapper;
    
    @Autowired
    private FeatureConfigMapper featureConfigMapper;
    
    // 应用相关方法
    @Override
    public List<Application> getAllApplications() {
        return applicationMapper.findAllEnabled();
    }
    
    @Override
    public Application getApplicationById(Long id) {
        return applicationMapper.findById(id);
    }
    
    @Override
    public Application createApplication(Application application) {
        application.onCreate();
        applicationMapper.insert(application);
        return application;
    }
    
    @Override
    public Application updateApplication(Application application) {
        application.onUpdate();
        applicationMapper.update(application);
        return application;
    }
    
    @Override
    public void deleteApplication(Long id) {
        applicationMapper.deleteById(id);
    }
    
    // 部署单元相关方法
    @Override
    public List<DeploymentUnit> getAllDeploymentUnits() {
        return deploymentUnitMapper.findAllEnabled();
    }
    
    @Override
    public DeploymentUnit getDeploymentUnitById(Long id) {
        return deploymentUnitMapper.findById(id);
    }
    
    @Override
    public DeploymentUnit createDeploymentUnit(DeploymentUnit deploymentUnit) {
        deploymentUnit.onCreate();
        deploymentUnitMapper.insert(deploymentUnit);
        return deploymentUnit;
    }
    
    @Override
    public DeploymentUnit updateDeploymentUnit(DeploymentUnit deploymentUnit) {
        deploymentUnit.onUpdate();
        deploymentUnitMapper.update(deploymentUnit);
        return deploymentUnit;
    }
    
    @Override
    public void deleteDeploymentUnit(Long id) {
        deploymentUnitMapper.deleteById(id);
    }
    
    // 特性相关方法
    @Override
    public List<Feature> getFeaturesByApplicationAndDeploymentUnit(String applicationCode, String deploymentUnitCode) {
        return featureMapper.findByApplicationAndDeploymentUnit(applicationCode, deploymentUnitCode);
    }
    
    @Override
    public Feature getFeatureById(Long id) {
        return featureMapper.findById(id);
    }
    
    @Override
    public Feature createFeature(Feature feature) {
        feature.onCreate();
        featureMapper.insert(feature);
        return feature;
    }
    
    @Override
    public Feature updateFeature(Feature feature) {
        feature.onUpdate();
        featureMapper.update(feature);
        return feature;
    }
    
    @Override
    public void deleteFeature(Long id) {
        featureMapper.deleteById(id);
    }
    
    @Override
    public FeatureDTO getFeatureWithConfigs(Long featureId) {
        Feature feature = featureMapper.findById(featureId);
        if (feature == null) {
            return null;
        }
        
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setId(feature.getId());
        featureDTO.setName(feature.getName());
        featureDTO.setCode(feature.getCode());
        featureDTO.setDescription(feature.getDescription());
        featureDTO.setApplicationCode(feature.getApplicationCode());
        featureDTO.setDeploymentUnitCode(feature.getDeploymentUnitCode());
        featureDTO.setImageUrl(feature.getImageUrl());
        featureDTO.setStatus(feature.getStatus());
        
        // 获取配置列表
        List<FeatureConfig> configs = featureConfigMapper.findByFeatureId(featureId);
        List<FeatureConfigDTO> configDTOs = configs.stream().map(config -> {
            FeatureConfigDTO configDTO = new FeatureConfigDTO();
            configDTO.setId(config.getId());
            configDTO.setFeatureId(config.getFeatureId());
            configDTO.setKey(config.getKey());
            configDTO.setValue(config.getValue());
            configDTO.setDescription(config.getDescription());
            configDTO.setStatus(config.getStatus());
            return configDTO;
        }).collect(Collectors.toList());
        
        featureDTO.setConfigs(configDTOs);
        return featureDTO;
    }
    
    // 配置相关方法
    @Override
    public List<FeatureConfig> getConfigsByFeatureId(Long featureId) {
        return featureConfigMapper.findByFeatureId(featureId);
    }
    
    @Override
    public FeatureConfig getConfigById(Long id) {
        return featureConfigMapper.findById(id);
    }
    
    @Override
    public FeatureConfig createConfig(FeatureConfig featureConfig) {
        featureConfig.onCreate();
        featureConfigMapper.insert(featureConfig);
        return featureConfig;
    }
    
    @Override
    public FeatureConfig updateConfig(FeatureConfig featureConfig) {
        featureConfig.onUpdate();
        featureConfigMapper.update(featureConfig);
        return featureConfig;
    }
    
    @Override
    public void deleteConfig(Long id) {
        featureConfigMapper.deleteById(id);
    }
} 