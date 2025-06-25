
1. Visão Geral
Objetivo: simular o gerenciamento de memória virtual com página­ção e política NRU, exibindo em tempo real cada evento (hit, falta de página, swap, estado da RAM e do disco) em um console Swing.

Fluxo principal:

Main → inicializa o console e dispara dois Processo em threads distintas.

Cada Processo gera uma sequência de operações (leitura/escrita) que invocam GerenciadorDeMemoria.acessarPagina().

O GerenciadorDeMemoria coordena a MemoriaPrincipal (RAM) e a MemoriaVirtual (swap), aplicando a política NRU para decidir qual página remover quando a RAM está cheia.

Todas as ações e estados são reportados em tempo real por Console.log(), que escreve num JTextArea dentro de um JFrame.

2. Diagrama de Classes (simplificado)
scss
Copiar
Editar
Main ──▶ Console
   │
   ├─▶ GerenciadorDeMemoria ─┬─▶ MemoriaPrincipal
   │                         ├─▶ MemoriaVirtual
   │                         └─▶ Substituicao (interface)
   │                              └─▶ AlgoritmoNRU (implementação)
   │
   └─▶ Processo (Thread)
3. Componentes Detalhados
3.1 Console
Classe: memoria.Console

Responsabilidade: abrir uma janela Swing com um JTextArea e oferecer método estático log(String) para imprimir linhas.

Como funciona:

init() cria o JFrame, adiciona um JScrollPane que envolve um JTextArea não editável.

log(...) chama SwingUtilities.invokeLater(...) para garantir atualização segura da UI e faz scroll automático ao final.

3.2 Página
Classe: memoria.Pagina

Atributos:

id e processoId (imutáveis)

referenciada, modificada, presente, moldura (bits e posição)

Métodos: getters, setters de bits e toString() para exibir “P{pid}P{id}”.

3.3 Política de Substituição (NRU)
Interface: memoria.algoritmo.Substituicao

Implementação: memoria.algoritmo.AlgoritmoNRU

Agrupa páginas em quatro classes segundo bits R/W:

R=0, W=0

R=0, W=1

R=1, W=0

R=1, W=1

Escolhe a primeira classe não vazia, embaralha (para desempate) e retorna a vítima.

3.4 Memória Principal (RAM)
Classe: memoria.MemoriaPrincipal

Atributos:

tamanho (nº máximo de quadros)

paginas (lista de Pagina residentes)

Métodos:

contem(Pagina) — verifica por id+pid

adicionar(Pagina) — marca presente, define moldura e loga no console

remover(Pagina) — retira da lista, marca ausente e loga

mostrar() — imprime lista atual

3.5 Memória Virtual (Swap)
Classe: memoria.MemoriaVirtual

Funcionamento muito parecido com a RAM, mas armazena páginas não residentes e não tem molde físico (sempre presente=false).

3.6 Gerenciador de Memória
Classe: memoria.GerenciadorDeMemoria

Atributos: instâncias de MemoriaPrincipal, MemoriaVirtual e Substituicao (NRU).

Método-chave:

java
Copiar
Editar
public synchronized void acessarPagina(String operacao, int pid)
Fluxo interno:

Parse de "X-R" ou "X-W" em paginaId e tipoOp.

Cria um objeto temporário Pagina(paginaId, pid).

Página em RAM?

Sim: é hit → recupera a instância real da lista.

Não: é page fault →
a. Se a RAM está cheia, chama algoritmo.substituir(...) para eleger vítima.
b. Move a vítima da RAM para o swap (disco.adicionar(vitima)).
c. Se a página requerida já está no swap, faz swap-in (remove do disco).
d. Caso contrário, cria nova página.
e. Adiciona a página na RAM (ram.adicionar(...)).

Ajusta bits: setReferenciada(true) e, se for 'W', setModificada(true).

Exibe estado atual de RAM e disco via ram.mostrar() e disco.mostrar().

Insere linha de separação no console para legibilidade.

Nota: o método é synchronized para impedir que duas threads façam alterações concorrentes na mesma estrutura de dados.

3.7 Processo (Thread)
Classe: memoria.Processo implementa Runnable.

Construtor recebe id, List<String> operacoes e o GerenciadorDeMemoria.

run(): itera sobre cada operação, chama gerenciador.acessarPagina(op, id) e faz Thread.sleep(100) para espalhar as ações no tempo.

start(): método auxiliar que cria e inicia uma nova Thread(this).

4. Sequência de Execução
Main.main()

Chama Console.init() → abre janela de log.

Cria um GerenciadorDeMemoria(3, 10, new AlgoritmoNRU()).

Instancia dois processos p1 e p2 com suas listas de acessos.

Chama p1.start() e p2.start() quase simultaneamente.

Cada Processo gera chamadas concorrentemente a acessarPagina(). Graças ao synchronized, cada chamada é executada por vez, mas as threads alternam entre si, simulando execução paralela.

No console Swing aparecem, em ordem cronológica:

“Falta de página: P…”, “Criando nova página”, “Carregou na RAM”

“Hit: P…” quando a página já estiver carregada

“Removeu da RAM: P…” e “Gravou no disco: P…” em situações de swap-out

“Swap-in: P…” quando a página retorna do disco

“Estado RAM: […]” e “Estado Disco: […]” após cada acesso

Linhas “———” dividindo cada acesso para facilitar a leitura.

5. Pontos de Extensão e Melhorias
Reset periódico do bit R (para NRU fiel): implementar um timer que zere referenciada em intervalos fixos.

Outras políticas: criar classes como AlgoritmoLRU ou AlgoritmoFIFO implementando Substituicao.

Parâmetros configuráveis: oferecer opções de tamanho de RAM, disco e delay via linha de comando ou GUI de setup.

Visualização gráfica: além do console, desenhar quadros da RAM e do disco em painéis Swing.

Com essa arquitetura, você tem uma simulação completa, thread-safe e interativa do gerenciamento de memória virtual com páginação e política NRU.
