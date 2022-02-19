/*
 *  Header
 */
package lorettischinoppi.grafi;

import java.util.ArrayList;
import java.util.Arrays;
import lorettischinoppi.controlli.NotFoundException;
import lorettischinoppi.controlli.RimuoviDuplicati;
import movida.commons.Collaboration;
import movida.commons.Movie;
import movida.commons.Person;

/**
 *
 * @author Andrea
 */
public class Grafo 
{
    RimuoviDuplicati R = new RimuoviDuplicati();

    public static Person[] PSN;
    ArrayList<Collaboration> colList=new ArrayList<>();
    
    /**
     * @param m è un array di film
     */
    public Grafo(Movie[] m)
    {
        CreaPSN(m);
    }
    
    /**
     * Crea un array contenente tutti gli attori, una volta sola
     * @param m è un array di film da cui ricavare gli attori
     */
    public void CreaPSN(Movie m[])
    {
        ArrayList<String> act = new ArrayList<>();
        
        for (int i = 0; i < m.length; i++)      //Scorro i film
        {
            for (int j = 0; j < m[i].getCast().length; j++) //Scorro i cast
            {
                act.add(m[i].getCast()[j].getName().trim().toLowerCase());   //Aggiungo i nomi degli attori alla lista
            }
        }
        
        String[] pplnm=new String[act.size()];
        
        for (int k = 0; k < act.size(); k++) 
        {
            pplnm[k]=act.get(k);    //Inserisco gli attori in un array
        }
        
        pplnm=R.rim_dup(pplnm);     //Rimuovo gli attori che sono stati trovati più di una volta
        
        PSN=new Person[pplnm.length];
        
        for (int j = 0; j < pplnm.length; j++) 
        {
            PSN[j]=new Person(pplnm[j]);    //Inserisco nell'array di tipo Person[] tutti gli attori precedentemente trovati
        }
    }
    
    /**
     * @param A è la matrice di adiacenza che rappresenta il grafo
     * @param Tocheck è l'inidce dell'attore di cui si vogliono consocere i collaboratori diretti
     * @return l'array degli indici corrispodenti ai collaboratori diretti dell'attore considerato
     */
    public int[] CheckCollab(int A[][],int Tocheck)
    {
        int C[]=new int[A.length];
        int NeedZero=1;
       
        for (int i = 0; i < A.length; i++) //Scorro la riga
        {
            if (A[i][Tocheck]==1)          //Se trovo 1 significa che gli attori hanno collaborato
            {
                if (i==0)
                {
                    NeedZero=0;
                }
                C[i]=i;                    //Inserisco l'indice della collaborazione nell'array
            }
        }
        int[] collab=Arrays.copyOfRange(R.rim_dup(C),NeedZero,R.rim_dup(C).length); //Array delle collaborazioni dirette
        return collab;
    }
    
    
    /**
    * @param A è la matrice di adiacenza che rappresenta il grafo
    * @param ind_attore è l'indice che corrisponde all'attore di cui si desiderano trovare i collaboratori
    * @return l'array di tutti gli indici degli attori collaboratori di ind_attore
    */
    public int[] CheckInd(int A[][], int ind_attore)
    {
        int row=ind_attore;
        ArrayList<Integer> col=new ArrayList<>();
        int NeedZero=1;
        int tempCol[];
        boolean checked[]=new boolean[A.length];
        
        for (int j = 0; j < A.length; j++)  //Scorro la matrice
        {
            if (A[row][j]!=0)               //Se trovo un collaboratore
            {                               //
                tempCol=CheckCollab(A,j);   //inserisco in un array le collaborazioni del collaboratore
            
                for (int k = 0; k < tempCol.length; k++) 
                {
                    col.add(tempCol[k]);        //Aggiungo le collaborazioni alla lista
                    col.add(j);                 //includendo anche il collaboratore trovato precedentemente
                }
                if (j==0) 
                {
                    NeedZero=0;
                }
            }         
        }
        
        int q =0;
        int y=0;
        while(q<A.length)
        {
            for (int i=col.get(0);i<checked.length && y<col.size()-1;i=col.get(y)) //Scorro le collaborazioni
            {
                if (checked[i]==false)  //Se non ho mai controllato un elemento
                {
                    int w=0;
                    for (w = 0; w < col.size(); w++) 
                    {
                        if (col.get(w)==i) //Ricavo la posizione che occupa nella lista
                        {
                            break;
                        }
                    }
                    tempCol=CheckCollab(A,col.get(w)); //Controllo anche le sue collaborazioni dirette
                    checked[i]=true;                   //Indico come controllato
                    
                    for (int j = 0; j < tempCol.length; j++) 
                    {
                        col.add(tempCol[j]);           //Aggiungo le nuove collaborazioni alla lista
                    }
                    if (i==0) 
                    {
                        NeedZero=0;
                    }
                }
                y++;
                
            }
            q++;
            y=0;
        }
        
        int I_coll[]=new int[col.size()];
        for (int i = 0; i < I_coll.length; i++) 
        {
            I_coll[i]=col.get(i);   //Creo l'array delle collaborazioni indirette
        }
        
       
        for (int v = 0; v < I_coll.length; v++) //Un attore non può collaborare con sè stesso
        {
            if (I_coll[v]==ind_attore) 
            {
                I_coll[v]=0;
                I_coll=R.rim_dup(I_coll);
            }
        }
        return Arrays.copyOfRange(I_coll, NeedZero, I_coll.length);
    }
    
