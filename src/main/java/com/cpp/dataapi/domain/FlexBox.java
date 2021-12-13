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
 * A FlexBox.
 */
@Document(collection = "flex_box")
public class FlexBox implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("justify_content")
    private String justifyContent;

    @DBRef
    @Field("spacing")
    private Spacing spacing;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    @DBRef
    @Field("elements")
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "button", "text", "table", "form", "badge", "icon", "image", "elements", "element", "flexbox" },
        allowSetters = true
    )
    private Set<Elements> elements = new HashSet<>();

    @DBRef
    @Field("page")
    @JsonIgnoreProperties(value = { "genericObjectsLists", "flexBoxes" }, allowSetters = true)
    private Page page;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FlexBox id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJustifyContent() {
        return this.justifyContent;
    }

    public FlexBox justifyContent(String justifyContent) {
        this.setJustifyContent(justifyContent);
        return this;
    }

    public void setJustifyContent(String justifyContent) {
        this.justifyContent = justifyContent;
    }

    public Spacing getSpacing() {
        return this.spacing;
    }

    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    public FlexBox spacing(Spacing spacing) {
        this.setSpacing(spacing);
        return this;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public FlexBox attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Set<Elements> getElements() {
        return this.elements;
    }

    public void setElements(Set<Elements> elements) {
        if (this.elements != null) {
            this.elements.forEach(i -> i.setFlexbox(null));
        }
        if (elements != null) {
            elements.forEach(i -> i.setFlexbox(this));
        }
        this.elements = elements;
    }

    public FlexBox elements(Set<Elements> elements) {
        this.setElements(elements);
        return this;
    }

    public FlexBox addElements(Elements elements) {
        this.elements.add(elements);
        elements.setFlexbox(this);
        return this;
    }

    public FlexBox removeElements(Elements elements) {
        this.elements.remove(elements);
        elements.setFlexbox(null);
        return this;
    }

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public FlexBox page(Page page) {
        this.setPage(page);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexBox)) {
            return false;
        }
        return id != null && id.equals(((FlexBox) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexBox{" +
            "id=" + getId() +
            ", justifyContent='" + getJustifyContent() + "'" +
            "}";
    }
}
