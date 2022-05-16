package com.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreateModel {

    @NotNull
    @Length(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Length(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Length(min = 1, max = 250)
    private String email;

    @Length(min = 12, max = 12)
    private String phone;
}
