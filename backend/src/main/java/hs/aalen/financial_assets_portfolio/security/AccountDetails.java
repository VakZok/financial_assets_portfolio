package hs.aalen.financial_assets_portfolio.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//
//public class AccountDetails implements UserDetails {
//    public static final String ROLE_PREFIX = "ROLE_";
//
//    private Account account;
//
//    /*Constructor*/
//    public AccountDetails(Account account) {
//        this.account = account;
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
//        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + this.account.getRole()));
//        return list;
//    }
//
//    @Override
//    public String getPassword() {
//        return account.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return account.getUserName();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
