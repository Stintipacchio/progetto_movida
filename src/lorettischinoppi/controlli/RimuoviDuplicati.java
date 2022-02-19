/*
 *  Header
 */
package lorettischinoppi.controlli;

import java.util.Arrays;
import movida.commons.Person;

/**
 *
 * @author Andrea
 */
public class RimuoviDuplicati 
{
    
  /**
 * Rimuove i duplicati dall'array dato dopo averlo ordinato
 * @param A è l'array da cui si vogliono rimuovere i duplicati
 * @return l'array senza duplicati, ridotto alla dimensione strettamente necesssaria a contenere gli elementi
 */
    public int[] rim_dup(int A[])
    {
        if (A.length ==0)
        {
            return A;
        }
        Arrays.sort(A);
        int[] tmp = new int[A.length];
        int j = 0;
        for (int i=0; i<A.length-1; i++)
        
            if (A[i]!=A[i+1])
            {
                tmp[j] = A[i];
                j++;
            }
        
        tmp[j++] = A[A.length-1];
        int i=0;

        for (i=0; i<j; i++) 
        
            A[i] = tmp[i];
        
        return Arrays.copyOfRange(A, 0, j);    
    }    
    
    public Object[] rim_dup(Object A[])
    {
        if (A.length ==0)
        {
            return A;
        }
        Arrays.sort(A);
        Object[] tmp = new Object[A.length];
        int j = 0;
        for (int i=0; i<A.length-1; i++)
        
            if (!(A[i].equals(A[i+1])))
            {
                tmp[j] = A[i];
                j++;
            }
        tmp[j++] = A[A.length-1];
        return Arrays.copyOfRange(tmp, 0, j);
    }

    public String[] rim_dup(String A[])
    {
        if (A.length ==0)
        {
            return A;
        }
        Arrays.sort(A);
        String[] tmp = new String[A.length];
        int j = 0;
        for (int i=0; i<A.length-1; i++)

            if (!(A[i].trim().toLowerCase().equals(A[i+1].trim().toLowerCase())))
            {
                tmp[j] = A[i];
                j++;
            }
        tmp[j++] = A[A.length-1];
        return Arrays.copyOfRange(tmp, 0, j);
    }    
}
