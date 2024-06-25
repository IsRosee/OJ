package com.yz.oj.mapper;

import com.yz.oj.entity.SubmissionMedia;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubmissionMediaMapper {

    @Insert("INSERT INTO submission_media (submission_id, media_type, media_url, sort_order) " +
            "VALUES (#{submissionId}, #{mediaType}, #{mediaUrl}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertSubmissionMedia(SubmissionMedia media);

    @Select("SELECT * FROM submission_media WHERE submission_id = #{submissionId} ORDER BY sort_order")
    @Results(id = "SubmissionMediaResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "submissionId", column = "submission_id"),
            @Result(property = "mediaType", column = "media_type"),
            @Result(property = "mediaUrl", column = "media_url"),
            @Result(property = "sortOrder", column = "sort_order"),
            @Result(property = "createdAt", column = "created_at")
    })
    List<SubmissionMedia> selectMediaBySubmissionId(long submissionId);

    @Delete("DELETE FROM submission_media WHERE id = #{id}")
    void deleteSubmissionMedia(long id);

    @Delete("DELETE FROM submission_media WHERE submission_id = #{id}")
    void deleteMediaBySubmissionId(long id);

    // 先得到提交记录的id，再删除提交的媒体文件
    @Delete("DELETE FROM submission_media WHERE submission_id IN (SELECT id FROM submissions WHERE experiment_id = #{id})")
    void deleteMediaByExperimentId(long id);

}
