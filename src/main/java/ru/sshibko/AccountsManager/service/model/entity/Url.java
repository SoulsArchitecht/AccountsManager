package ru.sshibko.AccountsManager.service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sshibko.AccountsManager.service.AttributeEncryptor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long id;

    private String link;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime changeAt;

    @Convert(converter = AttributeEncryptor.class)
    private String login;

    @Convert(converter = AttributeEncryptor.class)
    private String password;

    @Convert(converter = AttributeEncryptor.class)
    private String email;

    @Convert(converter = AttributeEncryptor.class)
    private String emailAnother;

    @Convert(converter = AttributeEncryptor.class)
    private String nickName;

    private boolean active;
}
