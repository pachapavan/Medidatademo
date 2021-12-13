import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPage, Page } from '../page.model';
import { PageService } from '../service/page.service';

@Component({
  selector: 'jhi-page-update',
  templateUrl: './page-update.component.html',
})
export class PageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    modelId: [],
    pageId: [],
    type: [],
    fullScreen: [],
    history: [],
  });

  constructor(protected pageService: PageService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ page }) => {
      this.updateForm(page);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const page = this.createFromForm();
    if (page.id !== undefined) {
      this.subscribeToSaveResponse(this.pageService.update(page));
    } else {
      this.subscribeToSaveResponse(this.pageService.create(page));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(page: IPage): void {
    this.editForm.patchValue({
      id: page.id,
      name: page.name,
      description: page.description,
      modelId: page.modelId,
      pageId: page.pageId,
      type: page.type,
      fullScreen: page.fullScreen,
      history: page.history,
    });
  }

  protected createFromForm(): IPage {
    return {
      ...new Page(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      modelId: this.editForm.get(['modelId'])!.value,
      pageId: this.editForm.get(['pageId'])!.value,
      type: this.editForm.get(['type'])!.value,
      fullScreen: this.editForm.get(['fullScreen'])!.value,
      history: this.editForm.get(['history'])!.value,
    };
  }
}
