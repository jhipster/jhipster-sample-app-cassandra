import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 'cedabc6f-ec82-4ade-9a49-ede691896f98',
  login: 'VfH',
};

export const sampleWithPartialData: IUser = {
  id: '224d773b-2400-4cb7-87c3-a2869b102209',
  login: 'Aug',
};

export const sampleWithFullData: IUser = {
  id: 'b98cc4a1-f30b-49a8-91ce-bbe453675d6e',
  login: 'CWG@oV\\^E9',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
