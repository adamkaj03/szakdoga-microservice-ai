import { Component, OnInit } from '@angular/core';
import { ArchiveService } from '../services/archive.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-new-archive',
  templateUrl: './new-archive.component.html',
  styleUrls: ['./new-archive.component.css']
})
export class NewArchiveComponent implements OnInit{

  selectedYear: number | null = null;
  archiveYears: number[] = [];

  constructor(private archiveService: ArchiveService, private router: Router, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    const currentYear = new Date().getFullYear();
    this.archiveYears = Array.from({ length: 20 }, (_, i) => currentYear - i);
  }

  public startArchive() {
    if (this.selectedYear) {
      this.archiveService.createArchive(this.selectedYear).subscribe({
        next: () => {
          this.router.navigateByUrl("/"); 
          this.toastr.success("Archiválás sikeres!")
        },
        error: (err) => {
          console.error("Archiválás hiba:", err);
          alert('Hiba történt az archiválás során.');
        }
      });
    }
  }
  
}
