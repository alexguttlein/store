package ar.uba.fi.ingsoft1.alternative;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alternative {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String alternativeName;
    private String alternativePhoto;

    public Alternative(String alternativeName, String alternativePhoto){
        this.alternativeName = alternativeName;
        this.alternativePhoto = alternativePhoto;
    }

}

