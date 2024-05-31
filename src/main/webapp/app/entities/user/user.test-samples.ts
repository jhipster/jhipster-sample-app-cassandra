import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 'c8e9d4a0-b1ce-466f-a1e4-c38928a2dae5',
  login: 'lQ@4xZI7\\HLQgUJ\\swym-',
};

export const sampleWithPartialData: IUser = {
  id: 'b968d46e-9766-4985-8252-a2842db72773',
  login: 'IwO',
};

export const sampleWithFullData: IUser = {
  id: '30005c2b-1737-4bcd-a31a-a27826e91bf1',
  login: 'mAl_@q\\b6\\kDqe\\@OIXV5',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
