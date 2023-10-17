package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.domain.Message;
import hs.aalen.financial_assets_portfolio.persistence.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getMessageList(){
        return messageRepository.findAll();
    }
}
