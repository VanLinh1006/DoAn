package com.prepify.be.controllers;

import com.prepify.be.auth.AuthUser;
import com.prepify.be.dto.request.ChangePasswordBody;
import com.prepify.be.dto.request.ResetPasswordBody;
import com.prepify.be.dto.request.UpdateInfoBody;
import com.prepify.be.entities.User;
import com.prepify.be.repositories.UserRepository;
import com.prepify.be.response.BaseResponse;
import com.prepify.be.response.BaseResponseBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(UserController.MODULE_PATH)
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static final String MODULE_PATH = "api/user";

    @GetMapping("")
    public ResponseEntity<BaseResponse<User>> getUser() {
        var authUser = (AuthUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long id = authUser.getId();

        User user = userRepository.findFirstById(id);

        if (Objects.isNull(user)) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Không tìm thấy user", null));
        }

        return ResponseEntity.ok(BaseResponseBuilder.success(user));
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse<User>> updateInfo(
            @Valid @RequestBody UpdateInfoBody body
    ) {
        var authUser = (AuthUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long id = authUser.getId();
        User user = userRepository.findFirstById(id);

        if (Objects.isNull(body.getName()) && Objects.isNull(body.getPhone())) {
            return ResponseEntity.ok(BaseResponseBuilder.success(user));
        }

        if (Objects.nonNull(body.getName())) {
            user.setName(body.getName());
        }

        userRepository.save(user);

        return ResponseEntity.ok(BaseResponseBuilder.success(user));
    }

    @PostMapping("reset-password")
    public ResponseEntity<BaseResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordBody body
            ) {
        var authUser = (AuthUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long id = authUser.getId();

        User user = userRepository.findFirstById(id);
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setUpdatedAt(LocalDateTime.now().toString());
        userRepository.save(user);

        return ResponseEntity.ok(BaseResponseBuilder.success("Update passwword success"));
    }

    @PostMapping("change-password")
    public ResponseEntity<BaseResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordBody body
    ) {
        var authUser = (AuthUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long id = authUser.getId();
        User user = userRepository.findFirstById(id);

        boolean isRightPassword = passwordEncoder.matches(body.getOldPassword(), user.getPassword());

        if (!isRightPassword) {
            return ResponseEntity.ok(BaseResponseBuilder.error(400, "Old password not exact"));
        }

        user.setPassword(passwordEncoder.encode(body.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now().toString());
        userRepository.save(user);

        return ResponseEntity.ok(BaseResponseBuilder.success("Change passwword success"));
    }

    @GetMapping("check-auth")
    public ResponseEntity<BaseResponse<String>> checkAuth() {
        return ResponseEntity.ok(BaseResponseBuilder.success("Valid token"));
    }
}
