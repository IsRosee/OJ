package com.yz.oj.mapper;

import com.yz.oj.entity.TestResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TestResultMapper {

    @Insert("INSERT INTO test_results (test_paper_id, student_id, answers, score) " +
            "VALUES (#{testPaperId}, #{studentId}, #{answers}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTestResult(TestResult testResult);

    @Select("SELECT * FROM test_results WHERE id = #{id}")
    TestResult selectTestResultById(long id);

    @Select("SELECT * FROM test_results WHERE test_paper_id = #{testPaperId}")
    List<TestResult> selectTestResultsByTestPaperId(long testPaperId);

    @Select("SELECT * FROM test_results WHERE student_id = #{studentId}")
    List<TestResult> selectTestResultsByStudentId(long studentId);

    @Update("UPDATE test_results SET answers = #{answers}, score = #{score}, completed_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{id}")
    void updateTestResult(TestResult testResult);

    @Delete("DELETE FROM test_results WHERE id = #{id}")
    void deleteTestResult(long id);
}
