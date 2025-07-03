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
            15, 30, new AlgoritmoNRU()
        );
        List<String> operacoes1 = Arrays.asList(new FabricaDeEntradas(15).getNewEntrada().split(","));
        List<String> operacoes2 = Arrays.asList(new FabricaDeEntradas(15).getNewEntrada().split(","));
        List<String> operacoes3 = Arrays.asList(new FabricaDeEntradas(15).getNewEntrada().split(","));
        List<String> operacoes4 = Arrays.asList(new FabricaDeEntradas(15).getNewEntrada().split(","));

        Processo p1 = new Processo(1, operacoes1, ger);
        Processo p2 = new Processo(2, operacoes2, ger);
        Processo p3 = new Processo(3, operacoes3, ger);
        Processo p4 = new Processo(4, operacoes4, ger);


        p1.start();
        p2.start();
        p3.start();
        p4.start();
    }
}
