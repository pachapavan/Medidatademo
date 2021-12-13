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
 * A Elements.
 */
@Document(collection = "elements")
public class Elements implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("type")
    private ElementType type;

    @DBRef
    @Field("spacing")
    private Spacing spacing;

    @DBRef
    @Field("attributes")
    private Attributes attributes;

    @DBRef
    @Field("button")
    private Button button;

    @DBRef
    @Field("text")
    private Text text;

    @DBRef
    @Field("table")
    private Table table;

    @DBRef
    @Field("form")
    private FormWrap form;

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
    @Field("elements")
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "button", "text", "table", "form", "badge", "icon", "image", "elements", "element", "flexbox" },
        allowSetters = true
    )
    private Set<Elements> elements = new HashSet<>();

    @DBRef
    @Field("element")
    @JsonIgnoreProperties(
        value = { "spacing", "attributes", "button", "text", "table", "form", "badge", "icon", "image", "elements", "element", "flexbox" },
        allowSetters = true
    )
    private Elements element;

    @DBRef
    @Field("flexbox")
    @JsonIgnoreProperties(value = { "spacing", "attributes", "elements", "page" }, allowSetters = true)
    private FlexBox flexbox;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Elements id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ElementType getType() {
        return this.type;
    }

    public Elements type(ElementType type) {
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

    public Elements spacing(Spacing spacing) {
        this.setSpacing(spacing);
        return this;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Elements attributes(Attributes attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Elements button(Button button) {
        this.setButton(button);
        return this;
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Elements text(Text text) {
        this.setText(text);
        return this;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Elements table(Table table) {
        this.setTable(table);
        return this;
    }

    public FormWrap getForm() {
        return this.form;
    }

    public void setForm(FormWrap formWrap) {
        this.form = formWrap;
    }

    public Elements form(FormWrap formWrap) {
        this.setForm(formWrap);
        return this;
    }

    public Badge getBadge() {
        return this.badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public Elements badge(Badge badge) {
        this.setBadge(badge);
        return this;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Elements icon(Icon icon) {
        this.setIcon(icon);
        return this;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Elements image(Image image) {
        this.setImage(image);
        return this;
    }

    public Set<Elements> getElements() {
        return this.elements;
    }

    public void setElements(Set<Elements> elements) {
        if (this.elements != null) {
            this.elements.forEach(i -> i.setElement(null));
        }
        if (elements != null) {
            elements.forEach(i -> i.setElement(this));
        }
        this.elements = elements;
    }

    public Elements elements(Set<Elements> elements) {
        this.setElements(elements);
        return this;
    }

    public Elements addElements(Elements elements) {
        this.elements.add(elements);
        elements.setElement(this);
        return this;
    }

    public Elements removeElements(Elements elements) {
        this.elements.remove(elements);
        elements.setElement(null);
        return this;
    }

    public Elements getElement() {
        return this.element;
    }

    public void setElement(Elements elements) {
        this.element = elements;
    }

    public Elements element(Elements elements) {
        this.setElement(elements);
        return this;
    }

    public FlexBox getFlexbox() {
        return this.flexbox;
    }

    public void setFlexbox(FlexBox flexBox) {
        this.flexbox = flexBox;
    }

    public Elements flexbox(FlexBox flexBox) {
        this.setFlexbox(flexBox);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Elements)) {
            return false;
        }
        return id != null && id.equals(((Elements) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Elements{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
