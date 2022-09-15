package ua.com.writethis.wsapi.mvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class HealthcheckControllerTest {

    @Autowired
    private HealthcheckController healthcheckController;

    @Test
    void healthcheck() {
        //given
        final String EXPECTED = "I'm alive!!!";

        //when
        String responseBody = healthcheckController.healthcheck().getBody();

        //then
        assertThat(responseBody).isEqualTo(EXPECTED);
    }
}