package com.transport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.TimeZone;
import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class TransportTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportTrackingApplication.class, args);
	}

	@PostConstruct
	void setUTCTimezone(){
		 TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
