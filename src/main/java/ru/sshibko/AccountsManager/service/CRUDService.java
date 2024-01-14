package ru.sshibko.AccountsManager.service;

import java.util.Collection;

public interface CRUDService<T> {

    T getById(Long id);
    Collection<T> getAll();
    void create(T item);
    void update(T item);
    void delete(Long id);

}
