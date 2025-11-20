// src/main/java/ma/dentalTech/repository/common/CrudRepository.java
package ma.dentalTech.repository.common;

import ma.dentalTech.common.exceptions.DaoException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity) throws DaoException;
    void deleteById(ID id) throws DaoException;
}