package com.prepify.be.controllers;

import com.prepify.be.entities.*;
import com.prepify.be.repositories.*;
import com.prepify.be.response.BaseResponse;
import com.prepify.be.response.BaseResponseBuilder;
import com.prepify.be.services.external.GoogleCloudStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(AdminController.MODULE_PATH)
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GoogleCloudStorageService googleCloudStorageService;

    public static final String MODULE_PATH = "api/admin";

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<Page<User>>> getAllUser(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "fullname", required = false) String fullname
    ) {
        if (Objects.isNull(page) || page < 0) {
            page = 0;
        } else {
            page = page - 1;
        }

        if (Objects.isNull(size) || size < 1) {
            size = 15;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllWithConditions(username, fullname, pageable);


        return ResponseEntity.ok(BaseResponseBuilder.success(users));
    }
}
