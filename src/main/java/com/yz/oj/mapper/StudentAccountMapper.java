package com.yz.oj.mapper;
import com.yz.oj.entity.TeacherAccount;
import com.yz.oj.entity.StudentAccount;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface StudentAccountMapper {
    @Select("SELECT * FROM student_accounts WHERE user_id = #{userId}")
    @Results(
            id = "StudentAccountResultMap",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "studentNumber", column = "student_number"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "updatedAt", column = "updated_at")
            }
    )
    Optional<StudentAccount> findByUserId(Long userId);

    @Insert("INSERT INTO student_accounts (user_id, student_number, created_at, updated_at) " +
            "VALUES (#{userId}, #{studentNumber}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStudentAccount(StudentAccount studentAccount);

    @Select("SELECT user_id FROM student_accounts WHERE user_id = (SELECT id FROM users WHERE username = #{username})")
    long selectStudentIdByUsername(String username);

    @Update("UPDATE student_accounts SET student_number = #{studentNumber}, updated_at = #{updatedAt} WHERE user_id = #{userId}")
    void updateStudentAccount(StudentAccount studentAccount);

    @Delete("DELETE FROM student_accounts WHERE user_id = #{id}")
    void deleteStudentAccountByUserId(Long id);
}
