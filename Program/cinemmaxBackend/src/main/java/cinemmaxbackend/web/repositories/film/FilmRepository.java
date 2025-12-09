package cinemmaxbackend.web.repositories.film;

import cinemmaxbackend.general.classes.classic.film.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByNameIgnoreCase(String name);

    @Query(value = """
            SELECT *
            FROM Films
            WHERE cinema_release_date >  CURRENT_DATE
            ORDER BY cinema_release_date
            """, nativeQuery = true)
    List<Film> getUpcoming();

    List<Film> findAllByOrderByCinemaReleaseDateAsc();

    @Query(value = "SELECT * FROM Films WHERE cinema_release_date <= CURRENT_DATE AND cinema_end_date >= CURRENT_DATE ORDER BY cinema_release_date", nativeQuery = true)
    List<Film> getAllReleased();

    @Query(value = """
            SELECT *
            FROM Films
            WHERE cinema_end_date >= CURRENT_DATE AND CURRENT_DATE BETWEEN DATEADD('DAY', -10, cinema_end_date) AND cinema_end_date
            ORDER BY cinema_end_date
            """, nativeQuery = true)
    List<Film> getEndingIn10Days();

    @Query(value = """
            SELECT *
            FROM Films
            ORDER BY imdb_rating DESC
            """, nativeQuery = true)
    List<Film> getBestRatedForAdmin();

    @Query(value = """
            SELECT *
            FROM Films
            WHERE cinema_release_date <= CURRENT_DATE AND cinema_end_date >= CURRENT_DATE
            ORDER BY imdb_rating DESC
            """, nativeQuery = true)
    List<Film> getBestRatedReleased();

    @Query(
            value = "SELECT DISTINCT TRIM(genre) FROM Films ORDER BY TRIM(genre)",
            nativeQuery = true
    )
    List<String> getAllDistinctGenres();

    @Query(value = """
    SELECT *
    FROM FILMS
    WHERE cinema_release_date <= CURRENT_DATE AND cinema_end_date >= CURRENT_DATE AND genre = :genre
    
""", nativeQuery = true)
    List<Film> getByGenreIgnoreCaseReleased(@Param("genre") String genre);

    List<Film> getByGenreIgnoreCase(String genre);

}
