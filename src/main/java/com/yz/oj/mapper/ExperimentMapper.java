package com.yz.oj.mapper;

import com.yz.oj.entity.Experiment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExperimentMapper {

    @Insert("INSERT INTO experiments (teacher_id, title, content, status) " +
            "VALUES (#{teacherId}, #{title}, #{content}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertExperiment(Experiment experiment);

    @Update("UPDATE experiments SET title = #{title}, content = #{content}, status = #{status}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void updateExperiment(Experiment experiment);

    @Delete("DELETE FROM experiments WHERE id = #{id}")
    void deleteExperiment(long id);

    @Select("SELECT * FROM experiments WHERE id = #{id}")
    @Results(id = "ExperimentResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "status", column = "status")
    })
    Experiment selectExperimentById(long id);

    @Select("SELECT * FROM experiments")
    @ResultMap("ExperimentResultMap")
    List<Experiment> selectAll();

    // 某学生用户的已提交的实验
    @Select("SELECT * FROM experiments WHERE id IN (SELECT experiment_id FROM submissions WHERE student_id = #{userId})")
    @ResultMap("ExperimentResultMap")
    List<Experiment> selectSubmittedExperimentsByUserId(long userId);


    // 某学生用户的未提交的实验
    @Select("SELECT * FROM experiments WHERE id NOT IN (SELECT experiment_id FROM submissions WHERE student_id = #{userId})")
    List<Experiment> selectUnsubmittedExperimentsByUserId(long userId);
}
