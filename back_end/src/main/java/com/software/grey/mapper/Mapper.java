package com.software.grey.mapper;

public interface Mapper <T, O> {
    O mapTo (T entity);
    T mapFrom (O dto);
}
