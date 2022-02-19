
package lorettischinoppi.Map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import lorettischinoppi.controlli.NotFoundException;
import movida.commons.Movie;
import movida.commons.Person;

/**
 * @author Andrea
 * @param <T>
 */
public class ABR <T extends Movie>
{
    public class Node
    {  
        T data;  
        Node left;  
        Node right;  
  
        public Node(T data)
        {  
            //Assegna data al nuovo nodo e imposta il figlio destro e sinistro a null  
            this.data = data;  
            this.left = null;  
            this.right = null;  
        }  
   }  
  
      //Rappresenta la radice dell'albero  
      public Node root;  
  
      public ABR()
      {  
          root = null;  
      }  
  
      /**
       * Inserisce un nuovo elemento all'interno dell'albero
       * @param data è l'elemento da inserire 
       */
      public void insert(T data) 
      {
          // Per prima cosa viene generato un nuovo nodo
          Node newNode = new Node(data);  
  
          //Si controlla se la struttura è vuota, se la struttura è vuota il nodo generato viene impostato come radice
          if(root == null)
          {  
              root = newNode;  
          }  
          else 
          {  
              //Se la struttura dati presenta degli elementi il nodo generato viene puntato alla radice dell'albero
              Node current = root, parent = null;  
  
              while(true)
              {  
                  //parent rapresenta il nodo precedente del nodo corrente
                  parent = current;
                  //In seguito viene fatto un confronto tra le chiavi dei 2 nodi 
                  //Se la chiave del nodo è minore del nodo corrente verrà inserito a nella foglia sinistra
                  if(data.getTitle().trim().toLowerCase().compareTo(current.data.getTitle().trim().toLowerCase())<0)
                  {  
                      current = current.left;  
                      if(current == null) 
                      {  
                          parent.left = newNode;  
                          return;  
                      }  
                  }  
                  //Se la chiave del nodo è maggiore del nodo corrente verrà inserito a nella foglia destra
                  else 
                  {  
                      current = current.right;  
                      if(current == null) 
                      {  
                          parent.right = newNode;  
                          return;  
                      }  
                  }  
              }  
          }  
      }  
  
      /**
       * Trova il nodo con valore minore navigando nella parte sinistra più esterna dell'albero
       * @param root è il punto da cui iniziare la ricerca
       * @return il nodo contenente il valore minimo
       */ 
      public Node minNode(Node root) 
      {   //Se esiste una foglia sinistra viene richiamata la funzione su quel nodo in caso contrario è nodo minimo è stato trovato
          if (root.left != null)  
              return minNode(root.left);  
          else  
              return root;  
      }
      
      boolean delete;
      /**
       * Chiama la funzione che elimina dall'albero il film intitolato title
       * @param node è il nodo da cui cominciare la ricerca dei film
       * @param title è il titolo del film da rimuovere
       * @return true se il film è stato rimosso, false altrimenti
       */
      public boolean deleteMovieByTitle(Node node, String title)
      {
          deleteMovieByTitleWA(node, title);
          return delete;
      }
      /**
       * Elimina dall'albero il film intitolato title
       * @param node è il nodo da cui cominciare la ricerca dei film
       * @param value è il titolo del film da rimuovere
       * @return l'albero senza il film eliminato
       */
      public Node deleteMovieByTitleWA(Node node, String value) 
      {  
          if(node == null)
          {  
            delete = false; 
            return null;
          }  
          else 
          {  
              //Se il valore fornito è più piccolo del valore del nodo che stiamo confrontando navigherà nella foglia sinistra  
              if(value.trim().toLowerCase().compareTo(node.data.getTitle().trim().toLowerCase())<0)  
                  node.left = deleteMovieByTitleWA(node.left, value);  
  
              //Se il valore fornito è maggiore del valore del nodo che stiamo confrontando navigherà nella foglia destra   
              else if(value.trim().toLowerCase().compareTo(node.data.getTitle().trim().toLowerCase())>0)  
                  node.right = deleteMovieByTitleWA(node.right, value);  
  
              //Se si trova la corrispondenza il nodo viene eliminato
              else 
              {  
                  //Se il nodo non ha figli viene eliminato direttamente
                  if(node.left == null && node.right == null)  
                      node = null;  
  
                  //Se il nodo ha un solo figlio nella foglia destra il nodo viene sovrascritto dal figlio
                  else if(node.left == null) 
                  {  
                      node = node.right;  
                  }  
  
                  //Se il nodo ha un solo figlio nella foglia sinistra il nodo viene sovrascritto dal figlio 
                  else if(node.right == null) 
                  {  
                      node = node.left;  
                  }  
  
                  //Se il nodo ha due figli uno nella foglia destra e uno sulla foglia sinistra 
                  else 
                  {  
                      //Viene effettuata una ricerca del nodo col valore minimo nel sottoalbero destro 
                      Node temp = minNode(node.right);  
                      //Viene creato un nodo di appoggio temp  
                      node.data = temp.data;  
                      //E viene eliminato il nodo duplicato nel sotto albero destro  
                      node.right = deleteMovieByTitleWA(node.right, temp.data.getTitle().trim().toLowerCase()); 
                      delete = true; 
                  }  
              }
              return node;
          }  
      }  
  
