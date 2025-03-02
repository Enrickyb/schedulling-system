package com.example.scheduling.dto;


import com.example.scheduling.models.User;

public interface UserProjection {
    static UserProjection fromEntity(User customer) {
        return new UserProjection() {
            @Override
            public String getEmail() {
                return customer.getEmail();
            }

            @Override
            public String getName() {
                return customer.getName();
            }

            @Override
            public String getPhone() {
                return customer.getPhone();
            }

            @Override
            public String getAddress() {
                return customer.getAddress();
            }
        };
    }

    String getEmail();
    String getName();
    String getPhone();
    String getAddress();
}