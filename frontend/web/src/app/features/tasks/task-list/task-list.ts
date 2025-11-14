import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Task } from '../../../core/models/task.model';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList {
  @Input() tasks: Task[] = [];
  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<Task>();

  onDelete(taskId: number) {
    this.delete.emit(taskId);
  }

  onToggleDone(task: Task) {
    this.update.emit({ ...task, done: !task.done });
  }
}
