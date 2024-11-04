package com.irusri.todo_rest_api.controller;

import com.irusri.todo_rest_api.dao.UserDao;
import com.irusri.todo_rest_api.entity.User;
import com.irusri.todo_rest_api.security.UserDetailService;
import com.irusri.todo_rest_api.webtoken.JwtService;
import com.irusri.todo_rest_api.webtoken.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailService userDetailService;

    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @PostMapping("/login")
    public String authentication(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.email(), loginForm.password()
        ));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(userDetailService.loadUserByUsername(loginForm.email()));
        }else{
            throw new UsernameNotFoundException("Invalid Credential");
        }
    }


}
