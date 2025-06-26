package memoria.algoritmo;

import memoria.Pagina;
import java.util.List;

/**
 * Contrato para políticas de substituição de páginas.
 */
public interface Substituicao {
    Pagina substituir(List<Pagina> paginas);
}
