package fr.desaintsteban.liste.envies.dto;

import fr.desaintsteban.liste.envies.model.Link;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 01/10/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvyDto {
    private Long id;

    private String owner;
    private Boolean suggest = false;
    private String label;

    private String description;

    private String price;
    private String picture;
    private List<Link> urls;
    private List<String> userTake;

    private List<NoteDto> notes;

    public EnvyDto() {
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

    public List<String> getUserTake() {
        return userTake;
    }

    public void setUserTake(List<String> userTake) {
        this.userTake = userTake;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }
}