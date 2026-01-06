package com.hypercube.evisa.common.api.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RefreshJwtRequestDTO {
    private String refreshToken;
}