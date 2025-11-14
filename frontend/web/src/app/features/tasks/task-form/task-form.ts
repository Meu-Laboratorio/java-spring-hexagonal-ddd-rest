import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Task } from '../../../core/models/task.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {
  private readonly fb = inject(FormBuilder);

  @Output() save = new EventEmitter<Partial<Task>>();

  form = this.fb.group({
    title: ['', Validators.required],
    scheduledDateTime: ['', Validators.required]
  });

  onSubmit() {
    if (this.form.valid) {
      const { title, scheduledDateTime } = this.form.value as { title: string; scheduledDateTime: string };
      this.save.emit({ title, scheduledDateTime, done: false });
      this.form.reset();
    }
  }
}
