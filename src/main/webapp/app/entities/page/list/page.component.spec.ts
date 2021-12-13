import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PageService } from '../service/page.service';

import { PageComponent } from './page.component';

describe('Page Management Component', () => {
  let comp: PageComponent;
  let fixture: ComponentFixture<PageComponent>;
  let service: PageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PageComponent],
    })
      .overrideTemplate(PageComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PageService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.pages?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
