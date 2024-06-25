package com.yz.oj.mapper;
import com.yz.oj.entity.TeacherAccount;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface TeacherAccountMapper {
    @Select("SELECT * FROM teacher_accounts WHERE user_id = #{userId}")
    @Results(
            id = "TeacherAccountResultMap",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "updatedAt", column = "updated_at")
            }
    )
    Optional<TeacherAccount> findByUserId(Long userId);

    @Select("SELECT user_id FROM teacher_accounts WHERE user_id = (SELECT id FROM users WHERE username = #{username})")
    Long findTeacherIdByUsername(String username);
    @Insert("INSERT INTO teacher_accounts (user_id, created_at, updated_at) " +
            "VALUES (#{userId}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTeacherAccount(TeacherAccount teacherAccount);
}
