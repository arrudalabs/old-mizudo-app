package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;

@Embeddable
public class Endereco {

    public String logradouro;
    public String numero;
    public String complemento;
    public String cidade;
    public String uf;
    public String cep;

}
