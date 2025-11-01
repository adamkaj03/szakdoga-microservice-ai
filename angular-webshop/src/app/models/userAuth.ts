/**
 * bejelentkezésnél és regisztrációnál visszakapott UserAuth-t reprezentálja, a szükséges adattagokkal
 */
export interface UserAuth{
  username: string;
  role: string;
  token: string;
}
