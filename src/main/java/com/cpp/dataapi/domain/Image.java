package com.cpp.dataapi.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Image.
 */
@Document(collection = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("source")
    private String source;

    @Field("alt")
    private String alt;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Image id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return this.source;
    }

    public Image source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAlt() {
        return this.alt;
    }

    public Image alt(String alt) {
        this.setAlt(alt);
        return this;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Image attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        return id != null && id.equals(((Image) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", alt='" + getAlt() + "'" +
            "}";
    }
}
