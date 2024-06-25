package com.yz.oj.mapper;

import com.yz.oj.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("INSERT INTO notifications (teacher_id, title, content) VALUES (#{teacherId}, #{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertNotification(Notification notification);

    @Select("SELECT * FROM notifications WHERE id = #{id}")
    @Results(id = "NotificationResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "teacherId", column = "teacher_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createdAt", column = "created_at")
    })
    Notification selectNotificationById(long id);

    @Select("SELECT * FROM notifications WHERE teacher_id = #{teacherId}")
    @ResultMap("NotificationResultMap")
    List<Notification> selectNotificationsByTeacherId(long teacherId);

    @Update("UPDATE notifications SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updateNotification(Notification notification);

    @Delete("DELETE FROM notifications WHERE id = #{id}")
    void deleteNotification(long id);

    @Select("SELECT * FROM notifications")
    @ResultMap("NotificationResultMap")
    List<Notification> selectAll();
}
