<<<<<<< HEAD
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
=======
package ma.teethcare.repository.common;

import java.util.List;

public interface CrudRepository<T, ID> {

    List<T> findAll();

    T findById(ID id);

    void create(T patient);

    void update(T patient);

    void delete(T patient);

    void deleteById(ID id);
}
>>>>>>> 22a2bab4b92f1984ef147fa55ab21d9ac82b7880
