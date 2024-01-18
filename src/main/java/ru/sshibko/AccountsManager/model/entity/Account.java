package ru.sshibko.AccountsManager.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotBlank
    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "description", nullable = true)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @NotBlank
    @Column(name = "login", nullable = true)
    //@Convert(converter = AttributeEncryptor.class)
    private String login;

    @NotBlank
    @Column(name = "password", nullable = false)
    //@Convert(converter = AttributeEncryptor.class)
    private String password;

    @NotBlank
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
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    //@JsonIgnore
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private User user;
}
