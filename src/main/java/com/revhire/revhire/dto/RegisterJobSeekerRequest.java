package com.revhire.revhire.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterJobSeekerRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Pattern(regexp = "^[0-9\\s\\-()]*$", message = "Phone must contain only digits, spaces, dashes, and parentheses")
    private String phone;
    
    private String location;
    private String currentEmploymentStatus;
}
