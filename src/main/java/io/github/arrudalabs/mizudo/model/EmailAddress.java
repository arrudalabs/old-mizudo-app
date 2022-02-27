package io.github.arrudalabs.mizudo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Embeddable
public class EmailAddress {

    public static EmailAddress of(String email) {
        var emailAddress = new EmailAddress();
        emailAddress.value = email;
        return emailAddress;
    }

    @Email
    @NotBlank
    @Column(name = "email")
    public String value;

}
