import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserManagement, NewUserManagement } from '../user-management.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { login: unknown }> = Partial<Omit<T, 'login'>> & { login: T['login'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserManagement for edit and NewUserManagementFormGroupInput for create.
 */
type UserManagementFormGroupInput = IUserManagement | PartialWithRequiredKeyOf<NewUserManagement>;

type UserManagementFormDefaults = Pick<NewUserManagement, 'login' | 'activated' | 'langKey' | 'authorities'>;

type UserManagementFormGroupContent = {
  id: FormControl<IUserManagement['id']>;
  login: FormControl<IUserManagement['login'] | NewUserManagement['login']>;
  firstName: FormControl<IUserManagement['firstName']>;
  lastName: FormControl<IUserManagement['lastName']>;
  email: FormControl<IUserManagement['email']>;
  activated: FormControl<IUserManagement['activated']>;
  langKey: FormControl<IUserManagement['langKey']>;
  authorities: FormControl<IUserManagement['authorities']>;
};

export type UserManagementFormGroup = FormGroup<UserManagementFormGroupContent>;

@Service()
export class UserManagementFormService {
  createUserManagementFormGroup(userManagement?: UserManagementFormGroupInput): UserManagementFormGroup {
    const userManagementRawValue = {
      ...this.getFormDefaults(),
      ...(userManagement ?? { login: null }),
    };

    return new FormGroup<UserManagementFormGroupContent>({
      id: new FormControl(userManagementRawValue.id),
      login: new FormControl(userManagementRawValue.login, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.maxLength(50),
          Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'), // NOSONAR
        ],
      }),
      firstName: new FormControl(userManagementRawValue.firstName, {
        validators: [Validators.maxLength(50)],
      }),
      lastName: new FormControl(userManagementRawValue.lastName, {
        validators: [Validators.maxLength(50)],
      }),
      email: new FormControl(userManagementRawValue.email, {
        validators: [Validators.required, Validators.email, Validators.minLength(5), Validators.maxLength(191)],
      }),
      activated: new FormControl(userManagementRawValue.activated),
      langKey: new FormControl(userManagementRawValue.langKey, {
        validators: [Validators.maxLength(10)],
      }),
      authorities: new FormControl(userManagementRawValue.authorities),
    });
  }

  getUserManagement(form: UserManagementFormGroup): IUserManagement | NewUserManagement {
    return form.getRawValue();
  }

  resetForm(form: UserManagementFormGroup, userManagement: UserManagementFormGroupInput): void {
    const userManagementRawValue = { ...this.getFormDefaults(), ...userManagement };
    form.reset({
      ...userManagementRawValue,
    });
  }

  private getFormDefaults(): UserManagementFormDefaults {
    return {
      login: null,
      activated: true,
      langKey: 'en',
      authorities: [],
    };
  }
}
