package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (first_name, last_name, password, email, role, avatar, is_locked) VALUES (#{username}, #{password}, #{email}, #{role}, #{avatar}, #{isLocked})")
    void save(User user);


}
