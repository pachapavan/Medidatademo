package com.cpp.dataapi.domain;

import com.cpp.dataapi.domain.enumeration.ColorEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BadgeType.
 */
@Document(collection = "badge_type")
public class BadgeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("status")
    private String status;

    @Field("type")
    private ColorEnum type;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    @DBRef
    @Field("badgeType")
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "text", "badge", "icon", "image", "badgeTypes", "displayAtt" },
        allowSetters = true
    )
    private DisplayAtt badgeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public BadgeType id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public BadgeType status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ColorEnum getType() {
        return this.type;
    }

    public BadgeType type(ColorEnum type) {
        this.setType(type);
        return this;
    }

    public void setType(ColorEnum type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public BadgeType attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public DisplayAtt getBadgeType() {
        return this.badgeType;
    }

    public void setBadgeType(DisplayAtt displayAtt) {
        this.badgeType = displayAtt;
    }

    public BadgeType badgeType(DisplayAtt displayAtt) {
        this.setBadgeType(displayAtt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BadgeType)) {
            return false;
        }
        return id != null && id.equals(((BadgeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BadgeType{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
