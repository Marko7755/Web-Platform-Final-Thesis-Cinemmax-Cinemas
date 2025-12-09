package cinemmaxbackend.web.repositories.ShowType;

import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowTypeRepository extends JpaRepository<ShowType, Long> {
    Optional<ShowType> findByType(String type);
}
