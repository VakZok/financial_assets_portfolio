package hs.aalen.financial_assets_portfolio.web;

import hs.aalen.financial_assets_portfolio.domain.Message;
import hs.aalen.financial_assets_portfolio.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class HelloController {

    private final MessageService messageService;

    public HelloController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<>("hello world", HttpStatus.OK);
    }

    @GetMapping(value = "/message-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMessageList(){
        return new ResponseEntity<Object>(msessageService.getMessageList(), HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<Message> postMessage(@RequestBody Map<String, String> payload){
        Message message = new Message();
        message.setMessage(payload.get("messageContent"));
        message.setSecondMessage(payload.get("secondMessage"));
        return new ResponseEntity<>(messageService.saveMessage(message), HttpStatus.OK);
    }
}
