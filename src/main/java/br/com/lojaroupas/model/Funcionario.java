package br.com.lojaroupas.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity 
@Table(name = "pessoa_funcionario")
@PrimaryKeyJoinColumn(name = "cod_pessoa")
public class Funcionario extends Pessoa {

    public Funcionario() {}

    @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    @Column(nullable = false, length = 80)
    private String cargo;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @ManyToOne
    @JoinColumn(name = "cod_pessoa_supervisor")
    private Funcionario supervisor;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Funcionario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Funcionario supervisor) {
        this.supervisor = supervisor;
    }

    

}
