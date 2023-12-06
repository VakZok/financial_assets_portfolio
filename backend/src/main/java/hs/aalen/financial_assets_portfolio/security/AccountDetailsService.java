package hs.aalen.financial_assets_portfolio.security;

import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepo;
    private AccountDetailsService(AccountRepository accountRepo){
        this.accountRepo = accountRepo;
    }

    /* method to load the user from the Account Repository for the login process */
    @Override
    public AccountDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = this.accountRepo.findByUsernameIgnoreCase(userName);
        if (account == null) {
            System.out.println("test");
            throw new UsernameNotFoundException("Der Account wurde nicht gefunden");
        }
        return new AccountDetails(account);
    }
}
