package com.lssm.service;

import com.lssm.dto.LoginDTO;
import com.lssm.utils.Result;
import jakarta.validation.Valid;

import java.util.Map;

public interface UserService {

    Result<String> register(@Valid LoginDTO userDTO);

    Result<Map<String,Object>> login(@Valid LoginDTO userDTO);
    Result<String> logout(String token);

}
