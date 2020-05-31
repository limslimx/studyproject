package com.studyproject.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app") //application.properties의 app을 찾음
public class AppProperties {

    private String host; //application.properties내의 app.host의 값을 가져옴
}
