package com.yz.oj.mapper;

import com.yz.oj.entity.ExperimentSubmission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExperimentSubmissionMapper {

    @Insert("INSERT INTO submissions (experiment_id, student_id, answer, status) " +
            "VALUES (#{experimentId}, #{studentId}, #{answer}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertExperimentSubmission(ExperimentSubmission submission);

    @Select("SELECT * FROM submissions WHERE student_id = #{studentId}")
    @Results(id = "ExperimentSubmissionResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "experimentId", column = "experiment_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "answer", column = "answer"),
            @Result(property = "status", column = "status"),
            @Result(property = "submittedAt", column = "submitted_at")
    })
    List<ExperimentSubmission> selectSubmissionsByStudentId(long studentId);

    @Select("SELECT * FROM submissions WHERE experiment_id = #{experimentId}")
    @ResultMap("ExperimentSubmissionResultMap")
    List<ExperimentSubmission> selectSubmissionsByExperimentId(long experimentId);

    @Select("SELECT * FROM submissions WHERE id = #{id}")
    @ResultMap("ExperimentSubmissionResultMap")
    ExperimentSubmission selectSubmissionById(long id);

    @Update("UPDATE submissions SET answer = #{answer}, status = #{status}, submitted_at = #{submittedAt} " +
            "WHERE id = #{id}")
    void updateExperimentSubmission(ExperimentSubmission submission);

    @Delete("DELETE FROM submissions WHERE id = #{id}")
    void deleteExperimentSubmission(long id);

    @Delete("DELETE FROM submissions WHERE experiment_id = #{id}")
    void deleteSubmissionsByExperimentId(long id);
}
