package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.domain.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public Message saveMessage(Message message){
        return message;
    }
}
