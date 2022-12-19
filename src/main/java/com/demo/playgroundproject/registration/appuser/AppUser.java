package com.demo.playgroundproject.registration.appuser;

import com.demo.playgroundproject.student.model.Student;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    @Column(name = "app_user_id")
    private Long appUserId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRole appUserRole,
                   Student student
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
