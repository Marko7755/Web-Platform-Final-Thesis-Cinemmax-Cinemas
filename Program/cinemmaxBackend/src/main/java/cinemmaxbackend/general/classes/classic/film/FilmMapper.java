package cinemmaxbackend.general.classes.classic.film;

import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import cinemmaxbackend.general.classes.commands.film.FilmCommand;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmMapper {

    public Long id;

    private void copyScalars(FilmCommand c, Film f) {
        f.setName(c.getName());
        f.setGenre(c.getGenre());
        f.setAbout(c.getAbout());
        f.setImdbRating(c.getImdbRating());
        f.setAgeRate(c.getAgeRate());
        f.setDuration(c.getDuration());
        f.setImageUrl(c.getImageUrl());
        f.setTrailerUrl(c.getTrailerUrl());
        f.setCinemaReleaseDate(c.getCinemaReleaseDate());
        f.setCinemaEndDate(c.getCinemaEndDate());
    }

    public Film mapForCreate(FilmCommand cmd, List<Actor> actors, List<Director> directors) {
        Film f = new Film();
        copyScalars(cmd, f);
        f.setActors(new ArrayList<>(actors));
        f.setDirectors(new ArrayList<>(directors));
        return f;
    }

    public void mapForUpdate(Film filmToEdit, FilmCommand cmd, List<Actor> actors, List<Director> directors) {
        copyScalars(cmd, filmToEdit);
        filmToEdit.getActors().clear();
        filmToEdit.getActors().addAll(actors);

        filmToEdit.getDirectors().clear();
        filmToEdit.getDirectors().addAll(directors);
    }


}
