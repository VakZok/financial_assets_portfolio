package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.AccountDTO;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountService {

    public static final int STRING_MAX_LENGTH = 255;

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
        return new ArrayList<AccountDTO>(accountList.stream().map(AccountDTO::new).toList());
    }

    public void addAccount(String username, AccountDTO accountDTO) throws FormNotValidException {
        ArrayList<ExceptionDTO> exceptionDTOList = this.validateForm(accountDTO);
        if (exceptionDTOList.isEmpty()){
            Account account = new Account (accountDTO);
            account.setRole(account.getRole().toUpperCase());
            saveAccount(account);
        }else {
            throw new FormNotValidException("Formfehler", exceptionDTOList);
        }
    }

    public void updateAccount(String username, AccountDTO accountDTO) throws FormNotValidException{
        Account existingAccount = accountRepository.findByUsernameIgnoreCase(username);
        ArrayList<ExceptionDTO> exceptionDTOList = this.validateForm(accountDTO);
        if (exceptionDTOList.isEmpty()){
            existingAccount.setUsername(accountDTO.getUsername());
            existingAccount.setName(accountDTO.getName());
            existingAccount.setRole(accountDTO.getRole().toUpperCase());
            existingAccount.setPassword(accountDTO.getPassword());
            saveAccount(existingAccount);
        }else {
            throw new FormNotValidException("Formfehler", exceptionDTOList);
        }
    }

    public void deleteAccountByUsername(String username) {
        accountRepository.deleteByUsername(username);
    }

    public boolean checkAccountExists(String username) {
        Account account = accountRepository.findByUsernameIgnoreCase(username);

        return account != null && account.getUsername().equalsIgnoreCase(username);
    }

    public ArrayList<ExceptionDTO> validateForm(AccountDTO accountDTO){
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
        if(accountDTO.getRole().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "role", "Die Rolle darf nicht länger als 255 Zeichen sein"));
        }
        if(accountDTO.getPassword().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "password", "Das Passwort darf nicht länger als 255 Zeichen sein"));
        }
        return exceptions;
    }
}
