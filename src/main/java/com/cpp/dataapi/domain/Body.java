package com.cpp.dataapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Body.
 */
@Document(collection = "body")
public class Body implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    @DBRef
    @Field("tabelValues")
    @JsonIgnoreProperties(value = { "attributes", "objectContainingStrings", "displayAtts", "tableValues" }, allowSetters = true)
    private Set<TabelValues> tabelValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Body id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Body attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Set<TabelValues> getTabelValues() {
        return this.tabelValues;
    }

    public void setTabelValues(Set<TabelValues> tabelValues) {
        if (this.tabelValues != null) {
            this.tabelValues.forEach(i -> i.setTableValues(null));
        }
        if (tabelValues != null) {
            tabelValues.forEach(i -> i.setTableValues(this));
        }
        this.tabelValues = tabelValues;
    }

    public Body tabelValues(Set<TabelValues> tabelValues) {
        this.setTabelValues(tabelValues);
        return this;
    }

    public Body addTabelValues(TabelValues tabelValues) {
        this.tabelValues.add(tabelValues);
        tabelValues.setTableValues(this);
        return this;
    }

    public Body removeTabelValues(TabelValues tabelValues) {
        this.tabelValues.remove(tabelValues);
        tabelValues.setTableValues(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Body)) {
            return false;
        }
        return id != null && id.equals(((Body) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Body{" +
            "id=" + getId() +
            "}";
    }
}
