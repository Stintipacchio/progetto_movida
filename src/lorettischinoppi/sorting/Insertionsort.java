package lorettischinoppi.sorting;

import movida.commons.Movie;

/**
 * @author Andrea
 * @param <T>
 */

public class Insertionsort<T extends Comparable>
{
    public void Isort_minmax(T[] data)
    {
        int n = data.length;
        for (int j = 1; j < n; j++) 
        {
            T key = data[j];    //Parto confrontando l'elemento 1 con quello in posizione 0 
            int i = j-1;        //

            while ( (i >-1) && (data[i].compareTo(key)>0) ) //L'elemento i dell'array è maggiore dell'elemento j (Key)
            {
                data[i+1] = data[i]; //Scambio gli elementi
                i--;
            }
            data[i+1] = key;    //Metto Key al posto giusto
        }
    }

    public void Isort_maxmin(T[] data)
    {
        int n = data.length;
        for (int j = 1; j < n; j++) 
        {
            T key = data[j];
            int i = j-1;
            while ( (i > -1) && (data[i].compareTo(key)<0) ) 
            {
                data [i+1] = data [i];
                i--;
            }
            data[i+1] = key;
        }
    }

    public void GetArrayMostVoted_Recent(Movie[] data, boolean choose)
    {
        int n = data.length;
        for (int j = 1; j < n; j++) 
        {
            Movie key = data[j];
            int i = j-1;
            if(choose==true) //choose viene utilizzato per la scelta del tipo di valori da organizzare per voti o per anno di rilascio
            while ( (i > -1) && (data[i].getVotes().compareTo(key.getVotes())<0) ) 
            {
                data [i+1] = data [i];
                i--;
            }
            else
            while ( (i > -1) && (data[i].getYear().compareTo(key.getYear())<0) ) 
            {
                data [i+1] = data [i];
                i--;
            }
            data[i+1] = key;
        }
    }
}