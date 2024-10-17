package com.swyp6.familytravel.notification.controller;

import com.swyp6.familytravel.notification.service.NotificationService;
import com.swyp6.familytravel.auth.entity.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
    ) throws IOException {
        return notificationService.connect();
    }

    @GetMapping("/test")
    public void test(){
        notificationService.sendToUser("test");
    }
}
