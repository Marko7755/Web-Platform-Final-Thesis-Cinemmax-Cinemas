import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Pipe({ name: 'youtubeEmbedUrl', standalone: true })
export class YoutubeEmbedUrlPipe implements PipeTransform {
  constructor(private s: DomSanitizer) {}

  transform(rawUrl: string | null | undefined): SafeResourceUrl {
    if (!rawUrl) return '' as unknown as SafeResourceUrl;

    try {
      const url = new URL(rawUrl);
      let id = '';

      if (url.hostname.includes('youtu.be')) {
        id = url.pathname.slice(1);
      } else if (url.pathname.includes('/embed/')) {
        id = url.pathname.split('/embed/')[1]?.split('/')[0] ?? '';
      } else if (url.searchParams.get('v')) {
        id = url.searchParams.get('v')!;
      }

      if (!id) return this.s.bypassSecurityTrustResourceUrl(rawUrl);

      const embed = `https://www.youtube-nocookie.com/embed/${id}`;
      return this.s.bypassSecurityTrustResourceUrl(embed);
    } catch {
      return this.s.bypassSecurityTrustResourceUrl(rawUrl);
    }
  }
}
