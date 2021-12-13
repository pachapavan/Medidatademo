import { IObjectContainingString } from 'app/entities/object-containing-string/object-containing-string.model';
import { IFlexBox } from 'app/entities/flex-box/flex-box.model';

export interface IPage {
  id?: string;
  name?: string | null;
  description?: string | null;
  modelId?: string | null;
  pageId?: number | null;
  type?: string | null;
  fullScreen?: boolean | null;
  history?: string | null;
  genericObjectsLists?: IObjectContainingString[] | null;
  flexBoxes?: IFlexBox[] | null;
}

export class Page implements IPage {
  constructor(
    public id?: string,
    public name?: string | null,
    public description?: string | null,
    public modelId?: string | null,
    public pageId?: number | null,
    public type?: string | null,
    public fullScreen?: boolean | null,
    public history?: string | null,
    public genericObjectsLists?: IObjectContainingString[] | null,
    public flexBoxes?: IFlexBox[] | null
  ) {
    this.fullScreen = this.fullScreen ?? false;
  }
}

export function getPageIdentifier(page: IPage): string | undefined {
  return page.id;
}
