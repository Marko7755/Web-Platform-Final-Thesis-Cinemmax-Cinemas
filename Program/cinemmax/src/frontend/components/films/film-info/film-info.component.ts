import { Component, OnInit } from '@angular/core';
import { FilmService } from '../../../services/film/film.service';
import { ActivatedRoute, ɵEmptyOutletComponent, RouterLink } from '@angular/router';
import { Film } from '../../../../backend/classes/general/Film/film/film';
import { CommonModule } from '@angular/common';
import { YoutubeEmbedUrlPipe } from "../../../pipes/SafeUrl/safe-url.pipe";
import { ShowTimeService } from '../../../services/show-time/show-time.service';
import { ShowTimeInterface } from '../../../../backend/interfaces/show-time';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { filter } from 'rxjs';
import { NotificationService } from '../../../services/notification/notification.service';
import { FormsModule } from '@angular/forms';
import { CinemaGroup, groupCinemasByLocation } from '../../../functions/group-cinemas-by-function';


type GroupedShowTimes = Array<{
  dateKey: string;
  dateLabel: string;
  items: Array<{
    filmName: string;
    showId: number;
    showTime: string;
    showType: { type: string };
    hall: { number: number; name: string; cinemaLocation: string, cinemaName: string };
  }>;
}>;


@Component({
  selector: 'app-film-info',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, YoutubeEmbedUrlPipe],
  templateUrl: './film-info.component.html',
  styleUrl: './film-info.component.css'
})

export class FilmInfoComponent implements OnInit {
  film: Film | null = null;
  showTimes: ShowTimeInterface[] = [];
  groupedShowTimes: GroupedShowTimes = [];
  allCinemas: Cinema[] = [];
  groupedCinemas: CinemaGroup[] = [];
  selectedCinemmaxName: string | null = null;
  currentDate: Date = new Date();


  constructor(private filmService: FilmService, private route: ActivatedRoute,
    private showTime: ShowTimeService, public cinemaService: CinemaService,
    private notificationService: NotificationService) { }

  parseDate(date: string) {
    return this.filmService.parseDate(date);
  }

  ngOnInit(): void {
    const filmId = this.route.snapshot.paramMap.get('filmId');
    if (filmId) {
      this.filmService.getById(+filmId).subscribe({
        next: (filmRes: Film) => {
          this.film = filmRes;
          /* console.log(this.film); */
        },
        error: (err) => {
          console.error('Error fetching film info:', err);
        }
      });

      /* this.cinemaService.selectedCinemmax$
        .pipe(filter((c): c is Cinema => c != null))
        .subscribe(cinema => {
          this.selectedCinemmax = cinema;
          this.showTime.getAllByFilmIdAndCinemaLocation(+id, cinema.location).subscribe({
            next: (showTimes: ShowTimeInterface[]) => {
              // Convert to embeddable URL if needed
              this.showTimes = showTimes;
              this.groupedShowTimes = this.buildGroupedShowTimes(showTimes);
              console.log(this.groupedShowTimes);
            },
            error: (err) => {
              console.error('Error fetching show times:', err);
            }
          });
        }); */

      this.cinemaService.selectedCinemmaxName$
        .pipe(filter((c): c is string => c != null))
        .subscribe(cinemaName => {
          this.selectedCinemmaxName = cinemaName;
          this.showTime.getAllByFilmIdAndCinemaLocation(+filmId, cinemaName).subscribe({
            next: (showTimes: ShowTimeInterface[]) => {
              // Convert to embeddable URL if needed
              this.showTimes = showTimes;
              showTimes.filter(sT => sT.showTime)
              this.groupedShowTimes = this.buildGroupedShowTimes(showTimes);
              console.log(this.groupedShowTimes);
              console.log(showTimes);
            },
            error: (err) => {
              console.error('Error fetching show times:', err);
            }
          });
        })
    }


    this.cinemaService.getAll().subscribe({
      next: (cinemas: Cinema[]) => {
        this.allCinemas = cinemas;
        this.groupedCinemas = groupCinemasByLocation(cinemas);
        this.groupedCinemas.sort((a, b) => a.location.localeCompare(b.location));
        this.groupedCinemas.forEach(group => group.cinemas.sort((a, b) => a.name.localeCompare(b.name)));
      },
      error: (err) => {
        this.notificationService.errorNotification("Error fetching cinemas", err.error?.error || "There was an error while fetching the cinemas!");
        console.error('Error fetching cinemas:', err);
      }
    });



  }

  isPast(showTime: string): boolean {
    const d = new Date(showTime);
    return d.getTime() < Date.now();
  }

  compareCinema = (a: Cinema | null, b: Cinema | null) => !!a && !!b && a.id === b.id;
  trackById = (_: number, c: Cinema) => c.id;


  private buildGroupedShowTimes(data: ShowTimeInterface[] = []): GroupedShowTimes {
    if (!data?.length) return [];
    // Parse "yyyy-MM-dd HH:mm:ss" safely (JS likes ISO with 'T')
    const parseShowTime = (s: string) => new Date(s);

    const now = new Date();

    // If you want to include *all of today* (even past times), set this to startOfToday:
    const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const lowerBound = startOfToday;
    /* const lowerBound = now; */ // only future times from *right now*

    // 1) filter out past
    const upcoming = data.filter(st => parseShowTime(st.showTime).getTime() >= lowerBound.getTime());

    // 2) sort ascending by time
    const sorted = [...upcoming].sort(
      (a, b) => parseShowTime(a.showTime).getTime() - parseShowTime(b.showTime).getTime()
    );

    // 3) group by yyyy-mm-dd
    const fmtWeekday = new Intl.DateTimeFormat('eng-us', { weekday: 'long' });
    const fmtDayMonth = new Intl.DateTimeFormat('hr-HR', { day: '2-digit', month: '2-digit' });

    const map = new Map<string, { dateLabel: string; items: GroupedShowTimes[number]['items'] }>();

    for (const st of sorted) {
      const d = parseShowTime(st.showTime);
      const key =
        `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
      const label = `${fmtWeekday.format(d).toUpperCase()} ${fmtDayMonth.format(d)}`;

      if (!map.has(key)) map.set(key, { dateLabel: label, items: [] });

      map.get(key)!.items.push({
        filmName: this.film?.name!,
        showId: st.id,
        showTime: st.showTime,
        showType: { type: st.showType.type },
        hall: {
          number: st.hall.number,
          name: st.hall.cinema.name,
          cinemaLocation: st.hall.cinema.location,
          cinemaName: st.hall.cinema.name,
        }
      });
    }

    // 4) to array
    return Array.from(map, ([dateKey, { dateLabel, items }]) => ({
      dateKey,
      dateLabel,
      items
    }));
  }



}
