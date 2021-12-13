package com.cpp.dataapi.domain;

import com.cpp.dataapi.domain.enumeration.ElementType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DisplayAtt.
 */
@Document(collection = "display_att")
public class DisplayAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("type")
    private ElementType type;

    @DBRef
    @Field("spacing")
    private Spacing spacing;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    @DBRef
    @Field("text")
    private Text text;

    @DBRef
    @Field("badge")
    private Badge badge;

    @DBRef
    @Field("icon")
    private Icon icon;

    @DBRef
    @Field("image")
    private Image image;

    @DBRef
    @Field("badgeType")
    @JsonIgnoreProperties(value = { "attributes", "badgeType" }, allowSetters = true)
    private Set<BadgeType> badgeTypes = new HashSet<>();

    @DBRef
    @Field("displayAtt")
    @JsonIgnoreProperties(value = { "attributes", "objectContainingStrings", "displayAtts", "tableValues" }, allowSetters = true)
    private TabelValues displayAtt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public DisplayAtt id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DisplayAtt name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElementType getType() {
        return this.type;
    }

    public DisplayAtt type(ElementType type) {
        this.setType(type);
        return this;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Spacing getSpacing() {
        return this.spacing;
    }

    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    public DisplayAtt spacing(Spacing spacing) {
        this.setSpacing(spacing);
        return this;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public DisplayAtt attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public DisplayAtt text(Text text) {
        this.setText(text);
        return this;
    }

    public Badge getBadge() {
        return this.badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public DisplayAtt badge(Badge badge) {
        this.setBadge(badge);
        return this;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public DisplayAtt icon(Icon icon) {
        this.setIcon(icon);
        return this;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public DisplayAtt image(Image image) {
        this.setImage(image);
        return this;
    }

    public Set<BadgeType> getBadgeTypes() {
        return this.badgeTypes;
    }

    public void setBadgeTypes(Set<BadgeType> badgeTypes) {
        if (this.badgeTypes != null) {
            this.badgeTypes.forEach(i -> i.setBadgeType(null));
        }
        if (badgeTypes != null) {
            badgeTypes.forEach(i -> i.setBadgeType(this));
        }
        this.badgeTypes = badgeTypes;
    }

    public DisplayAtt badgeTypes(Set<BadgeType> badgeTypes) {
        this.setBadgeTypes(badgeTypes);
        return this;
    }

    public DisplayAtt addBadgeType(BadgeType badgeType) {
        this.badgeTypes.add(badgeType);
        badgeType.setBadgeType(this);
        return this;
    }

    public DisplayAtt removeBadgeType(BadgeType badgeType) {
        this.badgeTypes.remove(badgeType);
        badgeType.setBadgeType(null);
        return this;
    }

    public TabelValues getDisplayAtt() {
        return this.displayAtt;
    }

    public void setDisplayAtt(TabelValues tabelValues) {
        this.displayAtt = tabelValues;
    }

    public DisplayAtt displayAtt(TabelValues tabelValues) {
        this.setDisplayAtt(tabelValues);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisplayAtt)) {
            return false;
        }
        return id != null && id.equals(((DisplayAtt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisplayAtt{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
