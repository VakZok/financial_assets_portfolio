package hs.aalen.financial_assets_portfolio.web;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    @GetMapping("/logins")
    public ResponseEntity<String> login() {

        return ResponseEntity.ok("");
    }
}


