package com.software.grey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class GreyApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

}
