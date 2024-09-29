package com.example.demo.user;


import lombok.Getter;

@Getter
public enum UserRole { //사용자가 로그인 한 후 권한과 관련된 내용
    SUPER_ADMIN("ROLE_SUPERADMIN"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }
    private String value;

}
