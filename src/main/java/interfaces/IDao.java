package interfaces;

import java.util.List;
import java.util.Optional;

public interface IDao<T> {
        void save(T t);
        void update(T t);
        void delete(T t);
        Optional<T> findById(int id);
        List<T>getAll();

}
