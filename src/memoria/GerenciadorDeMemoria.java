package memoria;

import memoria.algoritmo.Substituicao;

/**
 * Orquestra RAM e swap, aplicando a política NRU.
 */
public class GerenciadorDeMemoria {
    private final MemoriaPrincipal ram;
    private final MemoriaVirtual disco;
    private final Substituicao algoritmo;

    public GerenciadorDeMemoria(int tamRam, int tamSwap, Substituicao algoritmo) {
        this.ram = new MemoriaPrincipal(tamRam);
        this.disco = new MemoriaVirtual(tamSwap);
        this.algoritmo = algoritmo;
    }

    public synchronized void acessarPagina(int paginaId, char tipo, int processoId, Integer valor) {
        Pagina pagina = new Pagina(paginaId, processoId);

        if (!ram.contem(pagina)) {
            Console.log("Falta de página: " + pagina);

            if (ram.cheia()) {
                Pagina vitima = algoritmo.substituir(ram.getPaginas());
                ram.remover(vitima);
                disco.adicionar(vitima);
            }

            if (disco.contem(pagina)) {
                disco.remover(pagina);
                Console.log("→ Swap-in: " + pagina);
            } else {
                Console.log("→ Criando nova página: " + pagina);
            }

            ram.adicionar(pagina);
        } else {
            pagina = ram.getPagina(pagina); // garantir referência
            Console.log("Page hit: " + pagina);
        }

        pagina.setReferenciada(true);

        if (tipo == 'W') {
            pagina.setModificada(true);
            Console.log("→ Escrita de valor " + valor + " em " + pagina);
        } else {
            Console.log("→ Leitura de " + pagina);
        }

        ram.mostrar();
        disco.mostrar();
        Console.log("------------------------------------------------");
    }

    public MemoriaPrincipal getRam() {
        return ram;
    }

    public Substituicao getAlgoritmo() {
        return algoritmo;
    }
}
