package com.example.scheduling.dto;

import lombok.Builder;

@Builder
public record AuthDTO(String name, String email, String password, String phone, String address) {

}
