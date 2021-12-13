jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PageService } from '../service/page.service';
import { IPage, Page } from '../page.model';

import { PageUpdateComponent } from './page-update.component';

describe('Page Management Update Component', () => {
  let comp: PageUpdateComponent;
  let fixture: ComponentFixture<PageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageService: PageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PageUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageService = TestBed.inject(PageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const page: IPage = { id: 'CBA' };

      activatedRoute.data = of({ page });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(page));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Page>>();
      const page = { id: 'ABC' };
      jest.spyOn(pageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ page });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: page }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageService.update).toHaveBeenCalledWith(page);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Page>>();
      const page = new Page();
      jest.spyOn(pageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ page });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: page }));
      saveSubject.complete();

      // THEN
      expect(pageService.create).toHaveBeenCalledWith(page);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Page>>();
      const page = { id: 'ABC' };
      jest.spyOn(pageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ page });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageService.update).toHaveBeenCalledWith(page);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
