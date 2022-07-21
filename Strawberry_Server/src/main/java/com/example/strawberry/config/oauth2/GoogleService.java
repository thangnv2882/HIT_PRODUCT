package com.example.strawberry.config.oauth2;

import com.example.strawberry.adapter.web.base.AuthenticationProvider;
import com.example.strawberry.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.service.IUserService;
import com.example.strawberry.application.service.Impl.MyUserDetailsService;
import com.example.strawberry.application.utils.JwtTokenUtil;
import com.example.strawberry.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class GoogleService {
    private final IUserRepository userRepository;
    private final IUserService userService;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public GoogleService(IUserRepository userRepository, IUserService userService, MyUserDetailsService myUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    AuthenticationResponse saveUserWithFirstLogin(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttributes().get("email").toString();
//        String username = email.substring(0, email.indexOf('@'));
        User user = userService.getUserByEmail(email);
        if (user != null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());
            String jwt = jwtTokenUtil.generateToken(userDetails);
            return new AuthenticationResponse(user, jwt);
        }
        user = new User();
        user.setAuthProvider(AuthenticationProvider.GOOGLE);
        user.setPhoneNumber(token.getPrincipal().getAttributes().get("sub").toString());
        user.setFullName(token.getPrincipal().getAttributes().get("name").toString());
        user.setPassword(passwordEncoder().encode(email));
        user.setLinkAvt((String) token.getPrincipal().getAttributes().get("picture"));
        user.setEmail(email);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);
        userRepository.save(user);
        return new AuthenticationResponse(user, jwt);

    }
}
