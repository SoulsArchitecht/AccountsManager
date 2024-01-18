package ru.sshibko.AccountsManager.service;

import jakarta.validation.Valid;

import java.util.Collection;

public interface CRUDService<T> {

    T getById(Long id);
    Collection<T> getAll();
    void create(@Valid T item);
    void update(@Valid T item);
    void delete(Long id);

}
