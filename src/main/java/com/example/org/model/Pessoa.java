package com.example.org.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

public class Pessoa {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final DoubleProperty altura = new SimpleDoubleProperty();
    private final DoubleProperty peso = new SimpleDoubleProperty();

    // Calculados automaticamente a partir de altura e peso
    private final DoubleBinding imc = Bindings.createDoubleBinding(
            () -> getPeso() / (getAltura() * getAltura()), peso, altura);
    private final StringBinding classificacao = Bindings.createStringBinding(
            () -> classificar(getImc()), imc);

    public Pessoa(int id, String nome, double altura, double peso) {
        this.id.set(id);
        this.nome.set(nome);
        this.altura.set(altura);
        this.peso.set(peso);
    }

    public int getId() { return id.get(); }
    public String getNome() { return nome.get(); }
    public double getAltura() { return altura.get(); }
    public double getPeso() { return peso.get(); }
    public double getImc() { return imc.get(); }
    public String getClassificacao() { return classificacao.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public DoubleProperty alturaProperty() { return altura; }
    public DoubleProperty pesoProperty() { return peso; }
    public DoubleBinding imcProperty() { return imc; }
    public StringBinding classificacaoProperty() { return classificacao; }

    private static String classificar(double imc) {
        if (imc < 18.5) return "Abaixo do Peso";
        if (imc < 25)   return "Peso Normal";
        if (imc < 30)   return "Sobrepeso";
        if (imc < 35)   return "Obesidade Grau 1";
        if (imc < 40)   return "Obesidade Grau 2";
        return "Obesidade Grau 3";
    }
}