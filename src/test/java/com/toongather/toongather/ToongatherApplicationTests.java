package com.toongather.toongather;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles({"local"})
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ToongatherApplicationTests {

	@Autowired
	private Environment env;

	@Test
	public void testOAuth2Properties() {
		Assertions.assertThat(env.getProperty("spring.security.oauth2.client.registration.google.redirectUri"))
			.isEqualTo("http://localhost:8080/oauth2/callback/google");
	}

	@Test
	void contextLoads() {
	}

}
