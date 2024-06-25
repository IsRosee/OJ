package com.yz.oj.service;

import com.yz.oj.mapper.SystemConfigMapper;
import com.yz.oj.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public String getConfigValue(String key) {
        SystemConfig config = systemConfigMapper.selectConfigByKey(key);
        return config != null ? config.getValue() : null;
    }

    public void setConfigValue(String key, String value) {
        SystemConfig config = systemConfigMapper.selectConfigByKey(key);
        if (config != null) {
            systemConfigMapper.updateConfigValue(key, value);
        } else {
            config = new SystemConfig();
            config.setKey(key);
            config.setValue(value);
            systemConfigMapper.insertConfig(config);
        }
    }
}
