import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskList } from '../../tasks/task-list/task-list';
import { TaskForm } from '../../tasks/task-form/task-form';
import { UserService } from '../../../core/services/user-service';
import { User } from '../../../core/models/user.model';
import { Task } from '../../../core/models/task.model';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule, TaskList, TaskForm],
  templateUrl: './user-detail.html',
  styleUrl: './user-detail.css',
})
export class UserDetail implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly userService = inject(UserService);

  user?: User;

  ngOnInit() {
    const userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.loadUser(userId);
  }

  loadUser(userId: number) {
    this.userService.getUser(userId).subscribe(u => this.user = u);
  }

  handleAddTask(task: Partial<Task>) {
    if (this.user && task.title && task.scheduledDateTime) {
      this.userService.addTask(this.user.id, {
        title: task.title,
        scheduledDateTime: task.scheduledDateTime
      }).subscribe(() => this.loadUser(this.user!.id));
    }
  }

  handleDeleteTask(taskId: number) {
    if (this.user) {
      this.userService.deleteTask(this.user.id, taskId)
        .subscribe(() => this.loadUser(this.user!.id));
    }
  }

  handleUpdateTask(task: Task) {
    if (this.user) {
      this.userService.updateTask(this.user.id, task.id, task)
        .subscribe(() => this.loadUser(this.user!.id));
    }
  }

  reload() {
    if (this.user) {
      this.loadUser(this.user.id);
    }
  }

  goBack() {
    this.router.navigate(['/users/new']);
  }
}
