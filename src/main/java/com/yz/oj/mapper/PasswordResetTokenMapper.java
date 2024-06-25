package com.yz.oj.mapper;

import com.yz.oj.entity.PasswordResetToken;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface PasswordResetTokenMapper {
    @Select("SELECT * FROM password_reset_tokens WHERE token = #{token}")
    @Results(id = "PasswordResetTokenResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "token", column = "token"),
            @Result(property = "expiresAt", column = "expires_at")
    })
    Optional<PasswordResetToken> findByToken(String token);

    @Insert("INSERT INTO password_reset_tokens (user_id, token, expires_at) " +
            "VALUES (#{userId}, #{token}, #{expiresAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPasswordResetToken(PasswordResetToken passwordResetToken);
}
