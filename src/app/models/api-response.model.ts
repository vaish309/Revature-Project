export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// Models define the structure of data received from backend. They ensure type safety in Angular
