package com.example.org.utils;

import com.example.org.model.Pessoa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArquivoUtil {

    private static final String ARQUIVO = "dados_pessoas.txt";

    public static void salvar(List<Pessoa> pessoas) throws IOException {
        try (FileWriter fw = new FileWriter(ARQUIVO, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Pessoa p : pessoas) {
                bw.write(String.format(Locale.US, "%d,%s,%.2f,%.2f",
                        p.getId(), p.getNome(), p.getAltura(), p.getPeso()));
                bw.newLine();
            }
        }
    }

    public static List<Pessoa> carregar() throws IOException {
        List<Pessoa> lista = new ArrayList<>();
        File file = new File(ARQUIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] c = linha.split(",");
                if (c.length < 4) continue;
                lista.add(new Pessoa(
                        Integer.parseInt(c[0].trim()),
                        c[1].trim(),
                        Double.parseDouble(c[2].trim()),
                        Double.parseDouble(c[3].trim())));
            }
        }
        return lista;
    }
}