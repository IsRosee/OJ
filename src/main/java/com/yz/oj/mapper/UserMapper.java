package com.yz.oj.mapper;

import com.yz.oj.entity.Role;
import com.yz.oj.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results(
            id = "UserResultMap",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "passwordHash", column = "password_hash"),
                    @Result(property = "salt", column = "salt"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "userType", column = "user_type"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "updatedAt", column = "updated_at"),
                    @Result(property = "isActive", column = "is_active"),
                    @Result(property = "avatarUrl", column = "avatar_url"),
                    @Result(property = "nickname", column = "nickname"),
                    @Result(property = "signature", column = "signature"),
                    @Result(property = "todoList", column = "todo_list"),
                    @Result(property = "roles", column = "id", many = @Many(select = "com.yz.oj.mapper.RoleMapper.findRolesByUserId"))
            }
    )
    Optional<User> findByUsername(String username);

    @Select("SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    Set<Role> findRolesByUserId(Long userId);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @ResultMap("UserResultMap")
    Optional<User> findByEmail(String email);

    //
    @Insert("INSERT INTO users (username, password_hash, salt, email, user_type, created_at, updated_at, is_active, avatar_url, nickname, signature, todo_list) " +
            "VALUES (#{username}, #{passwordHash}, #{salt}, #{email}, #{userType}, #{createdAt}, #{updatedAt}, #{isActive}, #{avatarUrl}, #{nickname}, #{signature}, #{todoList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT salt FROM users WHERE username = #{username}")
    String getSalt(String username);

    @Select("SELECT password_hash FROM users WHERE username = #{username}")
    String getPasswordHash(String username);

    @Select("SELECT * FROM users WHERE user_type = #{userType}")
    @ResultMap("UserResultMap")
    List<User> findByUserType(User.UserType userType);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @ResultMap("UserResultMap")
    Optional<User> findById(Long id);

    @Update("UPDATE users SET username = #{username}, password_hash = #{passwordHash}, salt = #{salt}, email = #{email}, user_type = #{userType}, created_at = #{createdAt}, updated_at = #{updatedAt}, is_active = #{isActive}, avatar_url = #{avatarUrl}, nickname = #{nickname}, signature = #{signature}, todo_list = #{todoList} WHERE id = #{id}")
    void updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(Long id);
}
