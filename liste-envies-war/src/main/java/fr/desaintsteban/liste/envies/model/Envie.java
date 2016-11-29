package fr.desaintsteban.liste.envies.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 01/10/2014.
 */
@Cache
@Entity
public class Envie {

    @Parent
    @JsonIgnore
    Key<AppUser> owner;
    @Id
    private Long id;

    private String label;

    private String comment;

    private String price;
    private String url;
    private String userTake;

    public Envie() {
    }

    public Envie(AppUser owner, String label) {
        this.owner = Key.create(owner);
        this.label = label;
    }

    public Key<AppUser> getOwner() {
        return owner;
    }

    public void setOwner(Key<AppUser> owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserTake() {
        return userTake;
    }

    public void setUserTake(String userTake) {
        this.userTake = userTake;
    }
}
