package com.example.Employee_Management_System.mapper;

import com.example.Employee_Management_System.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (first_name, last_name, password, email, role, avatar, is_locked) VALUES (#{firstName}, #{lastName}, #{password}, #{email}, #{role}, #{avatar}, #{isLocked})")
    void save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    @Update("UPDATE users SET first_name = #{firstName}, last_name = #{lastName}, password = #{password}, email = #{email}, role = #{role}, avatar = #{avatar}, is_locked = #{isLocked} WHERE id = #{id}")
    void update(User user);

    User findByVerificationCode(String verificationCode);
}
