package ua.com.writethis.wsapi.mvc.controller;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public String register(@Valid @RequestBody RegistrationDTO registrationDTO) {

        userService.registerUser(registrationDTO);
//TODO:
//        * Send token to email (create message)
//        * Change email verification logic to password encoder

        return "A verification email had been sent to the email address: " + registrationDTO.getEmail();
    }

    @GetMapping("confirm")
    public ResponseEntity confirm(@RequestParam String token) {

//TODO:
//        * Decode token
//        * Check if it's active (wasn't expired)
//        * Find user with this token
//        * Compare email and expiration date
//        * Set "enabled" in true
//        * Clear token field in db

        return ResponseEntity.ok().build();
    }

//    TODO: Update logination. Forbid loginning, if email wasn't confirmed

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(errors.toString());
        return errors;
    }
}
