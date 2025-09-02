package com.sa.eureka_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sa.eureka_server.EurekaServerApplication;

@SpringBootTest
class EurekaServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodRunsWithoutException() {
		EurekaServerApplication.main(new String[] {});
	}
}
