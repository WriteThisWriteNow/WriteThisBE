package ua.com.writethis.wsapi.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {

    @GetMapping
    public ResponseEntity<String> healthcheck(){
        return ResponseEntity.ok("I'm alive!!!");
    }
}
