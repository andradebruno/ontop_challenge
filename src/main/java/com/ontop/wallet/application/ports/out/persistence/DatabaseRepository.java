package com.ontop.wallet.application.ports.out.persistence;

public interface DatabaseRepository<T> {
    T save(T obj);
}
