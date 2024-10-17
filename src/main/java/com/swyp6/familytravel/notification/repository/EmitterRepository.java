package com.swyp6.familytravel.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository{

    private final Map<Long, Map<String ,SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, String emitterId, SseEmitter emitter){
        emitterMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);
        return emitter;
    }

    public Map<String, SseEmitter> findAllByUserId(Long userId){
        return new HashMap<>(emitterMap.getOrDefault(userId, new HashMap<>()));
    }

    public void deleteById(Long userId, String emitterId){
        emitterMap.get(userId).remove(emitterId);
    }

}
