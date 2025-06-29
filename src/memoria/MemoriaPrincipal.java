package memoria;

import java.util.List;
import java.util.ArrayList;

/**
 * Simula a RAM, armazenando páginas residentes.
 */
public class MemoriaPrincipal {
    private final int tamanho;
    private final List<Pagina> paginas;

    public MemoriaPrincipal(int tamanho) {
        this.tamanho = tamanho;
        this.paginas = new ArrayList<>();
    }

    public int getTamanho() { return tamanho; }
    public List<Pagina> getPaginas() { return paginas; }

    public boolean contem(Pagina pg) {
        return paginas.stream()
            .anyMatch(p -> p.getId() == pg.getId()
                        && p.getProcessoId() == pg.getProcessoId());
    }

    public Pagina getPagina(Pagina pg) {
        return paginas.stream()
            .filter(p -> p.getId() == pg.getId()
                      && p.getProcessoId() == pg.getProcessoId())
            .findFirst().orElse(null);
    }

    public boolean cheia() {
        return paginas.size() >= tamanho;
    }

    public void adicionar(Pagina pg) {
        paginas.add(pg);
        pg.setPresente(true);
        pg.setMoldura(paginas.indexOf(pg));
        Console.log("→ Carregou na RAM: " + pg);
    }

    public void remover(Pagina pg) {
        paginas.removeIf(p -> p.getId() == pg.getId()
                          && p.getProcessoId() == pg.getProcessoId());
        pg.setPresente(false);
        Console.log("→ Removeu da RAM: " + pg);
    }

    public void mostrar() {
        Console.log("Estado RAM: " + paginas);
    }
}
