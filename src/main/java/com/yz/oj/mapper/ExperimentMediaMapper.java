package com.yz.oj.mapper;

import com.yz.oj.entity.ExperimentMedia;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExperimentMediaMapper {

    @Insert("INSERT INTO experiment_media (experiment_id, media_type, media_url, sort_order) " +
            "VALUES (#{experimentId}, #{mediaType}, #{mediaUrl}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertExperimentMedia(ExperimentMedia media);

    @Delete("DELETE FROM experiment_media WHERE id = #{id}")
    void deleteExperimentMedia(long id);

    @Select("SELECT * FROM experiment_media WHERE experiment_id = #{experimentId} ORDER BY sort_order")
    @Results(id = "ExperimentMediaResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "experimentId", column = "experiment_id"),
            @Result(property = "mediaType", column = "media_type"),
            @Result(property = "mediaUrl", column = "media_url"),
            @Result(property = "sortOrder", column = "sort_order"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<ExperimentMedia> selectMediaByExperimentId(long experimentId);

    @Delete("DELETE FROM experiment_media WHERE experiment_id = #{experimentId}")
    void deleteMediaByExperimentId(long experimentId);
}
