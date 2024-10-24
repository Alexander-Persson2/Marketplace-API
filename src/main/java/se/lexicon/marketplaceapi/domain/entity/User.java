package se.lexicon.marketplaceapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
@Builder

@Entity
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;

    private boolean expired;
    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Advertisement> advertisements = new HashSet<>();

    public void addAdvertisement(Advertisement advertisement) {
        this.advertisements.add(advertisement);
        advertisement.setUser(this);
    }

    public void removeAdvertisement(Advertisement advertisement) {
        this.advertisements.remove(advertisement);
        advertisement.setUser(null);
    }
    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
       if (role == null) {
           throw new IllegalArgumentException("Role cannot be null");
       }
       if (roles != null && roles.contains(role)) {
       roles.remove(role);
       role.getUsers().remove(this);}
    }
}
