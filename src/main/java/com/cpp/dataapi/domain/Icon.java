package com.cpp.dataapi.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Icon.
 */
@Document(collection = "icon")
public class Icon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("aria_label")
    private String ariaLabel;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Icon id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAriaLabel() {
        return this.ariaLabel;
    }

    public Icon ariaLabel(String ariaLabel) {
        this.setAriaLabel(ariaLabel);
        return this;
    }

    public void setAriaLabel(String ariaLabel) {
        this.ariaLabel = ariaLabel;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Icon attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Icon)) {
            return false;
        }
        return id != null && id.equals(((Icon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Icon{" +
            "id=" + getId() +
            ", ariaLabel='" + getAriaLabel() + "'" +
            "}";
    }
}
