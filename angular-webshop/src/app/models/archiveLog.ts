/**
 * api kérésnél visszakapott ArchiveLog-t reprezentálja, a szükséges adattagokkal
 */
export interface ArchiveLog{
  id: number;
  archiveYear: number;
  status: string;
  archivedCount: number; 
  startTime: string;
  endTime: string;
}
