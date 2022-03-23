package io.github.arrudalabs.mizudo.model;

import javax.persistence.*;
import java.io.Serializable;

public class MembroId implements Serializable {
    @ManyToOne
    public Membro membro;
}
