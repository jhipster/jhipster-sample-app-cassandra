import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: '5e1262ea-3a78-4f7d-bb98-ab763d339a75',
  login: 'OiqH',
};

export const sampleWithPartialData: IUser = {
  id: '19516568-af51-4efc-a18b-157ab0ca5365',
  login: 'c',
};

export const sampleWithFullData: IUser = {
  id: 'f7cc8f9f-e4b0-4147-9f95-7d9bff25d118',
  login: 'E3cr@A5J\\3ZsW25W\\,5\\2jH',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
