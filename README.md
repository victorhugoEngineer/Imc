# Calculadora de IMC com JavaFX

Aplicação desktop desenvolvida em **JavaFX** para cadastrar pessoas (nome, altura e peso), calcular o **Índice de Massa Corporal (IMC)**, exibir os dados em uma tabela dinâmica e salvar/carregar os registros em um arquivo CSV.

Projeto criado como exercício de laboratório de programação, com foco em interface gráfica, manipulação de arquivos e boas práticas de organização de código (MVC).

---

## Funcionalidades

- Cadastro de pessoas com **nome**, **altura (m)** e **peso (kg)**
- Cálculo do IMC pela fórmula `IMC = peso / altura²`
- Exibição do IMC e da **classificação** em destaque na tela
- **Tabela dinâmica** (`TableView` ligada a uma `ObservableList`): atualiza sozinha ao adicionar, remover ou carregar registros
- Coluna de classificação com **cor por faixa** (azul, verde, amarelo, laranja e vermelho)
- Ordenação numérica correta nas colunas (ID 10 vem depois do ID 2)
- Valores numéricos alinhados à direita e formatados (`1.75 m`, `70.0 kg`, `22.86`)
- Clique em uma linha para ver o IMC e a classificação dela no painel principal
- Exclusão de registro pelo menu **Edit > Delete** ou pela tecla `Delete`, com confirmação
- **Salvar** os dados em `dados_pessoas.txt` (formato CSV)
- **Carregar** os dados salvos e exibi-los na tabela
- Validação de entrada: campos vazios, valores não numéricos, valores menores ou iguais a zero e vírgula no nome (que quebraria o CSV)
- Aceita vírgula ou ponto como separador decimal (`1,75` ou `1.75`)
- Mensagem "Nenhuma pessoa cadastrada" quando a tabela está vazia

---

## Tecnologias

- Java (JDK 17 ou superior, conforme o `pom.xml`)
- JavaFX (controls e FXML)
- Maven (com Maven Wrapper incluído)
- Sistema de módulos Java (`module-info.java`)

---

## Estrutura do projeto

```
imc
├── pom.xml
├── mvnw / mvnw.cmd
├── dados_pessoas.txt              # arquivo de exemplo (gerado ao salvar)
└── src
    └── main
        ├── java
        │   ├── module-info.java
        │   └── com.example.org
        │       ├── Main.java
        │       ├── controller
        │       │   └── MainController.java
        │       ├── model
        │       │   └── Pessoa.java
        │       ├── utils
        │       │   ├── ArquivoUtil.java
        │       │   └── PathFXML.java
        │       └── view
        │           └── MainView.fxml
        └── resources
```

### Responsabilidade de cada classe

| Classe | Pacote | Função |
|---|---|---|
| `Main` | `com.example.org` | Ponto de entrada. Carrega o `MainView.fxml` e abre a janela |
| `Pessoa` | `model` | Representa uma pessoa. Usa *properties* do JavaFX e calcula IMC e classificação automaticamente (bindings) |
| `ArquivoUtil` | `utils` | Classe utilitária para salvar e carregar o arquivo CSV (`FileWriter` / `BufferedReader`) |
| `PathFXML` | `utils` | Auxiliar para resolver o caminho do arquivo FXML |
| `MainController` | `controller` | Controla a tela: eventos dos botões, validação, configuração da tabela e exclusão |
| `MainView.fxml` | `view` | Layout da interface (formulário, painel de resultado e tabela) |

---

## Classificação do IMC

| Classificação | Faixa de IMC |
|---|---|
| Abaixo do Peso | IMC < 18,5 |
| Peso Normal | 18,5 ≤ IMC < 25 |
| Sobrepeso | 25 ≤ IMC < 30 |
| Obesidade Grau 1 | 30 ≤ IMC < 35 |
| Obesidade Grau 2 | 35 ≤ IMC < 40 |
| Obesidade Grau 3 | IMC ≥ 40 |

> As faixas são contínuas (por exemplo, `< 25` em vez de `≤ 24.9`) para que valores como 24,95 não fiquem sem classificação.

---

## Formato do arquivo de dados

Os dados são salvos em `dados_pessoas.txt`, na pasta raiz do projeto, em formato **CSV** (valores separados por vírgula), uma pessoa por linha:

```
id,nome,altura,peso
```

Exemplo:

```
1,Maria Silva,1.65,58.00
2,João Santos,1.80,92.50
3,Ana Costa,1.60,48.00
4,Pedro Lima,1.75,105.00
```

O IMC e a classificação **não** são gravados no arquivo: são recalculados a partir da altura e do peso ao carregar.

---

## Como executar

### Pré-requisitos

- JDK 17 ou superior instalado
- Maven (opcional, o projeto já inclui o Maven Wrapper)
- IntelliJ IDEA (opcional)

### Pelo terminal

```bash
# Linux / macOS
./mvnw clean javafx:run

# Windows
mvnw.cmd clean javafx:run
```

### Pelo IntelliJ IDEA

1. Abra a pasta do projeto (`File > Open`)
2. Aguarde o Maven baixar as dependências
3. Execute a classe `Main`

---

## Como usar

1. Preencha **Nome**, **Altura** (ex.: `1.75`) e **Peso** (ex.: `70`)
2. Clique em **Calcular IMC**: o resultado aparece no painel e a pessoa é adicionada à tabela
3. Clique em **Salvar** para gravar os registros em `dados_pessoas.txt`
4. Clique em **Carregar Dados** para ler o arquivo e preencher a tabela
5. Para excluir, selecione uma linha e use **Edit > Delete** ou a tecla `Delete`

---

## Configuração do módulo

Como o projeto usa módulos, o `module-info.java` precisa liberar os pacotes para o JavaFX:

```java
module com.example.org {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.org to javafx.fxml;
    opens com.example.org.controller to javafx.fxml;
    opens com.example.org.model to javafx.base;

    exports com.example.org;
}
```

- `opens ...controller to javafx.fxml`: permite ao FXML instanciar o controller e injetar os `@FXML`
- `opens ...model to javafx.base`: permite à tabela ler as propriedades de `Pessoa`

---

## Solução de problemas

| Sintoma | Causa provável |
|---|---|
| Botões não fazem nada | Falta `onAction` no FXML ou `fx:controller` na raiz |
| `NullPointerException` ao abrir a tela | Algum `fx:id` do FXML não bate com o nome no controller |
| `LoadException` / erro de módulo | Falta o `opens` do pacote `controller` no `module-info.java` |
| Tabela vazia após calcular | Colunas sem `fx:id` ou `TableView` sem `fx:id="tabela"` |
| Abre a tela do template | O `Main` ainda está carregando `hello-view.fxml` |

---

## Critérios do exercício atendidos

- **Funcionalidade:** inserção, cálculo, exibição e manipulação de arquivo
- **Interface gráfica:** formulário organizado, botões claros e tabela com dados
- **Persistência:** gravação e leitura em CSV (`dados_pessoas.txt`)
- **Boas práticas:** separação em classes (modelo, utilitário, controller, view) e tratamento de exceções (`IOException`, `NumberFormatException`)

---

## Melhorias futuras

- Tabela editável (editar nome, altura e peso com duplo clique)
- Campo de busca/filtro por nome
- Exportação para outros formatos
- Testes unitários para o cálculo e a classificação do IMC

---

## Autor

Desenvolvido por **@victorhugoEngineer**.
