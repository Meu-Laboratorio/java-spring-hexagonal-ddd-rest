import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Task } from '../../../core/models/task.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-item.html',
  styleUrl: './task-item.css',
})
export class TaskItem {
  @Input() task!: Task;
  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<Task>();

  onDelete() {
    this.delete.emit(this.task.id);
  }

  onToggleDone() {
    this.update.emit({ ...this.task, done: !this.task.done });
  }
}
