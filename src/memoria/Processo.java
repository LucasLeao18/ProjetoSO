package memoria;

import java.util.List;

/**
 * Representa um processo (Thread) que referencia páginas.
 */
public class Processo extends Thread {
    private final int id;
    private final List<String> operacoes;
    private final GerenciadorDeMemoria ger;

    public Processo(int id, List<String> operacoes, GerenciadorDeMemoria gerenciador) {
        super("Processo-" + id);
        this.id = id;
        this.operacoes = operacoes;
        this.ger = gerenciador;
    }

    @Override
    public void run() {
        for (String op : operacoes) {
            String[] partes = op.split("-");

            int paginaId = Integer.parseInt(partes[0]);
            char tipo = partes[1].charAt(0);

            if (tipo == 'R') {
                ger.acessarPagina(paginaId, 'R', id, null);
            } else {
                int valor = Integer.parseInt(partes[2]);
                ger.acessarPagina(paginaId, 'W', id, valor);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
