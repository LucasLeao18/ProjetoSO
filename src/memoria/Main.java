package memoria;

import memoria.algoritmo.AlgoritmoNRU;
import java.util.Arrays;
import java.util.List;

/**
 * Ponto de entrada: inicializa o console e dispara os processos.
 */
public class Main {
    public static void main(String[] args) {
        Console.init();

        GerenciadorDeMemoria ger = new GerenciadorDeMemoria(
            3, 10, new AlgoritmoNRU()
        );
        String entrada = new FabricaDeEntradas(10).getNewEntrada();
        List<String> operacoes = Arrays.asList(entrada.split(","));

        Processo p1 = new Processo(1, operacoes, ger);
        Processo p2 = new Processo(2, operacoes, ger);
        Processo p3 = new Processo(3, operacoes, ger);
        Processo p4 = new Processo(4, operacoes, ger);


        p1.start();
        p2.start();
        p3.start();
        p4.start();
    }
}
