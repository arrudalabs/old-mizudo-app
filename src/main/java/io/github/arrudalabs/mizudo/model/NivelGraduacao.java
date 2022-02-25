package io.github.arrudalabs.mizudo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "niveis_graduacao")
public class NivelGraduacao extends PanacheEntityBase {

    @Id
    @NotBlank
    public String nivel;

    @NotBlank
    public String descricao;

    @Size(min = 6)
    public Integer mesesDeCarencia = 6;

}
