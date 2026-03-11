import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { TranslateModule } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { UserManagementDeleteDialog } from '../delete/user-management-delete-dialog';
import { UserManagementService } from '../service/user-management.service';
import { IUserManagement } from '../user-management.model';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, NgbPagination, TranslateDirective, TranslateModule],
})
export class UserManagement implements OnInit {
  readonly currentAccount = inject(AccountService).account;
  readonly users = signal<IUserManagement[] | null>(null);
  readonly isLoading = signal(false);

  private readonly userService = inject(UserManagementService);
  private readonly modalService = inject(NgbModal);

  ngOnInit(): void {
    this.loadAll();
  }

  setActive(userManagement: IUserManagement, isActivated: boolean): void {
    this.userService.update({ ...userManagement, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(item: IUserManagement): string {
    return item.id!;
  }

  deleteUser(userManagement: IUserManagement): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userManagement = userManagement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.userService.query().subscribe({
      next: (res: HttpResponse<IUserManagement[]>) => {
        this.isLoading.set(false);
        this.onSuccess(res.body);
      },
      error: () => this.isLoading.set(false),
    });
  }

  private onSuccess(users: IUserManagement[] | null): void {
    this.users.set(users);
  }
}
