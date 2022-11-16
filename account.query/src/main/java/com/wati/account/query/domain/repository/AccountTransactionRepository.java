package com.wati.account.query.domain.repository;

import com.wati.account.query.domain.entity.AccountTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, String> {

    List<AccountTransaction> findByAccountId(String accountId);

}
