import { Cinema } from "../../backend/classes/general/Cinema/cinema/cinema";

export type CinemaGroup = { location: string; cinemas: Cinema[] };

export function groupCinemasByLocation (
    cinemas: Cinema[],
  opts: { sortLocations?: boolean; sortCinemas?: boolean; fallback?: string } = {}
): CinemaGroup[] {
  const { sortLocations = true, sortCinemas = true, fallback = 'Other' } = opts;

  const map = new Map<string, Cinema[]>();
  for (const c of cinemas ?? []) {
    const loc = c.location?.trim() || fallback;
    if (!map.has(loc)) map.set(loc, []);
    map.get(loc)!.push(c);
  }

  let groups: CinemaGroup[] = Array.from(map, ([location, list]) => ({
    location,
    cinemas: sortCinemas ? [...list].sort((a, b) => a.name.localeCompare(b.name)) : list,
  }));

  if (sortLocations) {
    groups = groups.sort((a, b) => a.location.localeCompare(b.location));
  }
  
  return groups;
}
