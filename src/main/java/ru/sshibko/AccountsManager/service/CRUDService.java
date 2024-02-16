package ru.sshibko.AccountsManager.service;

import jakarta.validation.Valid;

import java.util.Collection;

public interface CRUDService<T> {

    T getById(Long id);
    Collection<T> getAll();
    T create(@Valid T item);
    T update(@Valid Long id, T item);
    void delete(Long id);

}
