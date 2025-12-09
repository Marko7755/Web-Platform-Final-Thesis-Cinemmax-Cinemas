package cinemmaxbackend.web.repositories.actor;

import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("""
  SELECT a FROM Actor a
  WHERE
    LOWER(CONCAT(a.name, ' ', a.surname)) LIKE LOWER(CONCAT('%', :input, '%'))
    OR LOWER(CONCAT(a.surname, ' ', a.name)) LIKE LOWER(CONCAT('%', :input, '%'))
""")
    List<Actor> findByInput(@Param("input") String input);

    Optional<Actor> findByNameAndSurname(String name, String surname);

}
