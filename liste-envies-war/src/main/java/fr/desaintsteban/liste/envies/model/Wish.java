package fr.desaintsteban.liste.envies.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.IfNotNull;
import fr.desaintsteban.liste.envies.dto.AppUserDto;
import fr.desaintsteban.liste.envies.dto.WishDto;
import fr.desaintsteban.liste.envies.dto.NoteDto;
import fr.desaintsteban.liste.envies.util.EncodeUtils;
import fr.desaintsteban.liste.envies.util.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.jdo.annotations.Embedded;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 01/10/2014.
 */
@Cache
@Entity
public class Wish {

    @Parent
    @JsonIgnore
    Key<WishList> list;
    @Id
    private Long id;

    @Embedded
    private AppUser owner;
    /**
     * L'envie à été suggéré par une autre personne
     */
    private Boolean suggest = false;
    /**
     * L'envie est archivé
     */
    private Boolean archived = false;
    /**
     * L'envie a été supprimé, mais elle a été noté comme donné.
     */
    private Boolean deleted = false;

    private String label;

    private String description;

    private String price;

    private List<String> pictures = new ArrayList<>();
    private Date date;



    private int rating;
    @Embedded
    private List<Link> urls;
    @Index
    private List<String> userTake;

    @Embedded
    private List<WishGiven> given = new ArrayList<>();

    @Index(IfNotNull.class)
    private List<String> userReceived;

    @Embedded
    private List<Note> notes;


    public Wish() {
        this.notes = new ArrayList<>();
        this.rating = 0;
    }

    public Wish(WishList list, String label) {
        this.list = Key.create(list);
        this.label = label;
        this.notes = new ArrayList<>();
        this.rating = 0;
    }


    public Wish(WishDto wish) {
        setId(wish.getId());
        if (wish.getOwner() != null) { setOwner(new AppUser(wish.getOwner())); }
        setSuggest(wish.getSuggest());
        setDeleted(wish.getDeleted());
        setLabel(wish.getLabel());
        setDescription(wish.getDescription());
        setPrice(wish.getPrice());
        setPicture(wish.getPicture());
        setDate(wish.getDate());
        setUrls(wish.getUrls());
        setRating(wish.getRating());
        if (wish.getUserTake() != null) {
            List<String> userTake = wish.getUserTake().stream().map(EncodeUtils::encode).collect(Collectors.toList());
            setUserTake(userTake);
        }
        this.notes = new ArrayList<>();
    }

    public WishDto toDto() {
        return this.toDto(false);
    }

    public WishDto toDto(boolean filter) {
        WishDto wish = new WishDto();
        wish.setId(getId());
        wish.setOwner(getOwnerDto());
        wish.setSuggest(getSuggest());
        wish.setDeleted(getDeleted());
        wish.setLabel(getLabel());
        wish.setDescription(getDescription());
        wish.setPrice(getPrice());
        wish.setPicture(getPicture());
        wish.setDate(getDate());
        wish.setRating(getRating());
        wish.setUrls(getUrls());

        if (!filter) { // Do not add this, if you doesn't want to have this information. For filter it.
            if (getUserTake() != null) {
                wish.setUserTake(getUserTake().stream().map(EncodeUtils::decode).collect(Collectors.toList()));
            }
            else {
                wish.setUserTake(Collections.emptyList());
            }
            if (this.notes != null && !this.notes.isEmpty()) {
                List<NoteDto> listNoteDto = this.notes.stream().map(Note::toDto).collect(Collectors.toList());
                wish.setNotes(listNoteDto);
            }
        }

        return wish;
    }

    public Key<WishList> getList() {
        return list;
    }

    public void setList(Key<WishList> owner) {
        this.list = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public Boolean getSuggest() {
        return suggest;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setSuggest(Boolean suggest) {
        this.suggest = suggest;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return (!pictures.isEmpty())? pictures.get(0) : "";
    }

    public void setPicture(String picture) {
        this.pictures.add(picture);
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Link> getUrls() {
        return urls;
    }

    public void setUrls(List<Link> urls) {
        this.urls = urls;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addUrl(String url) {
        if (this.urls == null) {
            this.urls = new ArrayList<>();
        }
        if (!StringUtils.isNullOrEmpty(url)) {
            this.urls.add(new Link(url));
        }
    }

    public void setUserTake(List<String> userTake) {
        this.userTake = userTake;
    }

    public List<String> getUserTake() {
        return userTake;
    }

    public boolean hasUserTaken() {
        return userTake != null && !userTake.isEmpty();
    }

    public void addUserTake(String userTake) {
        if (this.userTake == null) {
            this.userTake = new ArrayList<>();
        }
        if (!StringUtils.isNullOrEmpty(userTake)) {
            this.userTake.add(EncodeUtils.encode(userTake));
        }
    }

    public void removeUserTake(String userTake) {
        if (userTake != null) {
            this.userTake.remove(EncodeUtils.encode(userTake));
        }
    }

    public void addUserTake(AppUser userTake) {
        if (this.userTake == null) {
            this.userTake = new ArrayList<>();
        }
        if (!StringUtils.isNullOrEmpty(userTake.getEmail())) {
            this.userTake.add(EncodeUtils.encode(userTake.getEmail()));
            WishGiven wishGiven = new WishGiven(userTake);
            this.given.add(wishGiven);
        }
    }

    public void removeUserTake(AppUser userTake) {
        if (userTake != null) {
            this.userTake.remove(EncodeUtils.encode(userTake.getEmail()));

            this.given.removeIf(s -> s.getEmail().equals(userTake.getEmail()));
        }
    }

    public List<String> getUserReceived() {
        return userReceived;
    }

    public void setUserReceived(List<String> userReceived) {
        this.userReceived = userReceived;
    }

    public void addNote(String owner, String email, String text) {
        this.notes.add(new Note(owner, email, text));
    }

    public List<Note> getNotes () {
        return this.notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<WishGiven> getGiven() {
        return given;
    }

    public void setGiven(List<WishGiven> given) {
        this.given = given;
    }

    public AppUserDto getOwnerDto() {
        return owner.toDto();
    }
}
