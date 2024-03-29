package ua.com.writethis.wsapi.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.writethis.wsapi.mvc.controller.HealthcheckController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({HealthcheckController.class})
class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @Test
    void healthcheckShouldPassTest() throws Exception {
        mockMvc
                .perform(get("/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("I'm alive!!!")));
    }
}
