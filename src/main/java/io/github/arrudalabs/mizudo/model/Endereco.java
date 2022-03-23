package io.github.arrudalabs.mizudo.model;

import javax.persistence.Embeddable;

@Embeddable
public class Endereco {

    public String logradouro;
    public String numero;
    public String complemento;
    public String localidade;
    public String bairro;
    public String uf;
    public String cep;

}
