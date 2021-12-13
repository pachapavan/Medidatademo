package com.cpp.dataapi.domain;

import com.cpp.dataapi.domain.enumeration.ColorEnum;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Badge.
 */
@Document(collection = "badge")
public class Badge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("color")
    private ColorEnum color;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Badge id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ColorEnum getColor() {
        return this.color;
    }

    public Badge color(ColorEnum color) {
        this.setColor(color);
        return this;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Badge)) {
            return false;
        }
        return id != null && id.equals(((Badge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Badge{" +
            "id=" + getId() +
            ", color='" + getColor() + "'" +
            "}";
    }
}
