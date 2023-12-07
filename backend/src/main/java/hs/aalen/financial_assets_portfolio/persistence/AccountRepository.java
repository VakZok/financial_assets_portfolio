package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Account;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountRepository extends ListCrudRepository<Account, String> {

    Account findByUsernameIgnoreCase(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}

