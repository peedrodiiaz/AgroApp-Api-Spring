package com.salesianostriana.dam.agroapp.controller;

import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final PasswordEncoder passwordEncoder;
    private final TrabajadorRepository trabajadorRepository;

    @GetMapping("/hash")
    public String hashPassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/check-password")
    public String checkPassword(@RequestParam String email, @RequestParam String password) {
        var user = trabajadorRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "Usuario no encontrado";
        }
        
        String dbHash = user.get().getPassword();
        boolean matches = passwordEncoder.matches(password, dbHash);
        
        return "Email: " + email + 
               "<br>Password enviada: " + password + 
               "<br>Hash en DB: " + dbHash + 
               "<br>Matches: " + matches;
    }
}
