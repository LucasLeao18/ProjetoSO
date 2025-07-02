package memoria;

import java.util.List;
import java.util.ArrayList;

/**
 * Simula o swap/disco para páginas não-residentes.
 */
public class MemoriaVirtual {
    private final int tamanho;
    private final List<Pagina> paginas;

    public MemoriaVirtual(int tamanho) {
        this.tamanho = tamanho;
        this.paginas = new ArrayList<>();
    }

    public boolean contem(Pagina pg) {
        return paginas.stream()
            .anyMatch(p -> p.getId()==pg.getId()
                        && p.getProcessoId()==pg.getProcessoId());
    }

    public void adicionar(Pagina pg) {
        if (paginas.size() >= tamanho){
            throw new RuntimeException("Swap cheio!");
         } else{
        paginas.add(pg);
        pg.setPresente(false);
        Console.log("→ Gravou no disco: " + pg);
         }
    }

    public void remover(Pagina pg) {
        paginas.removeIf(p -> p.getId()==pg.getId()
                          && p.getProcessoId()==pg.getProcessoId());
        Console.log("→ Removeu do disco: " + pg);
    }

    public void mostrar() {
        Console.log("Estado Disco: " + paginas);
    }
}
