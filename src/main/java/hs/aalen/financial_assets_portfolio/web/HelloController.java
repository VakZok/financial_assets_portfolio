package hs.aalen.financial_assets_portfolio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<>("hello world", HttpStatus.OK);
    }
}
