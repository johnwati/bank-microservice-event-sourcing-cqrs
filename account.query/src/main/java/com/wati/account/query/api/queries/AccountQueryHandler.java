package com.wati.account.query.api.queries;

import com.wati.account.query.api.dto.EqualityType;
import com.wati.account.query.domain.entity.AccountTransaction;
import com.wati.account.query.domain.repository.AccountRepository;
import com.wati.account.query.domain.entity.BankAccount;
import com.wati.account.query.domain.repository.AccountTransactionRepository;
import com.wati.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountQueryHandler  implements QueryHandler{

    private final AccountRepository accountRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    @Autowired
    public AccountQueryHandler(AccountRepository accountRepository, AccountTransactionRepository accountTransactionRepository) {
        this.accountRepository = accountRepository;
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsByIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());
        if(bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccount  = accountRepository.findByAccountHolder(query.getAccountHolder());
        if(bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;

    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        List<BaseEntity> bankAccountsList = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());

        return bankAccountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByAccountNumber query) {
        var bankAccount  = accountRepository.findByAccountNumber(query.getAccountNumber());
        if(bankAccount.isEmpty()){
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindTransactionByAccountId query) {
        Iterable<AccountTransaction>  accountTransactions  = accountTransactionRepository.findByAccountId(query.getAccountId());
        List<BaseEntity> accountTransactionList = new ArrayList<>();
        accountTransactions.forEach(accountTransactionList::add);
        return accountTransactionList;
    }
}
