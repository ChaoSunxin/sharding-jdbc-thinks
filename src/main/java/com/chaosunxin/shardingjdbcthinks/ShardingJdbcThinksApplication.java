package com.chaosunxin.shardingjdbcthinks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShardingJdbcThinksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcThinksApplication.class, args);
    }

}
