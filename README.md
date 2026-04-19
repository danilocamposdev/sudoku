# Sudoku

[![Java 25](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

Aplicação do Sudoku desenvolvida em Java focada na lógica algorítmica e abstração. O projeto implementa algoritmos de backtracking para garantir a geração de tabuleiros com soluções únicas e com níveis de dificuldade diferentes.

## 💻 Tecnologias

Este projeto foi desenvolvido utilizando as seguintes tecnologias:

- [Java 25](https://www.oracle.com/java/) - Linguagem utilizada.
- [Maven](https://maven.apache.org/) - Gerenciador de dependências.

## ⚙ Funcionalidades

- **Geração Dinâmica:** Cria novos tabuleiros aleatórios com níveis de dificuldade variados.
- **Algoritmo de Backtracking:** Garante que cada tabuleiro gerado possui uma solução válida e única.
- **Validação das jogadas:** Verifica a legalidade das jogadas conforme as regras do Sudoku.

## 🎮 Como Rodar

### Pré-requisitos

Para executar este projeto, você precisará ter instalado:

- JDK 25 ou superior.
- Maven 4.0+.

### Instalação e Execução

1. Clone o repositório:

   ```bash
   git clone https://github.com/danilocamposdev/sudoku.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd sudoku
   ```

3. Compile e execute o projeto via Maven:

   ```bash
   mvn exec:java -Dexec.mainClass="com.danilocampos.App"
   ```