    /**
     * Rimuove un attore dal grafo
     * @param A è la matrice di adiacenza che rappresenta il grafo
     * @param ToRem è l'indice dell'attore che si desidera rimuovere
     * @return la matrice di adiacenza senza l'attore rimosso
     */
    public int[][] RimAttore (int A[][], int ToRem)
    {
        int B[][]=new int[A.length-1][A.length-1];
        int R_B=0;
        int C_B=0;
        boolean skip=false;

        for (int i = 0; i<A.length; i++) 
        {
            C_B=0;
            skip=false;

            for (int j = 0; j < A.length; j++) 
            {
                if (i!= ToRem)      //
                {                   //Se non devo rimuovere l'attore
                    if (j!=ToRem)   //
                    {               
                        B[R_B][C_B]=A[i][j]; //Copio la cella nella matrice B
                        C_B++;   
                    } 
                }
                else
                {
                    skip=true;      //Indico di aver trovato e non copiato l'attore da rimuovere
                }
            }
            if (R_B<B.length-1 && skip == false) 
            {
                R_B++;
            }
        }
        return B;
    }
    
    /**
     * Crea la matrice di adiacenza composta da valori 0 e 1
     * @param m è l'array di film da cui ricavare le collaborazioni
     * @return la matrice di adiacenza
     */
    public int[][] CreaMat(Movie[] m)
    {
        int NAttori;

        NAttori=PSN.length;
        
        int[][] mat = new int[NAttori][NAttori];
        
        for (int i = 0; i < PSN.length; i++)    //Persone
        {
            for (int j = 0; j < m.length; j++)  //Film
            {
                for (int k = 0; k < m[j].getCast().length; k++)     //Cast
                {
                    if(m[j].getCast()[k].getName().trim().toLowerCase().compareTo(PSN[i].getName().trim().toLowerCase())==0)  //Se trovo l'attore considerato in qualsisasi cast
                    {
                        for (int l = 0; l < m[j].getCast().length; l++)             //Scorro il cast e lo inserisco nelle collaborazioni
                        {
                            Definecollab(mat,i,getindex(PSN,m[j].getCast()[l].getName().trim().toLowerCase()));
         
                        }
                        
                    }
                }
            }
        }
        for (int q = 0; q < mat.length; q++) //Rendo 0 le diagonali perchè un attore non può collaborare con sè stesso
        {
         mat[q][q]=0;   
        }
        
        return mat;
        
    }

