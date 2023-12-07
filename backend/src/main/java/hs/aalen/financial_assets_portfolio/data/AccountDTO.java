package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import hs.aalen.financial_assets_portfolio.domain.Account;


@JsonFilter("accFilter")
public class AccountDTO {
    private String username;
    private String password;
    private String name;
    private String role;

    public AccountDTO(String username, String password, String name, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }


    public AccountDTO(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.name = account.getName();
        this.role = account.getRole();
    }

    public AccountDTO() {
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}
}
