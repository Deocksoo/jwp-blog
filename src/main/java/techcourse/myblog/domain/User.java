package techcourse.myblog.domain;

import techcourse.myblog.dto.UserProfileDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void updateByUserProfileDto(UserProfileDto userProfileDto) {
        String updatedName = userProfileDto.getName();
        UserFactory.checkValidNameLength(updatedName);
        UserFactory.checkValidName(updatedName);
        this.name = updatedName;
    }
}