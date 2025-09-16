package ru.sshibko.AccountsManager.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.sshibko.AccountsManager.service.AttributeEncryptor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank
    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "description", nullable = true)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "changed_at", nullable = true)
    private LocalDateTime changedAt;

    @NotBlank
    @Column(name = "login", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String login;

    @NotBlank
    @Column(name = "password", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String password;

    @NotBlank
    @Column(name = "email", nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String email;

    @Column(name = "another_email", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String emailAnother;

    @Column(name = "nickname", nullable = true)
    @Convert(converter = AttributeEncryptor.class)
    private String nickName;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
 //   @JsonIgnore
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    private User user;


    @Override
    public String toString() {
        return "Account{" +
                "active=" + active +
                ", nickName='" + nickName + '\'' +
                ", emailAnother='" + emailAnother + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                ", changedAt=" + changedAt +
                ", createdAt=" + createdAt +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", id=" + id +
                '}';
    }
}
