package com.ontop.wallet.application.ports.out.persistence;

import com.ontop.wallet.application.core.domain.User;

public interface UserPersistencePort extends DatabaseRepository<User> {
    User findByUserId(Long userId);
}