      /**
       * Effettua una visita dell'albero
       * @param node è il nodo da cui cominciare la visita
       */  
      public void visit(Node node) 
      {
          //Viene controllato se l'albero è vuoto 
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");
          }  
          else 
          {   //Se esiste una foglia sinistra viene richiamata la funzione su quel nodo
              if(node.left!= null)  
                  visit(node.left);
              //vengono stampati tutti i dati riguardanti il nodo corrente
              System.out.println("\nFilm da ABR");
                    System.out.print(node.data.getTitle()+"\n" );
                    System.out.print(node.data.getYear()+"\n" );
                    System.out.print(node.data.getDirector().getName()+"\n" );
                    for (int j = 0; j < node.data.getCast().length; j++)
                    {
                        System.out.println(node.data.getCast()[j].getName());
                    }      
                    System.out.println(node.data.getVotes()+"\n");
              //Se esiste una foglia destra viene richiamata la funzione su quel nodo              
              if(node.right!= null)  
                  visit(node.right);
          }  
      }
      
      /**
       * Elimina tutti i dati dall'albero
       */
      public void Clear() 
      {
          root=null; //Il Garbage collector rimuoverà le altre dangling references
      }
      
     /**
      * Conta quanti nodi sono presenti nella struttura
      * @param N è il nodo da cui cominciare a contare
      * @return il numero di nodi dell'albero
      */
      public int ContaNodi(Node N)
      {   //viene inizializzato un contatore
          int conta=0; 
          //se il nodo corrente non è null viene aumentato il contatore
          if (N!=null) 
          {
              conta++;
            //se il nodo nella foglia sinistra non è null viene richiamata la funzione su di esso aumentando il contatore
            if (N.left != null) 
            {
                conta = conta + ContaNodi(N.left);
            }
            //se il nodo nella foglia destra non è null viene richiamata la funzione su di esso aumentando il contatore
            if (N.right != null) 
            {
                conta = conta + ContaNodi(N.right);
            }
          }
          return conta;
    }
      
      /**
       * Sovrascrive un file con i dati dei film all'interno di ogni nodo
       * @param node è il nodo da cui cominciare a salvare i film
       * @param FW è un FileWriter già inizializzato dal chiamante
       * @throws IOException in caso di errori in scrittura
       */
      public void Save(Node node, FileWriter FW) throws IOException 
      {   //Viene controllato se l'albero è vuoto 
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {   //nel caso il nodo nella foglia sinistra non sia null viene richiamata la funzione su di esso
              if(node.left!= null) 
                  Save(node.left, FW);
                    //Qui avviene la scrittura dei dati sul file 
                    FW.write("Title: "+node.data.getTitle()+"\n" );
                    FW.write("Year: " +node.data.getYear()+"\n" );
                    FW.write("Director: "+node.data.getDirector().getName()+"\n" );
                    FW.write("Cast: ");
                    //in questo ciclo avviene la separazione dei nomi del cast
                    for (int j = 0; j < node.data.getCast().length; j++)
                    {
                        FW.write(node.data.getCast()[j].getName());
                        if (j!= node.data.getCast().length-1) 
                        {
                            FW.write(", ");
                        }
                    }      
                    FW.write("\n");
                    FW.write("Votes: "+node.data.getVotes()+"\n");
                    FW.write("\n");
              //nel caso il nodo nella foglia destra non sia null viene richiamata la funzione su di esso      
              if(node.right!= null)  
                  Save(node.right,FW);
          }  
      }
      
      /**
       * Restuiruisce il numero totale di persone
       * @param node è il nodo da cui cominciare a contare le persone
       * @param people è un ArrayList inizializzata dal chiamante
       */
      public void ContaPersone(Node node, ArrayList people) 
      {   //Viene controllato se l'albero è vuoto 
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {   //nel caso il nodo nella foglia sinistra non sia null viene richiamata la funzione su di esso     
              if(node.left!= null)  
                  ContaPersone(node.left,people);
                    //Viene verificato se il campo del direttore non è vuoto in seguito si ottiene il campo
                    if ((node.data.getDirector().getName().trim().toLowerCase())!= null) 
                    {
                        people.add(node.data.getDirector().getName().trim().toLowerCase());
                    }
                    //viene effettuato un ciclo sul cast in modo da ottenere tutti i membri             
                    for (int j=0; j < node.data.getCast().length; j++)
                    {
                        people.add(node.data.getCast()[j].getName().trim().toLowerCase());
                    }
              //nel caso il nodo nella foglia destra non sia null viene richiamata la funzione su di esso           
              if(node.right!= null)  
                  ContaPersone(node.right,people);
          } 

      }
      
      /**
       * Restituituisce il record di un film dato il titolo
       * @param title è il titolo del film da cercare
       * @param Nodo è il nodo da cui iniziare la ricerca del film
       * @return il record del film cercato, se presente
       * @throws NotFoundException se il film non è presente
       */
      public Movie GetbyTitle (String title, Node Nodo) 
      {
          Movie found;
          //Viene controllato se l'albero è vuoto
          if(root == null)
          {  
              throw new NotFoundException();  
          }  
          else 
          {   //nel caso il nodo nella foglia sinistra non sia null e il valore nel nodo è maggiore del valore fornite viene richiamata la funzione su di esso
              if(Nodo.left!= null && Nodo.data.getTitle().trim().toLowerCase().compareTo(title.trim().toLowerCase())>0)
              {
                  //Nodo=Nodo.left;
                  return found=GetbyTitle(title, Nodo.left);
              }
              //Se trovo il film che sto cercando stampo i campi    
              if (Nodo.data.getTitle().trim().toLowerCase().equals(title.trim().toLowerCase())) 
              {
                  /*
                  System.out.print("Title: "+Nodo.data.getTitle()+"\n" );
                    System.out.print("Year: "+Nodo.data.getYear()+"\n" );
                    System.out.print("Director: "+Nodo.data.getDirector().getName()+"\n" );
                    System.out.print("Cast: ");
                    for (int j = 0; j < Nodo.data.getCast().length; j++)
                    {
                        System.out.print(Nodo.data.getCast()[j].getName());
                        
                        if (j!= Nodo.data.getCast().length-1) 
                        {
                            System.out.print(", ");
                        }
                    }
                    System.out.println("\nVotes: "+Nodo.data.getVotes()+"\n");
                    System.out.println("\n");
                    */
                    if(Nodo.right!= null)   //Se trovo il film mi sposto
                    {
                        Nodo=Nodo.right;
                    }
                    else if(Nodo.left!= null)
                    {
                        Nodo=Nodo.left;
                    }
                    return Nodo.data;
              }
              //nel caso il nodo nella foglia destra non sia null e il valore nel nodo è minore del valore fornite viene richiamata la funzione su di esso              
              if(Nodo.right!= null && Nodo.data.getTitle().trim().toLowerCase().compareTo(title.trim().toLowerCase())<0)
              {
                  //Nodo=Nodo.right;
                  return found=GetbyTitle(title, Nodo.right);
              }     
          }  
          throw new NotFoundException();
      }
      
      
        Person p=null;
        public Person GetPRecord(String name)
        {
            GetPNodo(name, root);
            if (p==null) 
            {
                throw new NotFoundException();
            }
            return p;
        }
      
      
      public void GetPNodo (String name, Node root) 
      {
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {
              if(root.left!= null)
              {
                  GetPNodo(name, root.left);
              }
              if ((name.trim().toLowerCase()).equals(root.data.getDirector().getName().trim().toLowerCase()));
              {
                  p= root.data.getDirector();
              }
              
              for (int j = 0; j < root.data.getCast().length; j++) 
              {
                  if (root.data.getCast()[j].getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
                  {
                      p= root.data.getCast()[j];
                  } 
              }
                            
              if(root.right!= null)
              {
                  GetPNodo(name, root.right);
              }
          }
      }
      
      /**
       * Modifica l'ArrayList in input inserendo tutti i record dei film
       * @param node è il nodo da cui iniziare ad inserire i film
       * @param mm è la lista di film inizializzata dal chiamante
       */
      public void GetALLM(Node node, ArrayList mm) 
      {
          if(root == null) //Se la radice è null segnalo che l'albero è vuoto
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {
              if(node.left!= null)  //Se il nodo sinistro è diverso da null lo controllo
                  GetALLM(node.left,mm);

              mm.add(node.data);    //Aggiungo i film alla lista
              
              if(node.right!= null)  //Se il nodo destro è diverso da null lo controllo
                  GetALLM(node.right,mm);
          }
      }
      
      /**
       * Modifica la lista in input aggiungendo tutti i film il cui titolo contiene title
       * @param title è la stringa che deve essere confrontata con i vari titoli
       * @param Movies è una lista inizializzata dal chiamante
       * @param node è il nodo da cui iniziare la ricerca
       */
      public void GetArrayTitle(String title, ArrayList Movies, Node node) 
      {
          if(root == null) //Se la radice è null segnalo che l'albero è vuoto
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {
              if (node.data.getTitle().trim().toLowerCase().contains(title.trim().toLowerCase())) //Se il film considerato contiene title
              {                                                       //lo aggiungo alla lista
                  Movies.add(node.data);                              //
              }
              
              if(node.left!= null)      //Se il nodo sinistro è diverso da null lo controllo
              {
                  GetArrayTitle(title, Movies, node.left);
              }
                  
              if(node.right!= null)     //Se il nodo destro è diverso da null lo controllo
              {
                  GetArrayTitle(title, Movies, node.right);
              }
                  
          }  
      }      
      
      /**
       * Modifica la lista in input aggiungendo tutti i film creati nell'anno year
       * @param year è l'anno da cosiderare
       * @param Movies è una lista inizializzata dal chiamante
       * @param node è il nodo da cui iniziare la ricerca
       */
      public void GetArrayYear(Integer year, ArrayList Movies, Node node) 
      {
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");  //Se la radice è null segnalo che l'albero è vuoto
          }  
          else 
          {
              if (node.data.getYear().equals(year)) //Se l'anno del film cosiderato
              {                                     //è uguale all'anno cercato
                  Movies.add(node.data);            //aggiungo il film alla lista
              }
              
              if(node.left!= null)                  //Se il nodo sinistro è diverso da null lo controllo
              {
                  GetArrayYear(year, Movies, node.left);
              }
                  
              if(node.right!= null)                 //Se il nodo destro è diverso da null lo controllo
              {
                  GetArrayYear(year, Movies, node.right);
              }
                  
          }  
      }  
      
      /**
       * Modifica la lista in input aggiungendo tutti i film diretti dal regista con nome name
       * @param name è il nome del direttore
       * @param Movies è una lista inizializzata dal chiamante
       * @param node è il nodo da cui iniziare la ricerca
       */
      public void GetArrayDirector(String name, ArrayList Movies, Node node)
      {
          if(root == null)      //Se la radice è null segnalo che l'albero è vuoto
          {  
              System.out.println("Albero vuoto\n");  
          }  
          else 
          {
              if (node.data.getDirector().getName().trim().toLowerCase().equals(name.trim().toLowerCase())) //Se trovo un film diretto dal regista
              {                                                                 //considerato lo aggiungo alla lista
                  Movies.add(node.data);                                        //
              }
              
              if(node.left!= null)       //Se il nodo sinistro è diverso da null lo controllo
              {
                  GetArrayDirector(name, Movies, node.left);
              }
                  
              if(node.right!= null)      //Se il nodo destro è diverso da null lo controllo
              {
                  GetArrayDirector(name, Movies, node.right);
              }
                  
          }  
      }
      
      /**
       * Modifica la lista in input aggiungendo tutti i film con l'attore di nome name
       * @param name è il nome dell'attore considerato
       * @param Movies è una lista inizializzata dal chiamante
       * @param node è il nodo da cui iniziare la ricerca 
       */
      public void GetArrayStarred(String name, ArrayList Movies, Node node)
      {
          if(root == null)
          {  
              System.out.println("Albero vuoto\n");  //Se la radice è null segnalo che l'albero è vuoto
          }            
              for (int j = 0; j < node.data.getCast().length; j++)
              {     
                if (node.data.getCast()[j].getName().trim().toLowerCase().equals(name.trim().toLowerCase())) //Se trovo un film con l'attore considerato
                {                                                                //lo aggiungo alla lista
                    Movies.add(node.data);                                       //
                }
              }
              
                if(node.left!= null)       //Se il nodo sinistro è diverso da null lo controllo
                {
                    GetArrayStarred(name, Movies, node.left);
                }
              
                if(node.right!= null)      //Se il nodo destro è diverso da null lo controllo
                {
                    GetArrayStarred(name, Movies, node.right);
                }
      }  
}
