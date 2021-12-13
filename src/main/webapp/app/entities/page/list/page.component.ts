import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPage } from '../page.model';
import { PageService } from '../service/page.service';
import { PageDeleteDialogComponent } from '../delete/page-delete-dialog.component';

@Component({
  selector: 'jhi-page',
  templateUrl: './page.component.html',
})
export class PageComponent implements OnInit {
  pages?: IPage[];
  isLoading = false;

  constructor(protected pageService: PageService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pageService.query().subscribe(
      (res: HttpResponse<IPage[]>) => {
        this.isLoading = false;
        this.pages = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPage): string {
    return item.id!;
  }

  delete(page: IPage): void {
    const modalRef = this.modalService.open(PageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.page = page;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
