package com.yz.oj.mapper;

import com.yz.oj.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO questions (creator_id, question_type, question_text, options, answer) " +
            "VALUES (#{creatorId}, #{questionType}, #{questionText}, #{options}, #{answer})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertQuestion(Question question);

    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question selectQuestionById(long id);

    @Select("SELECT * FROM questions WHERE creator_id = #{creatorId}")
    List<Question> selectQuestionsByCreatorId(long creatorId);

    @Update("UPDATE questions SET question_type = #{questionType}, question_text = #{questionText}, " +
            "options = #{options}, answer = #{answer}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void updateQuestion(Question question);

    @Delete("DELETE FROM questions WHERE id = #{id}")
    void deleteQuestion(long id);
}
