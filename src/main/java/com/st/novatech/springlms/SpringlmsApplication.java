package com.st.novatech.springlms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main driver class for the application for running from the command line.
 */
@PropertySource("classpath:database-config.properties")
@SpringBootApplication
public class SpringlmsApplication {

	/**
	 * Main method.
	 * @param args parsed by Spring
	 */
	public static void main(final String[] args) {
		SpringApplication.run(SpringlmsApplication.class, args);
	}

	/**
	 * Enable cross-origin requests.
	 *
	 * @return the CORS filter
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT",
						"DELETE", "HEAD");
			}
		};
	}

}
