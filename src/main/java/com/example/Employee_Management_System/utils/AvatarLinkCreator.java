package com.example.Employee_Management_System.utils;

public class AvatarLinkCreator {
    public static String createAvatarLink(String firstName, String lastName) {
        String avatarLink = String.format("https://ui-avatars.com/api/?name=%s+%s&background=random",
                firstName, lastName);
        return avatarLink;
    }
}
