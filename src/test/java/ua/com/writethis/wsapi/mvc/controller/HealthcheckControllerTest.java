package ua.com.writethis.wsapi.mvc.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthcheckControllerTest {

    @Test
    void healthcheck() {
        //given
        HealthcheckController healthcheckController = new HealthcheckController();
        final String EXPECTED = "I'm alive!!!";

        //when
        String responseBody = healthcheckController.healthcheck().getBody();

        //then
        assertThat(responseBody).isEqualTo(EXPECTED);
    }
}