package com.swyp6.familytravel.notification.service;

import com.swyp6.familytravel.notification.entity.Notification;
import com.swyp6.familytravel.notification.repository.EmitterRepository;
import com.swyp6.familytravel.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;


    public SseEmitter connect(String lastEventId, Long userId){
        String emitterId = userId + "_" + System.currentTimeMillis();
        // save
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendToClient(emitter, emitterId, "EventStream Created");

        if(!lastEventId.isEmpty()){
            Map<String, Object> events = emitterRepository.findAllEventStartWithByUserId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data){
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception){
            throw new RuntimeException("SSE 연결 오류");
        }
    }

    public void send(UserEntity user, String content){
        Notification notification = Notification.builder()
                .user(user)
                .content(content)
                .build();
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(user.getId());
        sseEmitters.forEach((key, emitter) -> {
                    emitterRepository.saveEvent(key, notification);
                    sendToClient(emitter, key, notification);
                });
    }

}
