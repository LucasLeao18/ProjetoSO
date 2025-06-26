
# Simulador de Gerenciamento de Memória Virtual com Política NRU

Este projeto em Java simula o gerenciamento de memória virtual por paginação, usando a política **Not Recently Used (NRU)** de substituição de páginas. Todas as operações (page-hit, page-fault, swap-in, swap-out e estado da RAM/disco) são exibidas em tempo real em um “console” Swing.

---

## 📋 Tabela de Conteúdos

1. [Visão Geral](#visão-geral)  
2. [Funcionalidades](#funcionalidades)  
3. [Estrutura de Diretórios](#estrutura-de-diretórios)  
4. [Pré-requisitos](#pré-requisitos)  
5. [Compilação](#compilação)  
6. [Execução](#execução)  
7. [Descrição Detalhada das Classes](#descrição-detalhada-das-classes)  
   - [Console](#1-console)  
   - [Pagina](#2-pagina)  
   - [Substituicao (interface)](#3-substituicao-interface)  
   - [AlgoritmoNRU](#4-algoritmonru)  
   - [MemoriaPrincipal](#5-memoriaprincipal)  
   - [MemoriaVirtual](#6-memoriavirtual)  
   - [GerenciadorDeMemoria](#7-gerenciadordememoria)  
   - [Processo](#8-processo)  
   - [Main](#9-main)  
8. [Fluxo de Execução](#fluxo-de-execução)  
9. [Política NRU – Como Funciona](#política-nru--como-funciona)  
10. [Concorrência e Sincronização](#concorrência-e-sincronização)  
11. [Possíveis Extensões](#possíveis-extensões)  

---

## Visão Geral

Este simulador recria, de forma didática, o comportamento de:

- **Memória Principal (RAM)** com número fixo de quadros.  
- **Memória Virtual (Swap)** com capacidade configurável.  
- **Processos concorrentes** acessando páginas (leitura/escrita).  
- **Console gráfico Swing** exibindo logs em tempo real.  
- **Política NRU** para escolher qual página descartar quando a RAM enche.

É útil para estudo de Sistemas Operacionais, especialmente em disciplinas de **Gerenciamento de Memória**.

---

## Funcionalidades

- 📄 **Paginação**: cada página é objeto `Pagina` com bits R/W e moldura.  
- 🔄 **Page-fault**: ao faltar página na RAM, simula swap-out e swap-in.  
- 📊 **Console Swing**: janela que exibe cada evento e o estado atual da RAM e do disco.  
- ⚙️ **Política NRU**: escolhe a “vítima” conforme bits Referenciada e Modificada.  
- 🔒 **Thread-safe**: acesso sincronizado no `GerenciadorDeMemoria` para simular múltiplos processos.

---

## Estrutura de Diretórios

```

src/
└─ memoria/
├─ algoritmo/
│   ├─ Substituicao.java      ← interface de política
│   └─ AlgoritmoNRU.java      ← implementação NRU
├─ Console.java               ← console Swing em tempo real
├─ Pagina.java                ← modelo de página
├─ MemoriaPrincipal.java      ← simula a RAM
├─ MemoriaVirtual.java        ← simula o swap/disco
├─ GerenciadorDeMemoria.java  ← coordena RAM, swap e algoritmo
├─ Processo.java              ← thread que referencia páginas
└─ Main.java                  ← ponto de entrada da simulação
README.md                          ← este arquivo

````

---

## Pré-requisitos

- **Java Development Kit (JDK) 8+**  
- IDE ou editor de sua preferência (Eclipse, IntelliJ, VSCode…)  
- Memória mínima: ideias em 512 MB de heap para visualização via Swing

---

## Compilação

Abra um terminal na pasta `src` e rode:

```bash
javac memoria/Console.java \
      memoria/algoritmo/*.java \
      memoria/*.java
````

Isso gerará os arquivos `.class` correspondentes.

---

## Execução

Ainda no diretório `src`, execute:

```bash
java memoria.Main
```

1. Uma janela “Console de Operações” aparecerá.
2. Dois processos (IDs 1 e 2) iniciarão referências às páginas em paralelo.
3. Você verá no console cada passo:

   * **Falta de página**, **Creação** de nova página
   * **Hit** (quando a página já está em RAM)
   * **Swap-out** (remoção de página da RAM para o disco)
   * **Swap-in** (trazer página do disco para a RAM)
   * Exibição dos estados atuais da RAM e do disco
   * Linha de separação “───” após cada acesso

---

## Descrição Detalhada das Classes

### 1. Console

* **Pacote**: `memoria`
* **Responsabilidade**: criar uma janela Swing com `JTextArea` e método estático `log(String)` para imprimir mensagens.
* **Uso**: todas as classes chamam `Console.log(...)` em vez de `System.out.println`.

### 2. Pagina

* **Pacote**: `memoria`
* **Atributos** (privados):

  * `id`, `processoId` (imútaveis)
  * `referenciada`, `modificada`, `presente`, `moldura`
* **Métodos**:

  * Getters e setters de bits
  * `toString()` retorna “P{processoId}P{id}” para exibição.

### 3. Substituicao (interface)

* **Pacote**: `memoria.algoritmo`
* **Método único**:

  ```java
  Pagina substituir(List<Pagina> paginas);
  ```
* **Objetivo**: definir o contrato para políticas de substituição de páginas.

### 4. AlgoritmoNRU

* **Pacote**: `memoria.algoritmo`
* **Implementa**: `Substituicao`
* **Estratégia**:

  1. Divide as páginas em 4 classes por bits R/W:

     * Classe 0: R=0, W=0
     * Classe 1: R=0, W=1
     * Classe 2: R=1, W=0
     * Classe 3: R=1, W=1
  2. Escolhe a primeira classe não vazia (menor penalidade).
  3. Embaralha a lista dessa classe para desempate aleatório.
  4. Retorna a página vítima.

### 5. MemoriaPrincipal

* **Pacote**: `memoria`
* **Atributos**:

  * `tamanho`: número máximo de quadros
  * `paginas`: `List<Pagina>` residentes
* **Métodos**:

  * `contem(Pagina)`: verifica se já está em RAM
  * `adicionar(Pagina)`: marca presente, atribui moldura e loga
  * `remover(Pagina)`: retira da lista e loga
  * `mostrar()`: exibe lista completa

### 6. MemoriaVirtual

* **Pacote**: `memoria`
* **Semelhante à RAM**, porém:

  * Armazena páginas não residentes
  * Lança exceção se o disco encher
  * Não gerencia molduras físicas

### 7. GerenciadorDeMemoria

* **Pacote**: `memoria`
* **Atributos**:

  * `MemoriaPrincipal ram`
  * `MemoriaVirtual disco`
  * `Substituicao algoritmo`
* **Método-chave**:

  ```java
  public synchronized void acessarPagina(String operacao, int pid)
  ```

  1. Faz parse de `"X-R"` ou `"X-W"`.
  2. Checa **page-hit** (em RAM) ou **page-fault**.
  3. Se RAM cheia, invoca `algoritmo.substituir(...)`, swap-out e swap-in.
  4. Marca bits R/W.
  5. Atualiza logs: falta de página, hit, swap, estado de RAM/disco.

### 8. Processo

* **Pacote**: `memoria`
* **Implementa**: `Runnable`
* **Função**: iterar sobre `List<String> operacoes` (e.g. `"4-R"`, `"5-W"`), chamar `gerenciador.acessarPagina`, e dormir 100 ms entre cada operação.
* **Método extra**: `start()` para criar e iniciar a Thread.

### 9. Main

* **Pacote**: `memoria`
* **Ponto de Entrada**:

  1. `Console.init()` abre a UI.
  2. Instancia `GerenciadorDeMemoria(3, 10, new AlgoritmoNRU())`.
  3. Cria e inicia dois processos com sequências predefinidas.

---

## Fluxo de Execução

1. **Inicialização**: Swing Console + Gerenciador de Memória.
2. **Processos Concorrentes**: threads p1 e p2 disparam acessos.
3. **Acesso a Página**:

   * Se está em RAM → **Hit**
   * Se não → **Page-fault**, possivelmente **Swap-out** + **Swap-in** ou criação de nova página
4. **Marcação de Bits**: R = sempre, W = apenas em escrita
5. **Logs**: cada passo e estado das memórias são exibidos em tempo real.

---

## Política NRU – Como Funciona

* **R (Referenced)**: indica se a página foi acessada recentemente.
* **W (Modified)**: indica se foi escrita desde que entrou na RAM.
* **Classes de Vítima**:

  1. R=0, W=0 (menor custo)
  2. R=0, W=1
  3. R=1, W=0
  4. R=1, W=1 (maior custo)
* **Seleção**: escolhe a menor classe não vazia e remove aleatoriamente uma página dela.

---

## Concorrência e Sincronização

* `acessarPagina(...)` é `synchronized` para garantir **consistência** das estruturas (RAM e disco) quando múltiplas threads acessam ao mesmo tempo.
* O Console usa `SwingUtilities.invokeLater(...)` para atualizar a UI sem bloquear a Event Dispatch Thread.

---

## Possíveis Extensões

* 🔄 **Reset periódico do bit R** para simular o “clock” NRU real.
* ⚙️ **Novas políticas**: LRU, FIFO, Clock, trabalhando com a mesma interface `Substituicao`.
* 🎛️ **Configuração via GUI**: permitir ao usuário ajustar tamanhos de RAM, swap e delays dinamicamente.
* 📈 **Visualização gráfica**: desenhar quadros da RAM/disco em painéis separados.
* 📝 **Relatórios**: exportar logs para CSV ou HTML para análise posterior.

---

> **Desfrute da simulação!**
> Qualquer dúvida ou sugestão, abra uma *issue* ou envie um pull request.

```
```
