package com.example.Employee_Management_System.repository;

import com.example.Employee_Management_System.domain.Manager;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.dto.response.UserInformation;
import com.example.Employee_Management_System.mapper.UserMapper;
import com.example.Employee_Management_System.model.EmployeeInformation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ManagerRepository managerRepository;

    public void save(User user) {
        userMapper.save(user);
    }

    public boolean existsByEmail(String email) {
        return userMapper.findByEmail(email).isPresent();
    }

    public Optional<User> findByUsername(String username) {
        return userMapper.findByEmail(username);
    }

    public Manager findManagerByReferenceCode(String referenceCode) {
        return managerRepository.findManagerByReferenceCode(referenceCode);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    //update only firstname and lastName
    public void updateName(User user) {
        userMapper.updateName(user);
    }

    public void updateVerificationCode(User user) {
        userMapper.updateVerificationCode(user);
    }

    public User findByVerificationCode(String verificationCode) {
        return userMapper.findByVerificationCode(verificationCode);
    }

    public User findUserById(Long employeeId) {
        return userMapper.findById(employeeId);
    }

    public List<EmployeeInformation> getEmployeeBelongToManager(long managerId) {
        return userMapper.getEmployeeBelongToManager(managerId);
    }

    @Update("UPDATE users SET is_locked = false WHERE id = #{id}")
    public void unlockUser(Long id) {
        userMapper.unlockUser(id);
    }

    public List<User> findAllManagerUnverified() {
        return userMapper.getAllManagerUnverified();
    }
}
