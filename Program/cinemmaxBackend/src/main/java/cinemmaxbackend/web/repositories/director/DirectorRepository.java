package cinemmaxbackend.web.repositories.director;

import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    @Query("""
  SELECT d FROM Director d
  WHERE
    LOWER(CONCAT(d.name, ' ', d.surname)) LIKE LOWER(CONCAT('%', :input, '%'))
    OR LOWER(CONCAT(d.surname, ' ', d.name)) LIKE LOWER(CONCAT('%', :input, '%'))
""")
    List<Director> findByInput(@Param("input") String input);

    Optional<Director> findByNameAndSurname(String name, String surname);
}
