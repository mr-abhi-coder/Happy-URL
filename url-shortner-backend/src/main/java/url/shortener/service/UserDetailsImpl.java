//package url.shortener.service;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import url.shortener.models.User;
//
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.stream.Collectors;
//
//
//@Data
//@NoArgsConstructor
//@Service
//public class UserDetailsImpl implements UserDetails {
//
//    private static final Long serialVersionUid = 1L;
//
//    private Long id;
//    private String email;
//    private String username;
//    private String password;
//
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserDetailsImpl(Long id, String email, String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.email = email;
//        this.username = username;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public static UserDetailsImpl build(User user){
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRoles());
//        return new UserDetailsImpl(
//                user.getId(),
//                user.getEmail(),
//                user.getUsername(),
//                user.getPassword(),
//                Collections.singleton(authority)
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//}

package url.shortener.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import url.shortener.models.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Service
public class UserDetailsImpl implements UserDetails {

    private static final Long serialVersionUid = 1L;

    private Long id;
    private String email;
    private String username;
    private String password;
    private String roles; // ✅ Added field

    public UserDetailsImpl(Long id, String email, String username, String password, String roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public static UserDetailsImpl build(User user){
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles() // ✅ Pass comma-separated string like "ADMIN,USER"
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Add these overrides to prevent Spring Security issues
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

