package com.example.Employee_Management_System.dto.response;

import com.example.Employee_Management_System.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInformation {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String role;
    private boolean isEmailVerified;
    private boolean isLocked;
    public UserInformation(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.isEmailVerified = user.getVerificationCode() == null || user.getVerificationCode().length() == 0; // if verificationCode is null, it means that the user has verified their email
        this.isLocked = user.isLocked();
        this.role = user.getRole();
    }
}
