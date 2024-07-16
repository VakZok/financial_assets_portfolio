package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import hs.aalen.financial_assets_portfolio.domain.Account;


@JsonFilter("accFilter")
public class AccountDTO {
    /**
     * A Data Transfer Object (DTO) representing an account with essential information.
     * This class is used to transfer user information between frontend and backend.
     * username: The username of the account.
     * password: The password associated with the account.
     * name:     The name associated with the account.
     * role:     The role or authorization level of the account.
     */

    private String username;
    private String password;
    private String name;
    private String role;


    /* CONSTRUCTORS */
    public AccountDTO(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.name = account.getName();
        this.role = account.getRole();
    }


    public AccountDTO() {
    }

    /* GETTERS AND SETTERS */

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}
}
