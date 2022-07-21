package com.example.strawberry.config;

import com.example.strawberry.application.dai.IUserRegisterRepository;
import com.example.strawberry.domain.entity.UserRegister;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
@EnableAsync
@Configuration
public class SchedulingConfig {
    private final IUserRegisterRepository userRegisterRepository;

    public SchedulingConfig(IUserRegisterRepository userRegisterRepository) {
        this.userRegisterRepository = userRegisterRepository;
    }


    // 0h hằng ngày check nếu đăng ký quá 24h chưa kích hoạt tài khoản thì xoá tài khoản
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    void deleteUserDontActive() {
        List<UserRegister> userRegisters = userRegisterRepository.findAllByStatus(Boolean.FALSE);
        Date date = new Date();
        userRegisters.forEach(i -> {
            if((date.getTime() - i.getCreatedAt().getTime()) > 86400000) {
                userRegisterRepository.delete(i);
            }
        });
    }
}
