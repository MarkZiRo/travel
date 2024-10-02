package com.swyp6.familytravel.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository{

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter emitter){
        emitterMap.put(emitterId, emitter);
        return emitter;
    }

    public void saveEvent(String emitterId, Object event){
        eventCache.put(emitterId, event);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(Long userId){
        return emitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(String.valueOf(userId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventStartWithByUserId(Long userId){
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(String.valueOf(userId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteById(String emitterId){
        emitterMap.remove(emitterId);
    }

    public void deleteAllEmitterStartWithByUserId(Long userId){
        emitterMap.entrySet().removeIf(entry -> entry.getKey().startsWith(String.valueOf(userId)));
    }

    public void deleteAllEventStartWithByUserId(Long userId){
        eventCache.entrySet().removeIf(entry -> entry.getKey().startsWith(String.valueOf(userId)));
    }
}