    /**
     * Restituisce l'indice relativo ad una persona contrnuta in un array
     * @throws NotFoundException se la persona non è presente
     * @param p è l'array di persone considerato
     * @param name è il nome della persona di cui si vuole conoscere l'indice
     * @return l'indice della persona cercata
     */
    public int getindex(Person[] p,String name) 
    {
        for (int i = 0; i < p.length; i++) 
        {
            if (p[i].getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
            {
                return i;
            }
        }
        throw new NotFoundException();
    }
    
    /**
     * @param p è l'array di persone considerato
     * @param ind è l'indice richiesto
     * @return il nome della persona presente all'indice richiesto
     */
    public static String GetactName(Person[] p,int ind)
    {
        return p[ind].getName().trim().toLowerCase();
    }
    
    /**
     * @param G è la matrice di adiacenza rappresentante un grafo
     * @param da è il primo elemento della collaborazione
     * @param a è il secondo elemento della collaborazione
     */
    private void Definecollab(int G[][], int da, int a)
    {
        G[da][a]=1;
    }
    
    /**
     * Trasforma la matrice di adiacenza in input in una matrice contenente i punteggi delle collaborazioni
     * @param m è una matrice di adiacenza rappresentante un grafo
     * @param M è l'array di film da cui ricavare i punteggi
     * @return la mtrice di adiacenza contenente i punteggi
     */
    public Double[][] MatScores(int[][] m, Movie[] M)
    {
        Double[][] Dmat=new Double[m.length][m.length];
        ArrayList<Movie> mv = new ArrayList<>();
        int a=-1;
        boolean filmaggiunti=false;
        
        for (int i = 0; i < m.length; i++)      //Scorro la matrice delle collaborazioni
        {                                       //
            for (int j = 0; j < m.length; j++)  //
            {
                if (m[i][j]==0) 
                {
                    Dmat[i][j]=0.0;
                }
                else
                {
                    for (int k = 0; k < M.length; k++) //Scorro tutti i film
                    {
                        for (int l = 0; l < M[k].getCast().length; l++)    //Scorro il cast alla ricerca del primo attore
                        {
                            for (int n = 0; n < M[k].getCast().length; n++)    //Scorro il cast alla ricerca del secondo attore
                            {
                                if (M[k].getCast()[l].getName().trim().toLowerCase().equals(GetactName(PSN,i).trim().toLowerCase())&& 
                                        (M[k].getCast()[n].getName().trim().toLowerCase().equals(GetactName(PSN,j).trim().toLowerCase()))) 
                                {
                                   mv.add(M[k]);               //Se li trovo tutti e due in un film, lo aggiungo alla lista
                                    if (!filmaggiunti) 
                                    {
                                        a++;
                                    } 
                                    filmaggiunti=true;
                                }
                            }   
                        }  
                    }
                    colList.add(new Collaboration(PSN[i],PSN[j],mv));   //Creo la collaborazione
                    Dmat[i][j]=colList.get(a).getScore();               //Inserisco il punteggio della collaborazione nella matrice
                    mv.clear();
                    filmaggiunti=false;
                }
            }
        }
        return Dmat;
    }
    
    /**
     * Crea un albero di copertura minimale di valore massimo a partire da una matrice di adiacenza
     * @param cost è la matrice di adiacenza contenente i punteggi delle collaborazioni
     * @param col è l'array delle collaborazioni dell'attore che si sta considerando, dove l'attore considerato è aggiunto in coda
     * @return l'array di ICC dell'attore considerato, senza l'elenco dei film che saranno aggiunti successivamente
     */
    public static Collaboration[] MaxTree(Double cost[][], int col[]) 
    {  
        Double[][] temp=cost;   //Copio la matrice originale così da poterla modificare senza rischi
        int V =col.length;  
        int V2=temp.length; 
        boolean stuck = false;

        Collaboration[] collarray = new Collaboration[V-1]; //Inizializzo l'array delle collaborazioni
 
        int y=col.length-1; //Assegno a y l'ultimo valore dell'array delle collaborazioni

        int N_archi = 0;
        
        int j=0;
        int max1=0; //Memorizza il valore massimo precedentemente trovato
        
        ArrayList<Integer> in = new ArrayList<>();  //
        ArrayList<Integer> vis = new ArrayList<>(); //Nodi visitati
        
        while (N_archi < V-1)
        {  
            Double max = Double.MIN_VALUE;  //Inizializzo il massimo all'equivalente di meno infinito
            int a = -1, b = -1; 
            
            if (stuck)  //Se precendentemente ho finito gli archi da percorrere, ricomincio dal primo nodo
            {
                y=col.length-1;
                stuck=false;
            }
            
            for (int k = 0; k < in.size()-1; k++)       //Abbiamo notato che se si viene a creare
            {                                           //un pattern di collaborazioni successive
                if (in.get(k).equals(in.get(k+1)))      //del tipo 'ab' - 'bc' è necessario
                {                                       //evitare il collegamento 'ac' per preservare la
                    temp[in.get(k-1)][in.get(k+2)]=0.0; //minimalità dell'albero. In caso succeda poniamo il
                    temp[in.get(k+2)][in.get(k-1)]=0.0; //valore delle collaborazioni 'ac' e 'ca' pari a 0
                }
            }
            
            for (int i = col[y]; j < V2 && y<col.length;/**/)  
            { 
                for (j = 0; j < V2; j++)  
                {              
                    if (temp[i][j] > max)   //Se trovo un nuovo valore massimo
                    {
                        if (!vis.contains(j))// && j!=col[col.length-1])   //Se non l'ho già considerato
                        {
                            max = temp[i][j];   //Aggiorno il massimo
                            if (max1!=j)        //Se ho trovato un altro massimo
                            {
                                if (vis.contains(max1)) 
                                {
                                    vis.remove(vis.indexOf(max1));  //Rimuovo il precedente dai valori considerati
                                }
                            }
                            max1=j;   //Assegno a max1 ogni massimo che trovo
                            temp[i][j] =0.0;    //Pongo a 0 il valore delle 
                            temp[j][i] =0.0;    //collaborazioni già considerate

                            a = i;  //Assegno ad a e b i valori degli indici
                            b = j;  //in cui ho trovato il massimo
                            
                            if (a!=col[col.length-1]) //Se non si tratta di una collaborazione con l'attore inizialmente richiesto
                            {
                                vis.add(a); //Aggiungo a e b
                                vis.add(b); //ai nodi già considerati
                            }
                            else vis.add(b);    //Altrimenti aggiungo solo b

                            in.add(a);  //In ogni caso aggiungo a e b a questa lista per eseguire
                            in.add(b);  //il controllo che garantisce minimalità in seguito

                            y=getind(col,j);   //y diventa l'indice del collaboratore successivo
                        }
                    } 
                }
            }
            j=0;
            max1=0;
            
            if (a != -1 && b != -1)  //Se ho trovato un nuovo massimo e posso percorrere l'arco
            {
                Person[] tmpers = {};                                   //Creo un film ad hoc da inserire 
                Movie tmp = new Movie("a",1,2,tmpers,new Person("b"));  //in ogni collaborazione
                ArrayList<Movie> film = new ArrayList<>();              //(i film corretti saranno aggiunti successivamente
                film.add(tmp);                                          //grazie a searchMoviesStarredBy() presente in MovidaCore
        
                collarray[N_archi]=new Collaboration(PSN[a],PSN[b],film);   //Aggiungo le persone e il film creato ad hoc all'array di collaborazioni
                System.out.printf("Arco "+N_archi+": (" + a+", "+ b+")\t Punteggio: "+ max+"\n"); 
                N_archi++;
                film.clear();
            } 
            else stuck = true; //Se non trovo collaborazioni valide segnalo di essere bloccato
        }
        return collarray;
    }
    
    /**
     * @param col è l'array delle collaborazioni
     * @param val è il valore cercato nell'array
     * @throws NotFoundException se l'indice non è presente
     * @return l'indice in cui si trova val
     */
    public static int getind(int col[], int val)
    {
        for (int i = 0; i < col.length; i++) 
        {
            if (col[i]==val) 
            {
                return i;
            }
        }
        throw new NotFoundException();
    }
}