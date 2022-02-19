package lorettischinoppi;

import movida.commons.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import lorettischinoppi.Map.*;
import lorettischinoppi.Map.ABR.Node;
import static movida.commons.MapImplementation.*;
import static movida.commons.SortingAlgorithm.*;
import lorettischinoppi.controlli.*;
import lorettischinoppi.grafi.Grafo;
import lorettischinoppi.sorting.Heapsort;
import lorettischinoppi.sorting.Insertionsort;
/**
 * @author Andrea
 */
public class IMovidaCore implements IMovidaConfig, IMovidaDB, IMovidaSearch, IMovidaCollaborations
{
    @SuppressWarnings("unchecked")
    
    SortingAlgorithm CurrentSort = null;
    MapImplementation CurrentMap=null;
    ControlloFormatoMovida controller = new ControlloFormatoMovida();
    WorkonStr D = new WorkonStr();
    RimuoviDuplicati RD = new RimuoviDuplicati();
    Insertionsort IS=new Insertionsort();
    Heapsort HS=new Heapsort();
    Grafo G;
    
    ABR<Movie> BST = new ABR<>();
    Btree<Movie> BT = new Btree<>();
    
    public String campi[]=
    {        
        "Title: ",	
        "Year: ",
        "Director: ",
        "Cast: ",
        "Votes: "
    };
    
    public Movie[] M;
    ArrayList<Movie> Movies = new ArrayList<>();
    
