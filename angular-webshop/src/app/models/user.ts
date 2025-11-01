/**
 * api kérésnél visszakapott User-t reprezentálja, a szükséges adattagokkal
 */
export interface UserDTO{
  id: number;
  name: string;
  username: string;
  email: string;
  role: string;
}
