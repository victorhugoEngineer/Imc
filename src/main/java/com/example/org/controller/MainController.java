package com.example.org.controller;

import com.example.org.model.Pessoa;
import com.example.org.utils.ArquivoUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private TextField txtNome;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPeso;
    @FXML private Label lblImc;
    @FXML private Label lblClassificacao;

    @FXML private TableView<Pessoa> tabela;
    @FXML private TableColumn<Pessoa, String> colId;
    @FXML private TableColumn<Pessoa, String> colNome;
    @FXML private TableColumn<Pessoa, String> colAltura;
    @FXML private TableColumn<Pessoa, String> colPeso;
    @FXML private TableColumn<Pessoa, String> colImc;

    private final ObservableList<Pessoa> pessoas = FXCollections.observableArrayList();
    private int proximoId = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colNome.setCellValueFactory(c -> c.getValue().nomeProperty());
        colAltura.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getAltura())));
        colPeso.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getPeso())));
        colImc.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getImc())));

        tabela.setItems(pessoas);
    }

    @FXML
    protected void onCalcularClick() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            alerta("Informe o nome.");
            return;
        }

        try {
            double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));

            if (altura <= 0 || peso <= 0) {
                alerta("Altura e peso devem ser maiores que zero.");
                return;
            }

            Pessoa p = new Pessoa(proximoId++, nome, altura, peso);
            pessoas.add(p); // a tabela atualiza sozinha (ObservableList)

            lblImc.setText(String.format("%.2f", p.getImc()));
            lblClassificacao.setText(p.getClassificacao());

            txtNome.clear();
            txtAltura.clear();
            txtPeso.clear();
            txtNome.requestFocus();
        } catch (NumberFormatException e) {
            alerta("Altura e peso devem ser números válidos (ex: 1.75 e 70).");
        }
    }

    @FXML
    protected void onSalvarClick() {
        try {
            ArquivoUtil.salvar(pessoas);
            info("Dados salvos em dados_pessoas.txt");
        } catch (IOException e) {
            alerta("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    protected void onCarregarClick() {
        try {
            List<Pessoa> lista = ArquivoUtil.carregar();
            pessoas.setAll(lista);
            proximoId = lista.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
        } catch (IOException | NumberFormatException e) {
            alerta("Erro ao carregar: " + e.getMessage());
        }
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}