package com.yz.oj.mapper;

import com.yz.oj.entity.SystemConfig;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SystemConfigMapper {

    @Select("SELECT * FROM system_config WHERE system_config.config_key = #{key}")
    @Results(id = "SystemConfigResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "key", column = "config_key"),
            @Result(property = "value", column = "config_value"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    SystemConfig selectConfigByKey(String key);

    @Update("UPDATE system_config SET system_config.config_value = #{value}, updated_at = CURRENT_TIMESTAMP WHERE system_config.config_key = #{key}")
    void updateConfigValue(@Param("key") String key, @Param("value") String value);

    @Insert("INSERT INTO system_config (system_config.config_key, system_config.config_value) VALUES (#{key}, #{value})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertConfig(SystemConfig config);
}
