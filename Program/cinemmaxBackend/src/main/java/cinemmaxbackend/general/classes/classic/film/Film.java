package cinemmaxbackend.general.classes.classic.film;

import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import cinemmaxbackend.general.classes.commands.film.FilmCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Films")

public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "about")
    private String about;

    @Column(name = "imdb_rating")
    private BigDecimal imdbRating;

    @Column(name = "age_rate")
    private Integer ageRate;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "cinema_release_date")
    private LocalDate cinemaReleaseDate;

    @Column(name = "cinema_end_date")
    private LocalDate cinemaEndDate;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ShowTime> showTimes;

    @ManyToMany
    @JoinTable(
            name = "Film_Roles",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @JsonManagedReference
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "Film_Directors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    @JsonManagedReference
    private List<Director> directors;



}
