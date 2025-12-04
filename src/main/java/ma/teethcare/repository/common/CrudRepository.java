package ma.teethcare.repository.common;

import java.util.List;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    T findById(ID id);
    void create(T newElement);
    void update(T newValuesElement);
    void delete(T element);
    void deleteById(ID id);
}