package dev.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevNoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevNoteApplication.class, args);
    }

}
