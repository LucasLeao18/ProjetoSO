package memoria;

import memoria.algoritmo.AlgoritmoNRU;
import java.util.Arrays;

/**
 * Ponto de entrada: inicializa o console e dispara os processos.
 */
public class Main {
    public static void main(String[] args) {
        Console.init();

        GerenciadorDeMemoria ger = new GerenciadorDeMemoria(
            3, 10, new AlgoritmoNRU()
        );

        Processo p1 = new Processo(1,
            Arrays.asList("1-R", "2-R", "3-W-99", "1-W-5", "4-R", "5-R"),
            ger);

        Processo p2 = new Processo(2,
            Arrays.asList("6-R", "2-R", "1-R", "3-W-8", "7-R"),
            ger);

        p1.start();
        p2.start();
    }
}
