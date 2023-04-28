package com.example.basics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.PropertySource;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(CnjProps.class)
public class BasicsApplication {


    @Bean
    ApplicationRunner runner(CnjProps cnjProps) {
        return args -> {
            log.info("Message: {}", cnjProps.getMessage());

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BasicsApplication.class, args);
    }

}

class CnjPropertySource extends PropertySource<String> {


    public CnjPropertySource() {
        super("cnj");
    }

    @Override
    public Object getProperty(String name) {
        return System.getenv("USER");
    }
}

@AllArgsConstructor
@Data
@ConfigurationProperties(prefix = "cnj")
class CnjProps {
    private final String message;


}