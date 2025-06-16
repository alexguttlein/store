package ar.uba.fi.ingsoft1.alternative;

public record AlternativeDTO(
        Long id,
        String alternativeName,
        String alternativePhoto
) {
    public AlternativeDTO(Alternative alternative){
        this(alternative.getId(), alternative.getAlternativeName(), alternative.getAlternativePhoto());
    }

    public Alternative asAlternative(){
        Alternative alternative = new Alternative(this.alternativeName(), this.alternativePhoto());
        alternative.setAlternativeName(this.alternativeName);
        return alternative;
    }

}

