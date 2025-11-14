import { Task } from "./task.model";

export interface User {
    id: number;
    name: string;
    tasks?: Task[];
}
