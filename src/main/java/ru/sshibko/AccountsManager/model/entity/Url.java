package ru.sshibko.AccountsManager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sshibko.AccountsManager.service.AttributeEncryptor;

import java.time.LocalDateTime;

@Entity
@Table(name = "url")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changeAt;

    @Column(name = "login", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String login;

    @Column(name = "password", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String password;

    @Column(name = "email", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String email;

    @Column(name = "email_another", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String emailAnother;

    @Column(name = "nickname", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String nickName;

    @Column(name = "active", nullable = false)
    private boolean active;
}
