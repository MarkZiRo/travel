package com.swyp6.familytravel.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private OctetStreamReadMsgConverter octetStreamReadMsgConverter;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(octetStreamReadMsgConverter);
    }
}
