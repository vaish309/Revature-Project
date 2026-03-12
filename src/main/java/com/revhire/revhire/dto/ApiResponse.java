// DTOs are used to transfer data between frontend and backend without exposing entity classes directly.
package com.revhire.revhire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
