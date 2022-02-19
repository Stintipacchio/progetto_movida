/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lorettischinoppi.sorting;

import movida.commons.Movie;

/**
 * @author Andrea
 * @param <T>
 */
public class Heapsort<T extends Comparable>
{
    void heapify(T arr[], int n, int i) 
    { 
        int largest = i;  // inizializzo largest come radice 
        int sx = 2*i + 1;  // definisco figlio sinistro
        int dx = 2*i + 2;  // definisco figlio destro 
  
        // Se il figlio sinistro è maggiore della radice la sovrascrivo col figlio sinistro 
        if ((sx < n) && (arr[sx].compareTo(arr[largest])>0))
            largest = sx; 
  
        // Se il figlio destro è maggiore della radice la sovrascrivo col figlio destro
        if (dx < n && arr[dx].compareTo(arr[largest])>0) 
            largest = dx; 
  
        // Se largest non è la radice effettuo lo swap
        if (largest != i) 
        { 
            T swap = arr[i]; 
            arr[i] = arr[largest]; 
            arr[largest] = swap; 
  
            // Richiamo la funzione ricorsivamente
            heapify(arr, n, largest); 
        } 
    } 
      
    public void Hsort(T arr[]) 
    { 
        int n = arr.length; 
  
        // Creo l'heap 
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapify(arr, n, i); 
  
        // estraggo tutti gli elementi dall'heap 
        for (int i=n-1; i>=0; i--) 
        { 
            // sposto la radice alla fine dell'array
            T temp = arr[0]; 
            arr[0] = arr[i]; 
            arr[i] = temp; 
  
            // chiamo heapify sull'heap modificato
            heapify(arr, i, 0); 
        } 
    }
    
    void heapifyMovie(Movie arr[], int n, int i, boolean choose) 
    { 
        int largest = i; 
        int sx = 2*i + 1;  
        int dx = 2*i + 2;
        
        if(choose==true)//choose viene utilizzato per la scelta del tipo di valori da organizzare per voti o per anno di rilascio
        {
            if ((sx < n) && (arr[sx].getVotes().compareTo(arr[largest].getVotes())<0))
                largest = sx; 

            if (dx < n && arr[dx].getVotes().compareTo(arr[largest].getVotes())<0) 
                largest = dx; 
        }
        else
        {
            if ((sx < n) && (arr[sx].getYear().compareTo(arr[largest].getYear())<0))
                largest = sx; 

            if (dx < n && arr[dx].getYear().compareTo(arr[largest].getYear())<0) 
                largest = dx; 
 
        }
        if (largest != i) 
        { 
            Movie swap = arr[i]; 
            arr[i] = arr[largest]; 
            arr[largest] = swap; 
  
            heapifyMovie(arr, n, largest, choose); 
        } 
    }     
    
    public void GetArrayMostVoted_Recent(Movie[] data, boolean choose)
    {
        int n = data.length; 
  
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapifyMovie(data, n, i, choose); 
  
        for (int i=n-1; i>=0; i--) 
        { 
            Movie temp = data[0]; 
            data[0] = data[i]; 
            data[i] = temp; 
  
            heapifyMovie(data, i, 0, choose); 
        }         
    }
}
