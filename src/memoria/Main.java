package memoria;

import memoria.algoritmo.AlgoritmoNRU;
import java.util.Arrays;

/**
 * Ponto de entrada: inicializa o console e dispara os processos.
 */
public class Main {
    public static void main(String[] args) {
        // Abre a janela-console
        Console.init();

        // Cria gerenciador com 3 quadros de RAM e swap de 10
        GerenciadorDeMemoria ger = new GerenciadorDeMemoria(
            3, 10, new AlgoritmoNRU()
        );

        // Dois processos com suas sequências de referências
        Processo p1 = new Processo(1,
            Arrays.asList("1-R","2-R","3-R","1-W","4-R","5-R"),
            ger);
        Processo p2 = new Processo(2,
            Arrays.asList("6-R","2-R","1-R","3-W","7-R"),
            ger);

        // Inicia ambos simultaneamente
        p1.start();
        p2.start();
    }
}
