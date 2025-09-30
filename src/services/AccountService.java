package services;

import entities.Account;
import enums.AccountType;
import repositories.AccountRepositoryImpl;
import repositories.AccoutRepository;

import java.util.UUID;

public class AccountService {
    private final AccoutRepository accoutRepository = new AccountRepositoryImpl();

    public Account createAccount(String accountNumber, AccountType type, UUID clientId,int createdBy){
        Account account = new Account(accountNumber, type, clientId, createdBy);
        if(accoutRepository.createAccount(account)){
            return account;
        }
        return null;
    }
}
