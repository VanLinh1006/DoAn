package com.prepify.be.controllers;

import com.prepify.be.config.jwt.JwtService;
import com.prepify.be.dto.request.ForgotPasswordBody;
import com.prepify.be.dto.request.LoginBody;
import com.prepify.be.dto.request.MailStructure;
import com.prepify.be.dto.request.SignUpBody;
import com.prepify.be.dto.response.UserResponse;
import com.prepify.be.entities.User;
import com.prepify.be.repositories.UserRepository;
import com.prepify.be.response.BaseResponse;
import com.prepify.be.response.BaseResponseBuilder;
import com.prepify.be.services.external.GmailService;
import com.prepify.be.utils.JsonUtils;
import com.prepify.be.utils.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(AuthController.MODULE_PATH)
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GmailService gmailService;

    @Value("${spring.client.host}")
    private String clientHost;

    public static final String MODULE_PATH = "api/auth";

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<User>> signUp(
            @RequestBody SignUpBody signUpBody
    ) {
        if (Objects.isNull(signUpBody) || Objects.isNull(signUpBody.getEmail())
                || Objects.isNull(signUpBody.getPassword()) || Objects.isNull(signUpBody.getConfirmPassword())) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Cần gửi lên tên đăng nhập và mật khẩu"));
        }

        if (!signUpBody.getPassword().equals(signUpBody.getConfirmPassword())) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Mật khẩu nhập lại không chính xác"));
        }

        if (!StringUtils.validEmailString(signUpBody.getEmail())) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Email không hợp lệ"));
        }

        User existUser = userRepository.findFirstByEmail(signUpBody.getEmail());

        if (Objects.nonNull(existUser)) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Username đã tồn tại"));
        }

        User user = new User();
        user.setEmail(signUpBody.getEmail());
        user.setPassword(passwordEncoder.encode(signUpBody.getPassword()));
        user.setRole("user");
        user.setName(signUpBody.getEmail());

        User signUpResponse = userRepository.save(user);

        return ResponseEntity.ok(BaseResponseBuilder.success(signUpResponse));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<UserResponse>> login(
            @RequestBody LoginBody loginBody
    ) {
        if (Objects.isNull(loginBody) || Objects.isNull(loginBody.getEmail())
                || Objects.isNull(loginBody.getPassword())) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Cần gửi lên tên đăng nhập và mật khẩu"));
        }

        User user = userRepository.findFirstByEmail(loginBody.getEmail().trim());

        if (Objects.isNull(user)) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Thông tin đăng nhập không chính xác"));
        }

        boolean isRightPassword = passwordEncoder.matches(loginBody.getPassword(), user.getPassword());

        if (!isRightPassword) {
            return ResponseEntity.ok(BaseResponseBuilder.error(200, "Thông tin đăng nhập không chính xác"));
        }

        UserResponse userResponse = JsonUtils.parse(JsonUtils.stringify(user), UserResponse.class);
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getEmail());
        payload.put("id", user.getId());
        payload.put("role", user.getRole());
        String accessToken = jwtService.genJwtToken(payload);
        userResponse.setAccessToken(accessToken);

        return ResponseEntity.ok(BaseResponseBuilder.success(userResponse));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<BaseResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordBody body
    ) {
        User user = userRepository.findFirstByEmail(body.getUsername());
        if (Objects.isNull(user)) {
            return ResponseEntity.ok(BaseResponseBuilder.error(400, "username not exists", null));
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getEmail());
        payload.put("id", user.getId());
        payload.put("role", user.getRole());
        String accessToken = jwtService.genJwtTokenForgotPassword(payload);

        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("Reset password Prepify, link expired after 5 minutes");
        mailStructure.setMessage(clientHost + "/reset-password?access_token=" + accessToken);

        gmailService.sendEmail(user.getEmail(), mailStructure);

        return ResponseEntity.ok(BaseResponseBuilder.success("Send link reset password to email"));
    }
}
