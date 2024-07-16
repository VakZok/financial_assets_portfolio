package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.AccountDTO;
import jakarta.persistence.*;

@Entity
public class Account {
    /**
     * The entity class for Account, containing the account
     * user credentials that are stored in the database.
     * username: The username of the account.
     * password: The password associated with the account.
     * name:     The name associated with the account.
     * role:     The role or authorization level of the account.
     */

    @Id
    private String username;
    private String password;
    private String name;
    private String role;


    /* CONSTRUCTORS */

    public Account(String username, String password, String name, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Account(AccountDTO accountDTO) {
        this.username = accountDTO.getUsername();
        this.password = accountDTO.getPassword();
        this.name = accountDTO.getName();
        this.role = accountDTO.getRole();
    }


    public Account() {
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
