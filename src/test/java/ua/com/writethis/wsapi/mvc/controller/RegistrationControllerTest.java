package ua.com.writethis.wsapi.mvc.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.services.UserService;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private UserService userService;
    private RegistrationController registrationController;

    @BeforeEach
    public void setup() {
        registrationController = new RegistrationController(userService);
    }

    @Test
    void register() {
        // given
        RegistrationDTO registrationDTO = new RegistrationDTO("mail", "pass", "pass");

        // when
        registrationController.register(registrationDTO);

        // then
        verify(userService).registerUser(any());
    }

    @Test
    void confirm() {
        // given
        final String TOKEN = "testtoken";

        // when
        registrationController.confirm(TOKEN);

        // then
        verify(userService).confirmRegistration(any());
    }

    @SneakyThrows
    @Test
    void handleValidationExceptions() {
        // given
        Method method = Object.class.getMethod("toString");
        MethodParameter methodParameter = new MethodParameter(method, -1);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        List<ObjectError> errors = new LinkedList<>();
        errors.add(new FieldError("objName1", "field1", "defMess1"));
        errors.add(new FieldError("objName2", "field2", "defMess2"));
        errors.add(new FieldError("objName3", "field3", "defMess3"));

        // when
        when(bindingResult.getAllErrors()).thenReturn(errors);
        Map<String, String> map = registrationController.handleValidationExceptions(exception);

        // then
        assertThat(map).isNotNull();
    }
}