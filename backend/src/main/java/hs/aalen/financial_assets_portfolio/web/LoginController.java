package hs.aalen.financial_assets_portfolio.web;
import hs.aalen.financial_assets_portfolio.data.AccountDTO;
import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    private final AccountRepository accountRepo;
    public LoginController(AccountRepository accountRepo){
        this.accountRepo = accountRepo;
    }

    @GetMapping("/logins")
    public ResponseEntity<AccountDTO> login(Authentication authentication ) {
        Account account = accountRepo.findByUsername(authentication.getName());
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName(account.getName());
        accountDTO.setRole(account.getRole());
        return new ResponseEntity<>(accountDTO, JSON_HEADER, HttpStatus.OK);
    }
}


