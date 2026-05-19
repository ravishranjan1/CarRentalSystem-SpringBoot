package com.rentify.carrental.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

    public static List<String> getAllRoleNames() {
        return Arrays.stream(values())
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
}
