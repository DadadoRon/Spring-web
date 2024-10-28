package com.example.springweb.service;

import com.example.springweb.annotations.CacheConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@CacheConfig(cacheResolver = CacheConfiguration.CACHE_RESOLVER_NAME)
@RequiredArgsConstructor
public abstract class BaseService<T, ID> {

    private final JpaRepository<T, ID> repository;

    @Cacheable
    public List<T> findAll() {
        return repository.findAll();
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }
}
