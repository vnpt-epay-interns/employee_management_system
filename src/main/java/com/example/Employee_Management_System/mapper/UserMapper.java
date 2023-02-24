package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (first_name, last_name, password, email, role, avatar, is_locked, verification_code) VALUES (#{firstName}, #{lastName}, #{password}, #{email}, #{role}, #{avatar}, #{isLocked}, #{verificationCode})")
    void save(User user);

    Optional<User> findByEmail(String email);

    @Update("UPDATE users SET first_name = #{firstName}, last_name = #{lastName}, password = #{password}, email = #{email}, role = #{role}, avatar = #{avatar}, is_locked = #{isLocked}, verification_code = #{verificationCode} WHERE id = #{id}")
    void update(User user);

    @Update("UPDATE users SET verification_code = #{verificationCode} WHERE id = #{id}")
    void updateVerificationCode(User user);

    User findByVerificationCode(String verificationCode);

}
