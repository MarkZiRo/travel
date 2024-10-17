package com.swyp6.familytravel.notification.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.notification.entity.Notification;
import com.swyp6.familytravel.notification.repository.EmitterRepository;
import com.swyp6.familytravel.notification.repository.NotificationRepository;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String EVENT_NAME = "notification";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final AuthenticationFacade facade;

    public SseEmitter connect() throws IOException {
        UserEntity user = facade.extractUser();
        Long userId = user.getId();
        String emitterId = UUID.randomUUID().toString();
        // save
        SseEmitter emitter = emitterRepository.save(userId, emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(userId, emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId, emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(userId, emitterId));

        emitter.send(createEvent(userId, "initial", "EventStream Created"));
        sendEventToEmitter(emitter, user, emitterId, "EventStream Created");

        List<Notification> notifications = notificationRepository.findAllByUserOrderByCreatedDateTimeDesc(user);
        notificationRepository.deleteByUser(user);
        notifications.forEach(notification -> sendToUser(user, notification.getContent()));

        return emitter;
    }

    private void sendEventToEmitter(SseEmitter emitter, UserEntity user, String emitterId, String content) {
        try {
            if (emitter != null) {
                emitter.send(createEvent(user.getId(), EVENT_NAME, content));
            }
        } catch (Exception exception) {
            // 예외 발생 시 처리
            emitterRepository.deleteById(user.getId(), emitterId);
            notificationRepository.save(Notification.builder()
                    .user(user)
                    .content(content)
                    .build());
            emitter.completeWithError(exception);
        }
    }

    public void sendToUser(String content) {
        UserEntity user = facade.extractUser();
        Map<String, SseEmitter> userEmitter = emitterRepository.findAllByUserId(user.getId());
        assert(userEmitter != null);
        userEmitter.forEach((emitterId, emitter) -> sendEventToEmitter(emitter, user, emitterId, content));
    }

    public void sendToUser(UserEntity user, String content) {
        Map<String, SseEmitter> userEmitter = emitterRepository.findAllByUserId(user.getId());
        assert(userEmitter != null);
        userEmitter.forEach((emitterId, emitter) -> sendEventToEmitter(emitter, user, emitterId, content));
    }

    private SseEmitter.SseEventBuilder createEvent(Long userId, String name, String content) {
        return SseEmitter.event()
                .id(userId.toString())
                .name(name)
                .data(content)
                .reconnectTime(10000);
    }
}
