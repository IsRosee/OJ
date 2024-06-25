package com.yz.oj.mapper;

import com.yz.oj.entity.TestPaper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TestPaperMapper {

    @Insert("INSERT INTO test_papers (creator_id, title, description, questions, settings) " +
            "VALUES (#{creatorId}, #{title}, #{description}, #{questions}, #{settings})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTestPaper(TestPaper testPaper);

    @Select("SELECT * FROM test_papers WHERE id = #{id}")
    TestPaper selectTestPaperById(long id);

    @Select("SELECT * FROM test_papers WHERE creator_id = #{creatorId}")
    List<TestPaper> selectTestPapersByCreatorId(long creatorId);

    @Update("UPDATE test_papers SET title = #{title}, description = #{description}, questions = #{questions}, " +
            "settings = #{settings}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void updateTestPaper(TestPaper testPaper);

    @Delete("DELETE FROM test_papers WHERE id = #{id}")
    void deleteTestPaper(long id);
}
