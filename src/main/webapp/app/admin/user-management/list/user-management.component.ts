import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import UserManagementDeleteDialogComponent from '../delete/user-management-delete-dialog.component';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
  imports: [RouterModule, SharedModule],
})
export default class UserManagementComponent implements OnInit {
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
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
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
