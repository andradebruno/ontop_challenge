package com.ontop.wallet.adapters.out.entity;

import com.ontop.wallet.application.core.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_data")
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String nationalIdNumber;
    private LocalDateTime createdAt;

    public User toDomain() {
        return User.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .nationalIdNumber(nationalIdNumber)
                .build();
    }
}
