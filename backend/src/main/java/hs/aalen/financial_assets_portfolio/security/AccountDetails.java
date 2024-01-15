package hs.aalen.financial_assets_portfolio.security;

import hs.aalen.financial_assets_portfolio.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountDetails implements UserDetails {
    /** AccountDetails class that implements the interface UserDetails.
     * The class is used for authentication and uses the user credentials
     * that are stored in the database as objects of class "Account".
     * ROLE_PREFIX: A constant prefix used to meet dependencies of spring security
     * account:     The account object that tries to authenticate.
     */


    public static final String ROLE_PREFIX = "ROLE_";

    private final Account account;

    /* CONSTRUCTOR */
    public AccountDetails(Account account) {
        this.account = account;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /** Method that returns authorities i.e. Roles to the Authenticator.
         * Role prefix is added to match requirements of Spring security.
         */
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + this.account.getRole()));
        return list;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    /* The following methods are overridden and permanently set
    to true as their functionality is not implemented. If not set to true, they will
    lock the user account from authentication.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
