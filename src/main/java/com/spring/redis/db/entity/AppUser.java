package com.spring.redis.db.entity;

import com.spring.redis.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAdded;
    @Enumerated(EnumType.STRING)
    private UserStatus status;  // ACTIVE, SUSPENDED


}
