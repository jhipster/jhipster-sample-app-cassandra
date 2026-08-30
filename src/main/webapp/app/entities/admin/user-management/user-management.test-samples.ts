import { IUserManagement, NewUserManagement } from './user-management.model';

export const sampleWithRequiredData: IUserManagement = {
  login: 'Randall.Sauer',
  email: 'Lucienne5@hotmail.com',
};

export const sampleWithPartialData: IUserManagement = {
  id: 'unnecessarily phew',
  login: 'Rowland.Mann',
  firstName: 'Hertha',
  lastName: 'Weber',
  email: 'Cecil76@hotmail.com',
  activated: true,
  langKey: 'en',
};

export const sampleWithFullData: IUserManagement = {
  id: 'atop bar fully',
  login: 'Merlin.Rice',
  firstName: 'Dortha',
  lastName: 'Carter',
  email: 'Joann_Howe33@yahoo.com',
  activated: false,
  langKey: 'en',
  authorities: ['ROLE_USER'],
};

export const sampleWithNewData: NewUserManagement = {
  email: 'Jasen82@yahoo.com',
  login: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
