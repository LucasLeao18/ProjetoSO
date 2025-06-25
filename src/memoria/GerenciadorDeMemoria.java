package memoria;

import memoria.algoritmo.Substituicao;

/**
 * Orquestra RAM e swap, aplicando a política NRU.
 */
public class GerenciadorDeMemoria {
    private final MemoriaPrincipal ram;
    private final MemoriaVirtual disco;
    private final Substituicao algoritmo;

    public GerenciadorDeMemoria(int tamRam,
                                int tamSwap,
                                Substituicao algoritmo) {
        this.ram       = new MemoriaPrincipal(tamRam);
        this.disco     = new MemoriaVirtual(tamSwap);
        this.algoritmo = algoritmo;
    }

    public synchronized void acessarPagina(String operacao, int pid) {
        String[] parts  = operacao.split("-");
        int    pgId     = Integer.parseInt(parts[0]);
        char   tipoOp   = parts[1].charAt(0);
        Pagina pagina   = new Pagina(pgId, pid);

        if (ram.contem(pagina)) {
            // page-hit
            pagina = ram.getPaginas().stream()
                .filter(p -> p.getId()==pgId && p.getProcessoId()==pid)
                .findFirst().get();
            Console.log("Hit → " + pagina);
        } else {
            // page-fault
            Console.log("Falta de página: " + pagina);
            if (ram.getPaginas().size() >= ram.getTamanho()) {
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
        }

        // marca R/W
        pagina.setReferenciada(true);
        if (tipoOp == 'W') pagina.setModificada(true);

        ram.mostrar();
        disco.mostrar();
        Console.log("------------------------------------------------");
    }
}
