/*
 *  Header
 */
package lorettischinoppi.Map;

import movida.commons.Movie;


/**
 *
 * @author Andrea
 */
public class Nodo 
{   
    public final int t=6;
    public final int MIN_KEYS = t-1;   
    public final int MAX_KEYS = 2*t-1; 
    public final int MAX_FIGLI = 2*t;  

        int N_keys;
        Movie[] keys;
        Nodo[] Figlio;
        boolean isLeaf;
        Nodo padre;
        
        public Nodo()
        {}

        public Nodo(Nodo genitore)
        {
            this.padre = genitore;
            keys =new Movie[MAX_KEYS];
            Figlio = new Nodo[MAX_FIGLI];
            isLeaf = true;
            N_keys = 0; 
        }

        public Movie getValue(int indice)
        {
            return keys[indice];
        }

        public Nodo getChild(int indice)
        {
            return Figlio[indice];
        }
}
