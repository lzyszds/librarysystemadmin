package com.example.librarysystemadmin;

import com.example.librarysystemadmin.config.LibraryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(LibraryConfig.class)
public class LibrarySystemAdminApplication {

    public static void main(String[] args) {

        SpringApplication.run(LibrarySystemAdminApplication.class, args);
    }

}
