package ua.com.writethis.wsapi.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ua.com.writethis.wsapi.mvc.dto.RegistrationDTO;
import ua.com.writethis.wsapi.mvc.services.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    @Value("${wsapi.emailVerification.token.expirationMinutes}")
    private int expirationMinutes;
    @Value("${wsapi.emailVerification.redirectUrl}")
    private String redirectUrl;

    @PostMapping
    public Map<String, String> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        userService.registerUser(registrationDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "A verification email had been sent to the email address: " + registrationDTO.getEmail());
        response.put("expirationMinutes", String.valueOf(expirationMinutes));
        return response;
    }

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @GetMapping("confirm")
    public ModelAndView confirm(@RequestParam String token) {
        userService.confirmRegistration(token);

        return new ModelAndView("redirect:" + redirectUrl);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(errors.toString());
        return errors;
    }
}
