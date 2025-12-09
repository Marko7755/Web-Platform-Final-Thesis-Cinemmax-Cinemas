import { Component, OnInit, HostListener, ElementRef, Renderer2, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, DOCUMENT, isPlatformBrowser } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../backend/services/auth.service';
import { NotificationService } from '../../services/notification/notification.service';
import { Observable, of, shareReplay, switchMap } from 'rxjs';
import { DecodedToken } from '../../../backend/interfaces/decoded-token';

type AdminSubKey = 'film' | 'cinema' | 'hall' | 'seat' | 'show_type';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css'
})
export class NavigationComponent implements OnInit {

  currentUser: Observable<DecodedToken | null> | undefined;
  showMenu: boolean = false;

  // mobile/tablet state for Admin dropdown
  adminOpen: boolean = false;
  openSub: AdminSubKey | null = null;

  private smallScreen = false;
  private isBrowser = false;

  public userInfo$: Observable<any | null> = of(null);

  constructor(
    public authService: AuthService,
    private notificationService: NotificationService,
    private router: Router,
    private el: ElementRef,
    private renderer: Renderer2,
    @Inject(PLATFORM_ID) private platformId: Object,
    @Inject(DOCUMENT) private doc: Document
  ) { }

  ngOnInit(): void {
    this.userInfo$ = this.authService.currentUser$.pipe(
      switchMap(u => u ? this.authService.getCurrentUser() : of(null)),
      shareReplay(1)
    );
    this.authService.updateUserRoleFromToken();
    this.currentUser = this.authService.currentUser$;

    this.isBrowser = isPlatformBrowser(this.platformId);
    if (this.isBrowser) {
      this.updateScreenSize();
    } else {
      this.smallScreen = false; // server render default
    }
  }

  // Keep your method name
  toggleMenu(): void {
    this.showMenu = !this.showMenu;
    this.updateScrollLock();

    if (!this.showMenu) {
      this.adminOpen = false;
      this.openSub = null;
    }
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.authService.localLogout();
        this.notificationService.logoutMessageSuccess(
          "Successfully logged out",
          "You've been successfully logged out!"
        );
        this.authService.updateUserRoleFromToken();

      },
      error: (err) => {
        this.notificationService.errorNotification(
          "Logout error",
          "There was a server error while logging out"
        );
        console.error(err);
      }
    });
  }

  handleLoginLogout(): void {
    if (this.isLoggedIn) {
      this.notificationService.confirmCancelNotification("logout", () => {
        this.logout();
      });
    } else {
      this.router.navigate(['/login']);
    }
  }


  closeUserMenu(toggle: HTMLInputElement, dropdownEl: HTMLElement) {
    // dodaj 'closing' da prebije :hover pravila na tren
    dropdownEl.classList.add('closing');
    toggle.checked = false;

    // ukloni klasu nakon kratkog vremena (npr. 200 ms)
    setTimeout(() => dropdownEl.classList.remove('closing'), 200);
  }


  // ===== mobile/tablet admin menu helpers =====

  toggleAdmin(): void {
    if (!this.smallScreen) return; // desktop uses hover; ignore clicks
    this.adminOpen = !this.adminOpen;
    if (!this.adminOpen) this.openSub = null;
  }

  toggleSub(key: AdminSubKey): void {
    if (!this.smallScreen) return; // desktop uses hover flyouts
    this.openSub = this.openSub === key ? null : key;
  }

  closeAll(): void {
    this.showMenu = false;
    this.adminOpen = false;
    this.openSub = null;
    this.updateScrollLock();
  }

  // Close admin dropdown on mobile when clicking outside of it
  @HostListener('document:click', ['$event'])
  onDocClick(ev: MouseEvent): void {
    if (!this.isBrowser || !this.smallScreen) return;
    const dropdown = this.el.nativeElement.querySelector('.dropdown');
    const clickedInside = dropdown && dropdown.contains(ev.target as Node);
    if (!clickedInside) {
      this.adminOpen = false;
      this.openSub = null;
    }
  }

  // Esc to close any open mobile UI
  @HostListener('document:keydown.escape')
  onEsc(): void {
    if (!this.isBrowser) return;
    if (this.smallScreen) this.closeAll();
  }

  @HostListener('window:resize')
  onResize(): void {
    if (!this.isBrowser) return;
    this.updateScreenSize();
    // Reset hover/tap state on breakpoint changes
    if (!this.smallScreen) {
      this.adminOpen = false;
      this.openSub = null;
      this.updateScrollLock();
    }
  }

  private updateScreenSize(): void {
    if (!this.isBrowser) return;
    const win = this.doc.defaultView; // safer than global window
    this.smallScreen = !!win && win.matchMedia('(max-width: 991px)').matches;
  }

  private updateScrollLock(): void {
    if (!this.isBrowser) return;
    const body = this.doc.body;
    if (!body) return;
    if (this.showMenu) {
      this.renderer.addClass(body, 'no-scroll');
    } else {
      this.renderer.removeClass(body, 'no-scroll');
    }
  }
}
