package com.service.acountservice.accountservice.security.auth;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private Long id;
    private String name;

    public UserPrincipal(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
