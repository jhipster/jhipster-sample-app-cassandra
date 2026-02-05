import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import UserManagementDeleteDialog from '../delete/user-management-delete-dialog';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, NgbModule, TranslateDirective, TranslateModule],
})
export default class UserManagement implements OnInit {
  currentAccount = inject(AccountService).trackCurrentAccount();
  users = signal<User[] | null>(null);
  isLoading = signal(false);

  private readonly userService = inject(UserManagementService);
  private readonly modalService = inject(NgbModal);

  ngOnInit(): void {
    this.loadAll();
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(item: User): string {
    return item.id!;
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
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
      next: (res: HttpResponse<User[]>) => {
        this.isLoading.set(false);
        this.onSuccess(res.body);
      },
      error: () => this.isLoading.set(false),
    });
  }

  private onSuccess(users: User[] | null): void {
    this.users.set(users);
  }
}
