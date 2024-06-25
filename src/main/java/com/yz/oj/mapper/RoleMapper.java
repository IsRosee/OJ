package com.yz.oj.mapper;

import com.yz.oj.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM roles WHERE name = #{name}")
    Optional<Role> findByName(String name);

//    findRolesByUserId
    @Select("SELECT r.* FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    Optional<Role> findRolesByUserId(Long userId);

    @Insert("INSERT INTO user_roles (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    void deleteUserRoles(Long userId);
}