    @Override
    public boolean setSort(SortingAlgorithm a)
    {
        if (CurrentSort==a)
        {
            return false; //La configurazione non è stata modificata
        }
        if (a == HeapSort)
        {
            CurrentSort=HeapSort;
            return true; //Configurazione modificata
        }
        
        else if (a == InsertionSort)
        {
            CurrentSort=InsertionSort;
            return true; //Configurazione modificata
        }
        else
        {
            throw new NotAValidAlgorithmException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean setMap(MapImplementation m) {
        if (CurrentMap==m)
        {
            return false; //La configurazione non è stata modificata
        }
        if (m == ABR)
        {
            CurrentMap=ABR;
            return true; //Configurazione modificata
        }
        
        else if (m == BTree)
        {
            CurrentMap=BTree;
            return true; //Configurazione modificata
        }
        else
        {
            throw new NotAValidDictionaryException();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void loadFromFile(File f) 
    {
        controller.ControlloMovida(f);

        String title=new String(); 
        Integer year = 0; 
        Integer votes=0; 
        Person[] cast;
        Person director;        
        
        int c=0;
        String y=""; 
        String v=""; 
        Person MemberOfCast;
        String[] Array_cast;
        int old_i=0;
        boolean SameTitle=false;
        
        try
        {
            FileReader fr;
            fr = new FileReader(f);
            BufferedReader reader = new BufferedReader(fr);
            String line="";
            
            for (int i=Movies.size();line!=null; i++) 
            {
               c=0;
               SameTitle=false;
               line = reader.readLine();
               title=line.substring(campi[c].length());
               c++;
               
                for (int k = 0; k < Movies.size(); k++) 
                {
                    if (Movies.get(k)!=null && Movies.get(k).getTitle().trim().toLowerCase().compareTo(title.trim().toLowerCase())==0)
                    {
                        old_i=i;
                        i=k;
                        SameTitle=true;
                    }
                }
               
               line = reader.readLine();
               y=line.substring(campi[c].length()).trim().toLowerCase();
               year = Integer.parseInt(y);
               c++;
               
               line = reader.readLine();
               director=new Person(line.substring(campi[c].length()));
               c++;
               
               line = reader.readLine();
               Array_cast = D.Dividi(line.substring(campi[c].length()));
               cast = new Person[Array_cast.length];
                for (int j = 0; j < Array_cast.length; j++) 
                {
                    MemberOfCast=new Person(Array_cast[j]);
                    cast[j]= MemberOfCast;
                }
               c++;
               
               line = reader.readLine();
               v=line.substring(campi[c].length()).trim().toLowerCase();
               votes = Integer.parseInt(v);
               
               line = reader.readLine();
               
               Movie ToSave = new Movie(title,year,votes,cast,director);
               if (SameTitle==true) 
                {
                    Movies.set(i,ToSave);
                    i=old_i-1;  
                }
               else Movies.add(ToSave);
            }
            M=Movies.toArray(new Movie[Movies.size()]);
            
            if (CurrentMap==ABR) 
            {                
                BST.Clear();
                
                for (int g = 0; g < Movies.size(); g++) 
                {
                    BST.insert(Movies.get(g));
                }   
            }
            else
            {
                BT.Clear();
                BT = new Btree<>();
                
                for (int k = 0; k < Movies.size(); k++) 
                {
                    BT.insert(BT, Movies.get(k));
                }
            }
            System.out.println("DATI CARICATI ALL'INTERNO DI: "+ CurrentMap.toString() + "\n");
            System.out.println("CI SONO: "+ Movies.size()+ " film\n");
            G= new Grafo(M);
        } 
        
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(IMovidaCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(IMovidaCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    int[][] adj;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void saveToFile(File f) 
    { 
        if (f.exists()) 
        {
            f.delete();
            f=new File(f.getAbsolutePath());
        }
        else f=new File(f.getAbsolutePath());
        
        File Buffer= new File("Buffer.txt");
        
        try 
        {
            FileWriter FW = new FileWriter(Buffer);
            if (CurrentMap==ABR) 
            {
                Node rootPointer = BST.root;
                BST.Save(rootPointer, FW);
                FW.close();
            }
            else
            {
                Nodo rootPointer = BT.root;
                BT.Save(rootPointer, FW);
                FW.close();
            }
            
            FileReader Read= new FileReader(Buffer);
            BufferedReader R = new BufferedReader(Read);
            FileWriter W = new FileWriter(f);
            String line = R.readLine();
            while(line!=null)
            {
                if (line.isBlank()) 
                {
                    String line2 = R.readLine();
                    if (line2==null || line2.isBlank()) 
                    {
                        W.write(line);
                        break;
                    }
                    else W.write("\n"+line2);
                }
                W.write(line);
                W.write("\n");
                line=R.readLine();
            }
            R.close();
            Read.close();
            W.close();
            
            Buffer.delete();
        }
        
        catch (IOException ex) 
        {
            Logger.getLogger(IMovidaCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clear() 
    {
        Movies.clear();
        BST.Clear();
        BT.Clear();
        System.out.println("Tutti i dati eliminati, inserirne di nuovi");
    }

    @Override
    public int countMovies() 
    {
        int mv=0;
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root; 
            mv= BST.ContaNodi(rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            mv= BT.Contanodi(rootPointer);
        }
        return mv;
    }

    @Override
    public int countPeople() 
    {
        ArrayList people = new ArrayList();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;                     
            BST.ContaPersone(rootPointer,people);
        }
        else
        {
            Nodo rootPointer = BT.root;  
            BT.ContaPersone(rootPointer, people);
        }
        
          Object[] ppl=people.toArray();
          ppl=RD.rim_dup(ppl);
          people.clear();
        
        return ppl.length;
    }

    @Override
    public boolean deleteMovieByTitle(String title) 
    {
        for (int i = 0; i < M.length; i++) 
        {
            if (M[i].getTitle().trim().toLowerCase().equals(title.trim().toLowerCase()))
            {
                if (i!=M.length-1) 
                {
                    for (int j = i; j < M.length-1; j++) 
                    {
                        M[j]=M[j+1];
                    }
                }
            }   
        }
        M=Arrays.copyOfRange(M,0,M.length-1);
        G.CreaPSN(M);
        if (CurrentMap == ABR) 
        {
           Node rootPointer = BST.root; 
           return BST.deleteMovieByTitle(rootPointer, title);
        }
        else
        {  
           Nodo rootPointer = BT.root;
           return BT.deleteMovieTitle(rootPointer, title);
        }
    }

    @Override
    public Movie getMovieByTitle(String title) 
    {
        Movie m=null;
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            m=BST.GetbyTitle(title, rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            m=BT.GetMovieByTitle(title, rootPointer);
        }
        return m;
    }

    @Override
    public Person getPersonByName(String name) 
    {
        Person p;
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            p=BST.GetPRecord(name);
        }
        else
        {
            Nodo rootPointer = BT.root;
            p=BT.GetPRecord(name, rootPointer);
        }
        return p;
    }

    @Override
    public Movie[] getAllMovies() 
    {
        ArrayList<Movie> mm=new ArrayList<>();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.GetALLM(rootPointer, mm);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.GetALLM(rootPointer, mm);
        }
        Movie[] ALLM;
        ALLM=mm.toArray(new Movie[mm.size()]);
        return ALLM;
    }

    @Override
    public Person[] getAllPeople() 
    {
        ArrayList<String> people = new ArrayList<>();
        
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.ContaPersone(rootPointer,people);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.ContaPersone(rootPointer, people);
        }
        
        String[] pplnm=new String[people.size()];
        
        for (int i = 0; i < people.size(); i++) 
        {
            pplnm[i]=people.get(i);
        }
        
        pplnm=RD.rim_dup(pplnm);
        Person[] ppl=new Person[pplnm.length];
        
        for (int j = 0; j < pplnm.length; j++) 
        {
            ppl[j]=new Person(pplnm[j]);
        }
        return ppl;
    }
/////////////////////////////////////////////////////////////////////////////////
    @Override
    public Movie[] searchMoviesByTitle(String title) 
    {
        ArrayList<Movie> MatchingMovies = new ArrayList<>();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.GetArrayTitle(title, MatchingMovies, rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.GetArrayTitle(title, MatchingMovies, rootPointer);
        }
        Movie[] ALLM;
        ALLM=MatchingMovies.toArray(new Movie[MatchingMovies.size()]);
        return ALLM;
    }

    @Override
    public Movie[] searchMoviesInYear(Integer year) 
    {
        ArrayList<Movie> MatchingMovies = new ArrayList<>();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.GetArrayYear(year, MatchingMovies, rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.GetArrayYear(year, MatchingMovies, rootPointer);
        }
        Movie[] ALLM;
        ALLM=MatchingMovies.toArray(new Movie[MatchingMovies.size()]);
        return ALLM;
    }

    @Override
    public Movie[] searchMoviesDirectedBy(String name) 
    {
        ArrayList<Movie> MatchingMovies = new ArrayList<>();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.GetArrayDirector(name, MatchingMovies, rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.GetArrayDirector(name, MatchingMovies, rootPointer);
        }
        Movie[] ALLM;
        ALLM=MatchingMovies.toArray(new Movie[MatchingMovies.size()]);
        return ALLM;
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) 
    {
        ArrayList<Movie> MatchingMovies = new ArrayList<>();
        if (CurrentMap == ABR) 
        {
            Node rootPointer = BST.root;
            BST.GetArrayStarred(name, MatchingMovies, rootPointer);
        }
        else
        {
            Nodo rootPointer = BT.root;
            BT.GetArrayStarred(name, MatchingMovies, rootPointer);
        }
        Movie[] ALLM;
        ALLM=MatchingMovies.toArray(new Movie[MatchingMovies.size()]);
        return ALLM;
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer N) 
    {
        boolean choose = true;
        Movie[] MatchingMovies=M;
        if (CurrentSort == InsertionSort) 
        {
            IS.GetArrayMostVoted_Recent(MatchingMovies, choose);           
        }
        else
        {
            HS.GetArrayMostVoted_Recent(MatchingMovies, choose);
        }
        MatchingMovies=Arrays.copyOfRange(MatchingMovies, 0, N);
        return MatchingMovies;
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer N) 
    {
        boolean choose = false;
        Movie[] MatchingMovies=M;
        if (CurrentSort == InsertionSort) 
        {
            IS.GetArrayMostVoted_Recent(MatchingMovies, choose);           
        }
        else
        {
            HS.GetArrayMostVoted_Recent(MatchingMovies, choose);
        }
        MatchingMovies=Arrays.copyOfRange(MatchingMovies, 0, N);
        return MatchingMovies;
    }

    @Override
    public Person[] searchMostActiveActors(Integer N) 
    {
        Person[] PS = Grafo.PSN;
        Person[] P =new Person[N];
        int conta=0;
        ArrayList<Integer> pers=new ArrayList<>();
        Integer n = N;
       
        for (int k = 0; k < PS.length; k++) //Scorro le persone
        {        
            for (int i = 0; i < M.length; i++) //Scorro i film
            {
                for (int j = 0; j < M[i].getCast().length; j++) //Scorro i cast
                {
                    if (M[i].getCast()[j].getName().trim().toLowerCase().equals(PS[k].getName().trim().toLowerCase())) 
                    {
                        conta++;    //Quando trovo un film in cui un attore ha recitato aggiorno il conto
                    }
                }
            }
            pers.add(conta);    //Aggiungo alla lista in corrispondenza dell'indice dell'attore il conteggio di film
            conta=0;
        }
        
        
        if (N> pers.size())     //Se sono richiesti più attori di quanti ne siano presenti ridimensiono l'array
        {
            P=new Person[pers.size()];
            n=pers.size();
        }
        for (int p = 0; p < n; p++) 
        {
            int max=Collections.max(pers);  //Trovo il massimo di film girati
            int ind = pers.indexOf(max);    //e il relativo indice

                P[p]=PS[ind];               //Inserisco nell'array gli attori da quello che ha girato più film a quello che ne ha girati meno
                pers.set(ind,0);
        }
        return P;
    }
///////////////////////////////////////////////////////////////
@Override
    public Person[] getDirectCollaboratorsOf(Person actor) 
    {
        adj=G.CreaMat(M);   //Creo la matrice di adiacenza
        int[] dircol;
        dircol=G.CheckCollab(adj, G.getindex(Grafo.PSN, actor.getName().trim().toLowerCase())); //Creo un array di collaboratori diretti
        Person[] p= new Person[dircol.length];
        for (int i = 0; i < p.length; i++) 
        {
            p[i]=new Person(Grafo.GetactName(Grafo.PSN,dircol[i])); //Trasformo gli indici dei collaboratori in Person
        }
        return p;
    }

    @Override
    public Person[] getTeamOf(Person actor) 
    {
        adj=G.CreaMat(M);
        int[] indcol;
        indcol=G.CheckInd(adj, G.getindex(Grafo.PSN, actor.getName().trim().toLowerCase()));    //Creo un array di collaboratori indiretti
        Person[] p= new Person[indcol.length];
        for (int i = 0; i < p.length; i++) 
        {
            p[i]=new Person(Grafo.GetactName(Grafo.PSN,indcol[i])); //Trasformo gli indici dei collaboratori in Person
        }
        return p;
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) 
    {
        adj=G.CreaMat(M);
        int adj2[][]=adj;
        Collaboration[] collarray;
        int c[]=G.CheckInd(adj, G.getindex(G.PSN, actor.getName().trim().toLowerCase().trim().toLowerCase()));//Trovo i collaboratori indiretti actor
        
        int coll[]= new int[c.length+1];
        
        for (int k = 0; k < coll.length-1; k++) 
        {
          coll[k]=c[k];     ///Copio i collaboratori su un array
        }
        coll[coll.length-1]=G.getindex(Grafo.PSN,actor.getName());  //Aggiungo all'array actor
        
        int maxcol=0;
        int index=0;
        
        for (int i = 0; i < coll.length; i++) 
        {
            int a=G.CheckCollab(adj2, coll[i]).length;  //Tra tutti i collaboratori di actor cerco quello
                                                        //con più collaborazioni dirette
            if (a>=maxcol) 
            {
                maxcol=a;
                index=coll[i];
            }
        }
        
        int[] col= new int[G.CheckInd(adj, G.getindex(Grafo.PSN, Grafo.GetactName(Grafo.PSN, index))).length + 1];  //Calcolo le collaborazioni dell'attore
        for (int i = 0; i < col.length-1; i++)                                                                      //con più collaborazioni dirette
        {
             col[i]=G.CheckInd(adj, G.getindex(Grafo.PSN, Grafo.GetactName(Grafo.PSN, index)))[i];  //Copio le collaborazioni in un array
        }
        col[col.length-1]= G.getindex(Grafo.PSN, Grafo.GetactName(Grafo.PSN, index));   //Al termine dell'array inserisco l'attore stesso
      
        Double[][] matpunteggi;
        matpunteggi=G.MatScores(adj2,M);    //Creo la matrice dei punteggi
        col =RD.rim_dup(col);
        int q =Grafo.getind(col, index);
        int last =col[col.length-1];
        col[col.length-1]=col[q];
        col[q]=last;
        collarray=Grafo.MaxTree(matpunteggi,col); //Chiamo la funzione che crea l'albero minimale di massimo costo
        
        for (int i = 0; i < collarray.length; i++) 
        {
            Movie[] A=searchMoviesStarredBy(collarray[i].getActorA().getName().trim().toLowerCase());
            Movie[] B=searchMoviesStarredBy(collarray[i].getActorB().getName().trim().toLowerCase());
            collarray[i].movies.clear();
            ArrayList<Movie> common = new ArrayList<>();
            
            for (int t = 0; t < A.length; t++) 
            {
                for (int n = 0; n < B.length; n++) 
                {
                    if (A[t] == B[n])   //Copio i film in comune tra gli attori di tutte le Collaborations in una lista
                    {
                        common.add(A[t]);
                    }
                }
            }
            
            collarray[i].movies=common; //Assegno la lista a movies
        }
        return collarray;
    }
}
