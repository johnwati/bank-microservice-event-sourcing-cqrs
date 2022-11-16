package com.wati.account.query.domain.repository;

import com.wati.account.query.domain.entity.BankAccount;
import com.wati.cqrs.core.domain.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountHolder(String accountHolder);
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    List<BaseEntity> findByBalanceLessThan(double balance);
    Optional<BankAccount> findByAccountNumber(Integer accountNumber);
}
