package com.zhanlin.library_management_system;

import com.zhanlin.library_management_system.config.AdminBootstrapProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AdminBootstrapProperties.class)
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

}
