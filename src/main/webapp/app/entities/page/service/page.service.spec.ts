import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPage, Page } from '../page.model';

import { PageService } from './page.service';

describe('Page Service', () => {
  let service: PageService;
  let httpMock: HttpTestingController;
  let elemDefault: IPage;
  let expectedResult: IPage | IPage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      modelId: 'AAAAAAA',
      pageId: 0,
      type: 'AAAAAAA',
      fullScreen: false,
      history: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Page', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Page()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Page', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
          modelId: 'BBBBBB',
          pageId: 1,
          type: 'BBBBBB',
          fullScreen: true,
          history: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Page', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          description: 'BBBBBB',
          type: 'BBBBBB',
          fullScreen: true,
        },
        new Page()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Page', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
          modelId: 'BBBBBB',
          pageId: 1,
          type: 'BBBBBB',
          fullScreen: true,
          history: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Page', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPageToCollectionIfMissing', () => {
      it('should add a Page to an empty array', () => {
        const page: IPage = { id: 'ABC' };
        expectedResult = service.addPageToCollectionIfMissing([], page);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(page);
      });

      it('should not add a Page to an array that contains it', () => {
        const page: IPage = { id: 'ABC' };
        const pageCollection: IPage[] = [
          {
            ...page,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addPageToCollectionIfMissing(pageCollection, page);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Page to an array that doesn't contain it", () => {
        const page: IPage = { id: 'ABC' };
        const pageCollection: IPage[] = [{ id: 'CBA' }];
        expectedResult = service.addPageToCollectionIfMissing(pageCollection, page);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(page);
      });

      it('should add only unique Page to an array', () => {
        const pageArray: IPage[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '9aad666e-65c4-4c3c-8a02-dc82627231a8' }];
        const pageCollection: IPage[] = [{ id: 'ABC' }];
        expectedResult = service.addPageToCollectionIfMissing(pageCollection, ...pageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const page: IPage = { id: 'ABC' };
        const page2: IPage = { id: 'CBA' };
        expectedResult = service.addPageToCollectionIfMissing([], page, page2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(page);
        expect(expectedResult).toContain(page2);
      });

      it('should accept null and undefined values', () => {
        const page: IPage = { id: 'ABC' };
        expectedResult = service.addPageToCollectionIfMissing([], null, page, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(page);
      });

      it('should return initial array if no Page is added', () => {
        const pageCollection: IPage[] = [{ id: 'ABC' }];
        expectedResult = service.addPageToCollectionIfMissing(pageCollection, undefined, null);
        expect(expectedResult).toEqual(pageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
