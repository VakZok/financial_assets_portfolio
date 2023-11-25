package hs.aalen.financial_assets_portfolio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public class AccountDetailsService implements UserDetailsService {
//
//    @Autowired
//    private AccountRepository accountRepo;
//
//    /* method to load the user from the Account Repository for the login process */
//    @Override
//    public AccountDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Account account = accountRepo.findByUserName(userName);
//        if (account == null) {
//            throw new UsernameNotFoundException("Der Account wurde nicht gefunden");
//        }
//        return new AccountDetails(account);
//    }
//}
