package com.irusri.todo_rest_api.security;

import com.irusri.todo_rest_api.dao.UserDao;
import com.irusri.todo_rest_api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService  implements UserDetailsService {

    private final UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> u =  userDao.findByEmail(email);
        if(u.isPresent()){
            var userObj = u.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(userObj.getEmail())
                    .password(userObj.getPassword())
//                    .roles(userObj.getRole())
                    .build();
        }else{
            throw new UsernameNotFoundException(email);
        }
    }
}
