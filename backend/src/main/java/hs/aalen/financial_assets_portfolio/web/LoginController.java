package hs.aalen.financial_assets_portfolio.web;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public ResponseEntity<Object> login(Authentication authentication )  {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("accFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "username", "role", "name"));

            Account account = accountRepo.findByUsername(authentication.getName());
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setName(account.getName());
            accountDTO.setRole(account.getRole());

            ObjectMapper om = new ObjectMapper();
            String mappedObject = om.writer(filterProvider).writeValueAsString(accountDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}


