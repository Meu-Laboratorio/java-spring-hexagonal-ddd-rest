import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User } from '../models/user.model';
import { Task } from '../models/task.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly baseUrl = `${environment.apiBaseUrl}/users`;
  private readonly http = inject(HttpClient);

  createUser(user: { name: string }): Observable<User> {
    return this.http.post<User>(this.baseUrl, user);
  }

  getUser(userId: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${userId}`);
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${userId}`);
  }

  addTask(userId: number, task: { title: string; scheduledDateTime: string }): Observable<Task> {
    return this.http.post<Task>(`${this.baseUrl}/${userId}/tasks`, task);
  }

  updateTask(userId: number, taskId: number, updates: Partial<Task>): Observable<Task> {
    return this.http.patch<Task>(`${this.baseUrl}/${userId}/tasks/${taskId}`, updates);
  }

  deleteTask(userId: number, taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${userId}/tasks/${taskId}`);
  }
  
}
