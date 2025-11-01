import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { ArchiveLog } from '../models/archiveLog';
import { ArchiveService } from '../services/archive.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-archive-page',
  templateUrl: './archive-page.component.html',
  styleUrls: ['./archive-page.component.css']
})
export class ArchivePageComponent {
  public archiveLogs: Observable<ArchiveLog[]> = new Observable<ArchiveLog[]>();


  constructor(private archiveService: ArchiveService, private router: Router) {
  }

  public getArchiveLogs() {
    this.archiveLogs = this.archiveService.getArchiveLogs();
  }

  ngOnInit(): void {
    this.getArchiveLogs();
  }

  public navigateToArchivePage() {
    this.router.navigateByUrl("new-archive");
  }
}
