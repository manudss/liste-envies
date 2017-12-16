package fr.desaintsteban.liste.envies.model;

import com.googlecode.objectify.annotation.Index;
import fr.desaintsteban.liste.envies.dto.UserShareDto;

import javax.jdo.annotations.Embedded;

/**
 *
 */
public class UserShare {
    @Index
    private String email;

    private UserShareType type;

    @Embedded
    private AppUser user;

    public UserShare() {
    }

    public UserShare(String email, UserShareType type) {
        this.email = email;
        this.type = type;
    }

    public UserShare(AppUser user, UserShareType type) {
        this.email = user.getEmail();
        this.user = user;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserShareType getType() {
        return type;
    }

    public boolean isOwner() {
        return type == UserShareType.OWNER;
    }

    public void setType(UserShareType type) {
        this.type = type;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public UserShareDto toDto() {
        return new UserShareDto(this.getEmail(), this.getType(), null);
    }
}
