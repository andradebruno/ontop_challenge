package com.ontop.wallet.adapters.out.repository;

import com.ontop.wallet.adapters.out.entity.UserDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserDataEntity, Long> {
}
