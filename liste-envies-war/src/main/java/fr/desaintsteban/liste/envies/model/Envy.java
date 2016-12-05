package fr.desaintsteban.liste.envies.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import fr.desaintsteban.liste.envies.dto.EnvyDto;
import fr.desaintsteban.liste.envies.dto.NoteDto;
import fr.desaintsteban.liste.envies.util.EncodeUtils;
import fr.desaintsteban.liste.envies.util.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.jdo.annotations.Embedded;
import java.util.ArrayList;
import java.util.List;

/**
 * 01/10/2014.
 */
@Cache
@Entity
public class Envy {

    @Parent
    @JsonIgnore
    Key<ListEnvies> list;
    @Id
    private Long id;

    private String owner;
    /**
     * L'envie à été suggéré par une autre personne
     */
    private Boolean suggest = false;

    private String label;

    private String description;

    private String price;
    private String picture;
    @Embedded
    private List<Link> urls;
    @Index
    private List<String> userTake;

    @Embedded
    private List<Note> notes;


    public Envy() {
        this.notes = new ArrayList<>();
    }

    public Envy(ListEnvies list, String label) {
        this.list = Key.create(list);
        this.label = label;
        this.notes = new ArrayList<>();
    }


    public Envy(EnvyDto envie) {
        setId(envie.getId());
        setOwner(envie.getOwner());
        setSuggest(envie.getSuggest());
        setLabel(envie.getLabel());
        setDescription(envie.getDescription());
        setPrice(envie.getPrice());
        setPicture(envie.getPicture());
        setUrls(envie.getUrls());
        if (envie.getUserTake() != null) {
            List<String> userTake = new ArrayList<>();
            for (String email : envie.getUserTake()) {
                userTake.add(EncodeUtils.encode(email));
            }
            setUserTake(userTake);
        }
        this.notes = new ArrayList<>();
    }

    public EnvyDto toDto() {
        EnvyDto envie = new EnvyDto();
        envie.setId(getId());
        envie.setOwner(getOwner());
        envie.setSuggest(suggest);
        envie.setLabel(getLabel());
        envie.setDescription(getDescription());
        envie.setPrice(getPrice());
        envie.setPicture(getPicture());
        envie.setUrls(getUrls());
        List<String> userTake = new ArrayList<>();
        if (getUserTake() != null) {
            for (String email : getUserTake()) {
                userTake.add(EncodeUtils.decode(email));
            }
        }
        envie.setUserTake(userTake);
        if (this.notes != null && !this.notes.isEmpty()) {
            List<NoteDto> listNoteDto = new ArrayList<>();
            for (Note note : this.notes) {
                listNoteDto.add(note.toDto());
            }
            envie.setNotes(listNoteDto);
        }
        return envie;
    }

    public Key<ListEnvies> getList() {
        return list;
    }

    public void setList(Key<ListEnvies> owner) {
        this.list = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getSuggest() {
        return suggest;
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
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Link> getUrls() {
        return urls;
    }

    public void setUrls(List<Link> urls) {
        this.urls = urls;
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

    public void addUserTake(String userTake) {
        if (this.userTake == null) {
            this.userTake = new ArrayList<>();
        }
        if (!StringUtils.isNullOrEmpty(userTake)) {
            this.userTake.add(userTake);
        }
    }

    public void removeUserTake(String userTake) {
        if (userTake != null) {
            this.userTake.remove(userTake);
        }
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
}