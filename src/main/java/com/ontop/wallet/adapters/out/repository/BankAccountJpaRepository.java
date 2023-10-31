package com.ontop.wallet.adapters.out.repository;

import com.ontop.wallet.adapters.out.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountJpaRepository extends JpaRepository<BankAccountEntity, Long> {
}
