package lorettischinoppi;

import java.io.*;
import movida.commons.*;
import static movida.commons.MapImplementation.*;
import static movida.commons.SortingAlgorithm.*;

/**
 *
 * @author Andrea
 */
class Main 
{
    @SuppressWarnings("unchecked")
    
    public static void main(String[] args) 
    {

        File load = new File("src\\movida\\commons\\esempio-formato-dati.txt");
        File save = new File("src\\lorettischinoppi\\Save.txt");
        
        IMovidaCore core = new IMovidaCore();
       
        core.setSort(InsertionSort);
        //core.setSort(HeapSort);
        
        core.setMap(ABR);
        //core.setMap(BTree);
        
        System.out.println("*** TEST***\n");
        core.loadFromFile(load);
        core.saveToFile(save);
        System.out.println("Film Salvati\n");
        
        if (core.CurrentMap==ABR) //Test per ABR
        {
            core.BST.visit(core.BST.root);
        }
        else                      //Test per Btree
        {
            core.BT.stampa(core.BT.root);
        }
        System.out.println("\n\n");
        
        System.out.println("CountMovies: "+core.countMovies());
        System.out.println("\n");
        
        String tit = "Scarface";
        System.out.println("TEST deleteMovieByTitle: "+ tit);
        core.deleteMovieByTitle(tit);
        System.out.println("CountMovies dopo l'eliminazione: "+core.countMovies());
        System.out.println("\n");
        
        System.out.println("CountPeople: "+core.countPeople());
        System.out.println("\n");
        
        tit = "Die Hard";
        System.out.println("TEST GetMoviebyTitle: "+tit);
        Movie m = core.getMovieByTitle(tit);
        
        System.out.println("Titolo: "+m.getTitle());
        System.out.println("Anno: "+m.getYear());
        System.out.println("Regista: "+m.getDirector().getName());
        System.out.println("Cast: ");
        for (int i = 0; i < m.getCast().length; i++) 
        {
            System.out.println(m.getCast()[i].getName());
        }
        System.out.println("Voti: "+m.getVotes());
        System.out.println("\n\n");
        
        String nome = "Harrison Ford";
        
        System.out.println("TEST getPersonByName: "+nome);
        System.out.println("Valore restituito: " + core.getPersonByName(nome).getName());
        System.out.println("\n\n");
        
        System.out.println("TEST getAllMovies:\n");
        for (int i = 0; i < core.getAllMovies().length; i++) 
        {
            System.out.println(core.getAllMovies()[i].getTitle());
        }
        System.out.println("\n\n");
        
        
        System.out.println("TEST getAllPeople:\n");
        for (int i = 0; i < core.getAllPeople().length; i++) 
        {
            System.out.println(core.getAllPeople()[i].getName());
        }
        System.out.println("\n\n");

        String title = "Air Force One";
        Movie[] mt = core.searchMoviesByTitle(title);
        
        System.out.println("TEST searchMoviesByTitle: " + title);
        
        for (int i = 0; i < mt.length; i++) 
        {
          System.out.println("Risultato: " + mt[i].getTitle());  
        }
        System.out.println("\n\n");
        
        Integer year = 1976;
        Movie[] my = core.searchMoviesInYear(year);
        
        System.out.println("TEST searchMoviesInYear: " + year);
        
        for (int i = 0; i < my.length; i++) 
        {
          System.out.println("Risultato: " + my[i].getTitle());  
        }
        System.out.println("\n\n");
        
        
        System.out.println("TEST searchMoviesStarredBy: " + nome);
        
        for (int i = 0; i < core.searchMoviesStarredBy(nome).length; i++) 
        {
            System.out.println("Film: " +core.searchMoviesStarredBy(nome)[i].getTitle());
        }
        System.out.println("\n\n");
        
        String reg="Martin Scorsese";
        
        System.out.println("TEST searchMoviesDirectedBy: " + reg);
        Movie[] mr=core.searchMoviesDirectedBy(reg);
        for (int i = 0; i < mr.length; i++) 
        {
            System.out.println("Film: " +mr[i].getTitle());
        }        
        System.out.println("\n\n");
        
        System.out.println("TEST searchMostVotedMovies: " + 5);
        my = core.searchMostVotedMovies(5);
        for (int i = 0; i < my.length; i++) 
        {
            System.out.println("Film: " +my[i].getTitle());
        }
        System.out.println("\n\n");
        
        System.out.println("TEST searchMostRecentMovies: " + 5);
        my = core.searchMostRecentMovies(5);
        for (int i = 0; i < my.length; i++) 
        {
            System.out.println("Film: " +my[i].getTitle());
        }
        System.out.println("\n\n");
        
        System.out.println("TEST searchMostActiveActors: " + 5);
        Person[] p = core.searchMostActiveActors(5);
        for (int i = 0; i < p.length; i++) 
        {
            System.out.println("Film: " +p[i].getName());
        }
        System.out.println("\n\n");
        System.out.println("\n\n");
        
        System.out.println("INIZIO TEST GRAFO (Scarface è stato eliminato)");
        
        int [][] mat = core.G.CreaMat(core.M);
        System.out.println("Stampa dell'elenco di tutti gli attori:\n");
        for (int i = 0; i <core.G.PSN.length ; i++) 
        {
            System.out.println(i+"  "+core.G.PSN[i].getName());
        }
        System.out.print("\n\n");
        
        System.out.println("TEST collaboratori diretti " +nome);
        Person[] dir=core.getDirectCollaboratorsOf(new Person(nome.trim().toLowerCase()));
        for (int i = 0; i < dir.length; i++) 
        {
            System.out.print(dir[i].getName()+"\n");
        }
        System.out.print("\n\n");
       
        System.out.println("TEST collaboratori indiretti "+nome);
        Person[] indir=core.getTeamOf(new Person(nome.trim().toLowerCase()));
        for (int i = 0; i < indir.length; i++) 
        {
            System.out.print(indir[i].getName()+"\n");
        }
       System.out.print("\n\n");
      
       System.out.println("TEST massimizza valore collaborazioni "+nome);
       Collaboration[] coll=core.maximizeCollaborationsInTheTeamOf(new Person(nome.trim().toLowerCase()));
       
        for (int i = 0; i < coll.length; i++) 
        {
            System.out.println("\nAttori: "+coll[i].getActorA().getName() + "  "+coll[i].getActorB().getName());
            for (int j = 0; j < coll[i].movies.size(); j++) 
            {
                System.out.println("Film: "+coll[i].movies.get(j).getTitle());
            }  
        }
       System.out.print("\n\n");
       System.out.print("Test completato!\n");
       
    }  
}