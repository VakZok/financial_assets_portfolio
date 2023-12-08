package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.AccountDTO;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service

public class AccountService {

    public static final int STRING_MAX_LENGTH = 30;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO getAccount(String username) throws NoSuchElementException {
        Account account = accountRepository.findByUsernameIgnoreCase(username);
        if (account == null){
            throw new NoSuchElementException("Kein Account mit diesem Benutzernamen vorhanden");
        }
        return new AccountDTO(account);
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public ArrayList<AccountDTO> getAccounts() {
        ArrayList<Account> accountList = (ArrayList<Account>) accountRepository.findAll();
        if (accountList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ArrayList<>(accountList.stream().map(AccountDTO::new).toList());
    }

    public void addAccount(String username, AccountDTO accountDTO) throws FormNotValidException {
        ArrayList<ExceptionDTO> exceptionDTOList = this.validateAddForm(accountDTO);
        if (exceptionDTOList.isEmpty()){
            Account account = new Account (accountDTO);
            account.setRole(account.getRole().toUpperCase());
            saveAccount(account);
        }else {
            throw new FormNotValidException("Formfehler", exceptionDTOList);
        }
    }
    @Transactional
    public void updateAccount(String username, AccountDTO accountDTO, Authentication authentication) throws FormNotValidException{
        if(!username.equals(authentication.getName())){
            Account existingAccount = accountRepository.findByUsernameIgnoreCase(username);
            ArrayList<ExceptionDTO> exceptionDTOList = this.validateUpdateForm(username, accountDTO);

            if (exceptionDTOList.isEmpty()){
                if(!existingAccount.getUsername().equals(accountDTO.getUsername()) && !accountDTO.getUsername().isEmpty()){
                    existingAccount = new Account(
                            accountDTO.getUsername(),
                            existingAccount.getPassword(),
                            existingAccount.getName(),
                            existingAccount.getRole());
                    accountRepository.deleteByUsername(username);
                }
                if(!existingAccount.getName().equals(accountDTO.getName()) && !accountDTO.getName().isEmpty()){
                    existingAccount.setName(accountDTO.getName());
                }
                if(!existingAccount.getPassword().equals(accountDTO.getPassword()) && !accountDTO.getPassword().isEmpty()){
                    existingAccount.setPassword(accountDTO.getPassword());
                }
                if(!existingAccount.getRole().equalsIgnoreCase(accountDTO.getRole()) && !accountDTO.getRole().isEmpty()){
                    existingAccount.setRole(accountDTO.getRole().toUpperCase());
                }
                saveAccount(existingAccount);
            }else {
                throw new FormNotValidException("Formfehler", exceptionDTOList);
            }
        }

    }
    @Transactional
    public void deleteAccountByUsername(String username, String authenticatedUsername) {
        if(!username.equals(authenticatedUsername)){
            accountRepository.deleteByUsername(username);
        }
    }

    public boolean checkAccountExists(String username) {
        Account account = accountRepository.findByUsernameIgnoreCase(username);
        return account != null && account.getUsername().equalsIgnoreCase(username);
    }

    public ArrayList<ExceptionDTO> validateAddForm(AccountDTO accountDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();

        if (checkAccountExists(accountDTO.getUsername())){
            exceptions.add(new ExceptionDTO("username", "Account mit diesem Benutzername bereits vorhanden"));
        }
        if(accountDTO.getUsername() == null || accountDTO.getUsername().isEmpty()){
            exceptions.add(new ExceptionDTO("username", "Bitte füllen Sie den Benutzername aus"));
        }
        if(accountDTO.getName() == null || accountDTO.getName().isEmpty()){
            exceptions.add(new ExceptionDTO("name", "Bitte tragen Sie einen Namen ein"));
        }
        if(accountDTO.getRole() == null || accountDTO.getRole().isEmpty()){
            exceptions.add(new ExceptionDTO("role", "Bitte wählen Sie eine Rolle"));
        }
        if(accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty()){
            exceptions.add(new ExceptionDTO("password", "Bitte tragen Sie ein Passwort ein"));
        }
        if(accountDTO.getUsername().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "username", "Der Benutzername darf nicht länger als 255 Zeichen sein"));
        }
        if(accountDTO.getName().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "name", "Der Name darf nicht länger als 255 Zeichen sein"));
        }
        if(!(accountDTO.getRole().equals("ADMIN") || accountDTO.getRole().equals("USER"))){
            exceptions.add(new ExceptionDTO(
                    "role", "Rolle nicht valide"));
        }
        if(accountDTO.getPassword().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "password", "Das Passwort darf nicht länger als 255 Zeichen sein"));
        }
        return exceptions;
    }

    public ArrayList<ExceptionDTO> validateUpdateForm(String initUsername, AccountDTO accountDTO){
        Account account = accountRepository.findByUsernameIgnoreCase(initUsername);
        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();

        if (!(accountDTO.getUsername().equalsIgnoreCase(account.getUsername())) && !accountDTO.getUsername().isEmpty()){
            if (checkAccountExists(accountDTO.getUsername())){
                exceptions.add(new ExceptionDTO("username", "Account mit diesem Benutzernamen bereits vorhanden"));
            }
            if((accountDTO.getUsername() == null || accountDTO.getUsername().isEmpty())){
                exceptions.add(new ExceptionDTO("username", "Bitte füllen Sie den Benutzername aus"));
            }
            if(accountDTO.getUsername().length() > STRING_MAX_LENGTH){
                exceptions.add(new ExceptionDTO(
                        "username", "Der Benutzername darf nicht länger als 30 Zeichen sein"));
            }
        }

        if(!(accountDTO.getName().equalsIgnoreCase(account.getName())) && !accountDTO.getName().isEmpty()){
            if(accountDTO.getName().length() > STRING_MAX_LENGTH){
                exceptions.add(new ExceptionDTO(
                        "name", "Der Name darf nicht länger als 30 Zeichen sein"));
            }
        }

        if(! (accountDTO.getRole().equals("ADMIN") || accountDTO.getRole().equals("USER"))){
            exceptions.add(new ExceptionDTO(
                    "role", "Rolle nicht valide"));
        }

        if(!(accountDTO.getPassword().equalsIgnoreCase(account.getPassword())) && !accountDTO.getPassword().isEmpty()){
            if(accountDTO.getPassword().length() > STRING_MAX_LENGTH){
                exceptions.add(new ExceptionDTO(
                        "password", "Das Passwort darf nicht länger als 30 Zeichen sein"));
            }
        }
        return exceptions;
    }

}
