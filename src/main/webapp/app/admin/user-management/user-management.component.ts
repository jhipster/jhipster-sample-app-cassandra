import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { UserService } from 'app/core/user/user.service';
import { User } from 'app/core/user/user.model';
import { UserManagementDeleteDialogComponent } from './user-management-delete-dialog.component';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent implements OnInit, OnDestroy {
  currentAccount: Account | null = null;
  users: User[] | null = null;
  userListSubscription?: Subscription;

  constructor(
    private userService: UserService,
    private accountService: AccountService,
    private eventManager: JhiEventManager,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.userListSubscription = this.eventManager.subscribe('userListModification', () => this.loadAll());
    this.loadAll();
  }

  ngOnDestroy(): void {
    if (this.userListSubscription) {
      this.eventManager.destroy(this.userListSubscription);
    }
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(index: number, item: User): any {
    return item.id;
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
  }

  private loadAll(): void {
    this.userService.query().subscribe((res: HttpResponse<User[]>) => this.onSuccess(res.body));
  }

  private onSuccess(users: User[] | null): void {
    this.users = users;
  }
}
