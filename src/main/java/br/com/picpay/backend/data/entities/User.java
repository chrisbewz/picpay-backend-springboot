package br.com.picpay.backend.data.entities;

import br.com.picpay.backend.data.enums.UserKnownTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    @Email
    private String userEmail;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserKnownTypes userType;

    @Column(name = "currency_amount", nullable = false)
    private Double currencyAmount;
}
