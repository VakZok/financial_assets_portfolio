package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Message;
import org.springframework.data.repository.ListCrudRepository;

public interface MessageRepository extends ListCrudRepository<Message, Long> {

}
