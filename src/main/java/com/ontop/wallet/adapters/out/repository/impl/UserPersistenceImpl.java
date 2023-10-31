package com.ontop.wallet.adapters.out.repository.impl;


import com.ontop.wallet.adapters.out.entity.UserDataEntity;
import com.ontop.wallet.adapters.out.exception.NotFoundException;
import com.ontop.wallet.adapters.out.repository.UserJpaRepository;
import com.ontop.wallet.application.core.domain.User;
import com.ontop.wallet.application.ports.out.persistence.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPersistenceImpl implements UserPersistencePort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByUserId(Long userId) {
        Optional<UserDataEntity> userDataEntity = userJpaRepository.findById(userId);
        if (userDataEntity.isEmpty()) {
            String errorMessage = "User not found for ID=" + userId;
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return userDataEntity.get().toDomain();
    }

    @Override
    public User save(User obj) {
        throw new UnsupportedOperationException("Saving a User is not supported in this implementation.");

    }
}
