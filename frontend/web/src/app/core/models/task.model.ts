export interface Task {
    id: number;
    title: string;
    scheduledDateTime: string;   // ISO string, ex: "2025-11-14T10:00:00"
    done: boolean;
}
