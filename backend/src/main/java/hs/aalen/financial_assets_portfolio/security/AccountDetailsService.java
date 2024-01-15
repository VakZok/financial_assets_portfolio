package hs.aalen.financial_assets_portfolio.security;

import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {
    /** AccountDetailsService class implementing UserDetailsService.
     * The class is used to connect the backend authentication with the
     * database repository where user credentials are stored.
     * accountRepo: The repository where user credentials are stored
     */
    private final AccountRepository accountRepo;

    /* CONSTRUCTOR */
    private AccountDetailsService(AccountRepository accountRepo){
        this.accountRepo = accountRepo;
    }

    /* method to load the user from the Account Repository for the login process */
    @Override
    public AccountDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = this.accountRepo.findByUsernameIgnoreCase(userName);
        if (account == null) {
            throw new UsernameNotFoundException("Der Account wurde nicht gefunden");
        }
        return new AccountDetails(account);
    }
}
