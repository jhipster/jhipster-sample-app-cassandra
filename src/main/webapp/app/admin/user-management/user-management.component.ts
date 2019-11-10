import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { AccountService } from 'app/core/auth/account.service';
import { UserService } from 'app/core/user/user.service';
import { User } from 'app/core/user/user.model';
import { UserManagementDeleteDialogComponent } from './user-management-delete-dialog.component';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit, OnDestroy {
  currentAccount: any;
  users: User[];
  error: any;
  success: any;
  userListSubscription: Subscription;

  constructor(
    private userService: UserService,
    private alertService: JhiAlertService,
    private accountService: AccountService,
    private eventManager: JhiEventManager,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
      this.loadAll();
      this.registerChangeInUsers();
    });
  }

  ngOnDestroy() {
    if (this.userListSubscription) {
      this.eventManager.destroy(this.userListSubscription);
    }
  }

  registerChangeInUsers() {
    this.userListSubscription = this.eventManager.subscribe('userListModification', () => this.loadAll());
  }

  setActive(user, isActivated) {
    user.activated = isActivated;

    this.userService.update(user).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.loadAll();
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  loadAll() {
    this.userService
      .query()
      .subscribe((res: HttpResponse<User[]>) => this.onSuccess(res.body), (res: HttpResponse<any>) => this.onError(res.body));
  }

  trackIdentity(index, item: User) {
    return item.id;
  }

  deleteUser(user: User) {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
  }

  private onSuccess(data) {
    this.users = data;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }
}
