package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.LoginRequestDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Cliente login(@RequestBody LoginRequestDTO dto) {
        return loginService.login(dto);
    }
}