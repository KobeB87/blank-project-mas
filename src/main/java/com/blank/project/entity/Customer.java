package com.blank.project.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Schema(required = false, description = "The ID for the customer. Should be a Unique ID with max 36 Characters. If not provided, the system will assign one.")
    @Size(max = 36)
    @Column(name = "customer_id")
    private String customerId;

    @Schema(required = true, description = "The first name. The length cannot exceed 50.")
    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name")
    private String firstName;

    @Schema(required = true, description = "The last name. The length cannot exceed 50.")
    @NotBlank @Size(max = 50)
    @Column(name = "last_name")
    private String lastName;
}
