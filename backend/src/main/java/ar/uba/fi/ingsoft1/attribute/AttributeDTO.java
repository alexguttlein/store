package ar.uba.fi.ingsoft1.attribute;

public record AttributeDTO(
        Long id,
        String attributeName,
        String value
) {
    public AttributeDTO(Attribute attribute){
        this(attribute.getId(), attribute.getAttributeName(), attribute.getValue());
    }

    public Attribute asAttribute(){
        Attribute attribute = new Attribute(this.attributeName(), this.value);
        attribute.setAttributeName(this.attributeName);
        attribute.setValue(this.value);
        return attribute;
    }

}
