package ru.sshibko.AccountsManager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

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
    //@Convert(converter = AttributeEncryptor.class)
    private String login;

    @Column(name = "password", nullable = false)
    //@Convert(converter = AttributeEncryptor.class)
    private String password;

    @Column(name = "email", nullable = false)
    //@Convert(converter = AttributeEncryptor.class)
    private String email;

    @Column(name = "another_email", nullable = true)
    //@Convert(converter = AttributeEncryptor.class)
    private String emailAnother;

    @Column(name = "nickname", nullable = true)
    //@Convert(converter = AttributeEncryptor.class)
    private String nickName;

    @Column(name = "active", nullable = false)
    private boolean active;

    //@Column(name = "user_id")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
