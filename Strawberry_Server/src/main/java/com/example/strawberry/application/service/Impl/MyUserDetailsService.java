package com.example.strawberry.application.service.Impl;//package com.example.strawberry.application.service.Impl;
//
//import com.example.product.application.dai.IUserRepository;
//import com.example.product.config.exception.NotFoundException;
//import com.example.product.domain.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService  {
//    @Autowired
//    private IUserRepository userRepository;
//
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if(user == null){
//            throw new NotFoundException("Username " + username + " does not exist.");
//        }
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
//
//    }
//}
