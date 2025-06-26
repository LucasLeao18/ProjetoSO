package memoria;

import java.util.List;

/**
 * Representa um processo (Thread) que referencia páginas.
 */
public class Processo extends Thread {
    private final int id;
    private final List<String> operacoes;
    private final GerenciadorDeMemoria ger;

    public Processo(int id,
                    List<String> operacoes,
                    GerenciadorDeMemoria gerenciador) {
        this.id         = id;
        this.operacoes  = operacoes;
        this.ger        = gerenciador;
    }

    @Override
    public void run() {
        for (String op : operacoes) {
            ger.acessarPagina(op, id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
