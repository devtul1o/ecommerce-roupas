package br.com.lojaroupas.model;

import jakarta.persistence.*;


@Entity 
@Table(name = "pessoa_cliente")
@PrimaryKeyJoinColumn(name = "cod_pessoa")
public class Cliente extends Pessoa {
    public Cliente() {}
}
