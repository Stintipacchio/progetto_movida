/*
 *  Header
*/
package lorettischinoppi.Map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import lorettischinoppi.controlli.NotFoundException;
import movida.commons.Movie;
import movida.commons.Person;

/**
 *
 * @author Andrea
 * @param <T>
 * 
 */

public class Btree<T extends Movie>
{
        public Nodo root=new Nodo();
        int t=root.t;  //Grado del Btree
        Nodo genitore_null=null;
        boolean splitted=false;
        
        public Btree()
        {
          root = new Nodo(genitore_null);
        }

        /**
         * Ricerca il film nella struttura dati in base al nome fornito
         * @param root è il nodo di partenza per la ricerca
         * @param title è il titolo del film che si cerca
         * @return il nodo contenente il film
         */
        public Nodo search(Nodo root, String title)
        {
                Integer i = 0;

                while(i.compareTo(root.N_keys)<0 && title.compareTo(root.keys[i].getTitle())>0) //Cerco il figlio corretto
                {
                  i = i+1;
                }

                if(i < root.N_keys && title.trim().toLowerCase().compareTo(root.keys[i].getTitle().trim().toLowerCase())==0) 
                {
                    return root;    //Se trovo l'elemento termino
                }

                if(root.isLeaf) //Se la radice non ha figli 
                {
                 throw new NotFoundException(); //e non ho trovato nulla in precedenza
                }
                else
                {
                  return search(root.getChild(i),title);    //Altrimenti continuo la ricerca
                }
        }

        /**
         * La funzione di split permette di gestire i nodi che superano 2*t - 1 chiavi, creando 2 nodi
         * @param prec è il nodo precedente a quello considerato
         * @param i è l'indice del figlio su cui applico lo split
         * @param node è il nodo considerato
         */
        public void split(Nodo prec, int i, Nodo node)  
        {
                Nodo N = new Nodo(genitore_null); //Creo un nuovo nodo
                N.isLeaf = node.isLeaf;
                N.N_keys = t - 1;
                for (int j = 0; j < t - 1; j++)  // Copio gli ultimi elementi nel nuovo nodo
                {
                    N.keys[j] = node.keys[j + t];
                }
                if (!N.isLeaf) 
                {
                    for (int j = 0; j < t; j++) { //Copio gli ultimi t elementi del figlio del nodo considerato in N.Figlio
                            N.Figlio[j] = node.Figlio[j + t];
                    }
                    for (int j = t; j <= node.N_keys; j++) 
                    {
                        node.Figlio[j] = null;  //Pongo i figli a null
                    }
                }
                for (int j = t; j < node.N_keys; j++) 
                {
                        node.keys[j] = null;    //Pongo le ultime chiavi a null
                }
                node.N_keys = t - 1;    //Aggiorno il numero di chiavi
                
                // Insert a (child) pointer to node newNodo into the parentNodo, moving other keys and pointers as necessary.
                for (int j = prec.N_keys; j >= i + 1; j--) 
                {
                    prec.Figlio[j + 1] = prec.Figlio[j]; //Elimino le chiavi dalla posizione i+1
                }
                prec.Figlio[i + 1] = N; 
                
                for (int j = prec.N_keys - 1; j >= i; j--) 
                {
                    prec.keys[j + 1] = prec.keys[j];    //Elimino le chiavi fino alla posizione i
                }               
                prec.keys[i] = node.keys[t - 1]; //Inserisco in posizione i la chiave t-1 del nodo considerato
                node.keys[t - 1] = null; //Poi la elimino
                
                if (splitted==false); 
                {
                    prec.N_keys++;
                }                
        }
        /**
         * Inserimento in un nodo senza split
         * @param x è il nodo in cui inserire
         * @param key è l'elemento da inserire
         */
        public void ins(Nodo x, T key)
        {
            int i = x.N_keys;
            
            if(x.isLeaf)    //Se sto inserendo in una foglia
            {
                while(i>=1 && key.getTitle().compareTo(x.keys[i-1].getTitle())<0)
                {
                    x.keys[i] = x.keys[i-1];    //Se trovo un valore che andrebbe a sinistra
                    i=i-1;                      //rispetto alla posizione attuale faccio un left shift
                }

                x.keys[i] = key;                //poi inserisco il valore
                x.N_keys=x.N_keys+1;
            }

                else    //Se NON sto inserendo in una foglia
                {
                    int j = 0;
                    while(j<x.N_keys && key.getTitle().compareTo(x.keys[i-1].getTitle())>0)
                    {
                        j=j+1;          //Individuo il nodo in cui andrebbe inserito il prossimo elemento
                    }                   //(se è minore dei precedenti si inserisce a sinistra)

                    if(x.Figlio[j].N_keys == 2*t - 1)      //Se anche la posizione in cui va inserito
                    {                                      //fosse piena → split
                            split(x,j,x.Figlio[j]);        //

                            if(key.getTitle().compareTo(x.keys[j].getTitle())>0)
                            {                                                    
                                j=j+1;
                            }
                    }
                    ins(x.Figlio[j],key); //Inserisco la chiave al posto giusto
                }
        }    

        /**
         * Inserimento nel btree
         * @param t il btree in cui effettuare l'inserimento
         * @param key l'elemento da inserire
         */
        public void insert(Btree t, T key)
        {
            Nodo r = t.root;
            
            if(r.N_keys == 2*this.t - 1)
            {
                Nodo s = new Nodo(genitore_null);   //Creo un nuovo nodo che diventerà
                t.root = s;                         //la nuova radice del Btree
                s.isLeaf = false;
                s.N_keys = 0;                       //per il momento non ha chiavi
                s.Figlio[0] = r;                    //e il suo primo figlio è proprio la vecchia radice

                split(s,0,r);

                ins(s, key);
            }
            else
            ins(r,key);
        }

        /**
         * Stampa gli elementi nel btree
         * @param n è il nodo da cui iniziare la visita
         */
        public void stampa(Nodo n)
        {
                for(int i = 0; i < n.N_keys; i=i+1)
                {
                    System.out.println("\nFilm da Btree");
                    System.out.print(n.getValue(i).getTitle()+"\n" );
                    System.out.print(n.getValue(i).getYear()+"\n" );
                    System.out.print(n.getValue(i).getDirector().getName()+"\n" );
                    for (int j = 0; j < n.getValue(i).getCast().length; j++)
                    {
                        System.out.println(n.getValue(i).getCast()[j].getName());
                    }      
                    System.out.print(n.getValue(i).getVotes()+"\n" );
                }
                int q=0;
                for (q = 0; q < n.MAX_FIGLI; q++) 
                {
                    if (n.getChild(q)==null) 
                    {
                        break;
                    }
                }
                
                if(!n.isLeaf)
                {
                    for(int l = 0; l <= q; l=l+1)
                    {				  
                        if(n.getChild(l) != null) 
                        {
                            stampa(n.getChild(l));     
                        }
                    }
                }
        }
        
        /**
         * Elimina un film dall'albero dato il titolo
         * @param node è il nodo da cui iniziare la ricerca del film da eliminare
         * @param key è il titolo del film da eliminare
         * @return true se il film è stato eliminato, false altrimenti
         */
              public boolean deleteMovieTitle(Nodo node, String key)
              {
                    Nodo current = new Nodo(genitore_null);

                    current = search(node, key);

                    //index viene ridefinito ogni qual volta l'elemento da eliminare viene trovato
                    int index = -1;             
                    //viene controllato ogni elemento nel nodo corrente per definire index
                    for (int i = 0; i < current.N_keys-1; i++) 
                    {
                        if (current.getValue(i).getTitle().trim().toLowerCase().equals(key.trim().toLowerCase())) 
                        {
                            index=i;
                        }
                    } 
               if (index!=-1)
               {
                   //se l'elemento è stato trovato e il nodo corrente è una foglia con più di t-1 elementi 
                    if(current.isLeaf && current.N_keys >= this.t - 1)
                    {
                        Integer i = 0;
                        //finchè la chiave maggiore dell'elemento nel nodo corrente i viene incrementata 
                        while(key.trim().toLowerCase().compareTo(current.getValue(i).getTitle().trim().toLowerCase())>0)
                        {
                            i=i+1;
                        }
                        //vengono eliminate le chiavi dai 'i' in poi (left shift)
                        for(int j = i; j < 2*this.t - 2; j=j+1)
                        {
                            current.keys[j] = current.getValue(j+1);
                        }
                        current.N_keys --;
                        return true;
                    }
                    //se il nodo preso in considerazione non e una foglia
                    if (!current.isLeaf) 
                    {
                        //viene definito un puntatore al genitore del nodo
                        Nodo parent;
                        parent = current.Figlio[index];

                        Movie predMov;
                        //se il numero di elementi nel genitore supera o è uguale a t viene assegnato a predMov il valore della chiave precedente 
                        if (parent.N_keys >= this.t)
                        {
                            while(true) 
                            {
                                if (parent.isLeaf)
                                {
                                  predMov = parent.getValue(parent.N_keys -1);
                                  break;
                                } 
                                else //altrimenti vene assegnato al genitore il valore del figlio
                                {
                                  parent = parent.Figlio[parent.N_keys];
                                }
                            }
                            //viene richiamata la funzione sul nodo precedente 
                            deleteMovieTitle(parent, predMov.getTitle());
                            current.keys[index] = predMov;
                            return true;
                        }
                        //se il numero di chiavi nel nodo è inferiore a t viene assegnato un puntatore al nodo successivo
                        Nodo Child = current.Figlio[index + 1];
                        //viene ripetuta l'operazione sopra citata sul nodo successivo
                        if (Child.N_keys >= this.t)
                        {
                          Movie nextKey = Child.getValue(0);

                          if (!Child.isLeaf)
                          {
                            Child = Child.Figlio[0];
                            while(true)
                            {
                              if (Child.isLeaf)
                              {
                                nextKey = Child.keys[Child.N_keys - 1];
                                break;
                              }
                              else
                              {
                                Child = Child.Figlio[Child.N_keys];
                              }
                            }
                          }
                          deleteMovieTitle(Child, nextKey.getTitle());
                          current.keys[index] = nextKey;
                          return true;
                        }

                        int count = parent.N_keys + 1;
                        parent.keys[parent.N_keys++] = current.keys[index];//l'ultima chiave di parent viene posta come la chiave di current in posizione index

                        for (int i = 0, j = parent.N_keys; i < Child.N_keys; i++)//inserisce le chiavi del figlio nel padre
                        {
                          parent.keys[j++] = Child.keys[i];
                          parent.N_keys++;
                        }

                        for (int i = 0; i < Child.N_keys + 1; i++)//sposta le chiavi verso l'alto
                        {
                          parent.Figlio[count++] = Child.Figlio[i];
                        }
                        current.Figlio[index] = parent;

                        for (int i = index; i < current.N_keys; i++)//elimino le chiavi da current
                        {
                          if (i != 2 * this.t - 2)
                          {
                            current.keys[i] = current.keys[i + 1];
                          }
                        }

                        for (int i = index + 1; i < current.N_keys + 1; i++)//elimino le chiavi dal figlio
                        {
                          if (i != 2 * this.t - 1)
                          {
                            current.Figlio[i] = current.Figlio[i + 1];
                          }
                        }
                        current.N_keys--;

                        if (current.N_keys == 0)//se non ci sono più chiavi e il nodo corrente è la radice imposto come radice il primo figlio di current
                        {
                          if (current == root)
                          {
                            root = current.Figlio[0];
                          }
                          current = current.Figlio[0];
                        }
                        //richiamo la funzione sul genitore
                        deleteMovieTitle(parent, key);
                        return true;
                    }
               }
               //se l'elemento da eliminare non viene trovato si confrontano le chiavi del nodo per navigare nel figlio giusto
                    else 
                    {
                        for (index = 0; index < current.N_keys; index++) 
                        {
                            if (current.keys[index].getTitle().compareTo(key)>0)
                            {
                              break;
                            }
                        }

                        Nodo tmp = current.Figlio[index];//creo un nodo d'appoggio
                        if (tmp.N_keys >= this.t)//se il numero di chiavi supera o eguaglia t richiamo la funzione sul quel nodo
                        {
                          deleteMovieTitle(tmp, key);
                          return true;
                        }


                          Nodo BK = null;
                          Movie div;

                          if (index != current.N_keys && current.Figlio[index + 1].N_keys >= this.t)
                          {
                            div = current.keys[index];//salvo il valore di index
                            BK = current.Figlio[index + 1];//salvo il figlio destro di index
                            current.keys[index] = BK.keys[0];//imposto la chiave index del nodo corrente come la prima chiave di BK
                            tmp.keys[tmp.N_keys++] = div;//pongo div come ultima chiave
                            tmp.Figlio[tmp.N_keys] = BK.Figlio[0];//pongo l'ultima chiave come la prima di BK.Figlio

                            for (int i = 1; i < BK.N_keys; i++)//elimino gli elementi delle chiavi di BK a sinistra e i figli a sinistra
                            {
                              BK.keys[i - 1] = BK.keys[i];
                            }
                            for (int i = 1; i <= BK.N_keys; i++)
                            {
                              BK.Figlio[i - 1] = BK.Figlio[i];
                            }

                            BK.N_keys--;//diminuidco il numero di chiavi e richiamo la funzione
                            deleteMovieTitle(tmp, key);
                            return true;
                          }

                          else if (index != 0 && current.Figlio[index - 1].N_keys >= this.t)//se il numero delle chiavi del figlio a sinistra rispetto a index del nodo corrente è maggiore uguale a t e index è diverso da 0
                          {
                            div = current.keys[index - 1];//imposto div come la chiave sinistra rispetto a index
                            BK = current.Figlio[index - 1];//imposto BK come il figlio sinistro rispetto a index
                            current.keys[index - 1] = BK.keys[BK.N_keys - 1];//imposto la chiave in posizione index -1 come la chiave di BKin posizione BK.N_keys - 1

                            Nodo next = BK.Figlio[BK.N_keys];//imposta next come l'ultimo figlio di BK
                            BK.N_keys--;//diminuisco il numerodi chiavi

                          for (int i = tmp.N_keys; i > 0; i--)//elimino gli elementi delle chiavi di tmp a destra e i figli a destra
                          {
                            tmp.keys[i] = tmp.keys[i - 1];
                          }
                          tmp.keys[0] = div;

                          for (int i = tmp.N_keys + 1; i > 0; i--)
                          {
                            tmp.Figlio[i] = tmp.Figlio[i - 1];
                          }
                          tmp.Figlio[0] = next;//imposto il primo figlio di tmp come next
                          tmp.N_keys++;//incremento il numero di chiavi
                          deleteMovieTitle(tmp, key);//richiamo su tmp
                          return true;
                        } 
                          else
                          {
                            Nodo sx = null;
                            Nodo dx = null;
                            boolean last = false;
                            if (index != current.N_keys)
                            {
                              div = current.keys[index];//div viene impostato come chiave di index
                              sx = current.Figlio[index];//sx viene impostato come figlio in posizione index di current
                              dx = current.Figlio[index+ 1];//dx viene impostato come figlio in posizione index+1 di current
                            } 
                            else
                            {
                                div = current.keys[index - 1];//div viene impostato come chiave di index-1
                                dx = current.Figlio[index];//sx e dx vengono impostati come figlii in posizione index di current
                                sx = current.Figlio[index];
                                last = true;//sono arrivato all'ultimo nodo
                                index--;//decremento l'indice
                            }

                            for (int i = index; i < current.N_keys - 1; i++)//elimino gli elementi delle chiavi di current a sinistra e i figli a sinistra
                            {
                                current.keys[i] = current.keys[i + 1];
                            }

                            for (int i = this.t + 1; i < current.N_keys; i++)
                            {
                                current.Figlio[i] = current.Figlio[i + 1];
                            }
                            current.N_keys--;//decremento il numero di chiavi
                            sx.keys[sx.N_keys++] = div;//imposto la chiave di sx in posizione N_keys come div

                            for (int i = 0, j = sx.N_keys; i < dx.N_keys + 1; i++, j++) //finchè i è minore del numero di chiavi di dx faccio uno spostamento delle chiavi da dx verso sx, stesso procedimento per quanto riguarda i figli
                            {
                              if (i < dx.N_keys)           
                              {
                                sx.keys[j] = dx.keys[i];
                              }
                              sx.Figlio[j] = dx.Figlio[i];
                            }
                            sx.N_keys += dx.N_keys;

                            if (current.N_keys == 0)//se non ci sono più chiavi e il nodo corrente è la radice imposto come radice il primo figlio di current
                            {
                              if (current == root)
                              {
                                root = current.Figlio[0];
                              }
                              current = current.Figlio[0];
                            }
                            deleteMovieTitle(sx, key);//richiamo la funzione su sx
                            return true;
                          }
                    }
               return false;
           }


       /**
        * Elimina tutti gli elementi dal btree
        */
       public void Clear() 
        {
            root=null; //Il Garbage collector rimuoverà le altre dangling references
        }
       
       /**
        * Conta quanti nodi sono presenti nell'albero
        * @param n è il nodo da cui inziare a contare
        * @return numero dei nodi presenti nell'albero
        */
        public int Contanodi(Nodo n)
        {
            int conta =0;
            if (n!=null); 
            {
                conta=conta + n.N_keys;

                if(n.isLeaf==false)
                {
                    for(int j = 0; j <= n.N_keys; j=j+1)
                    {				  
                        if(n.getChild(j) != null) 
                        {	  
                        conta = conta+ n.getChild(j).N_keys;     
                        }
                    }
                }
            }
        return conta;
        }
        
        /**
         * Salva gli elementi presenti nell'albero in un file, secondo il formato MOVIDA
         * @param current è il nodo di partenza
         * @param FW è un FileWriter inizializzato dal chiamante
         * @throws IOException se ci sono problemi in scrittura
         */
        public void Save(Nodo current, FileWriter FW) throws IOException
        {
            for(int i = 0; i < current.N_keys; i=i+1)
                {
                    FW.write("Title: "+current.getValue(i).getTitle()+"\n" );
                    FW.write("Year: "+current.getValue(i).getYear()+"\n" );
                    FW.write("Director: " + current.getValue(i).getDirector().getName()+"\n" );
                    FW.write("Cast: ");
                    for (int j = 0; j < current.getValue(i).getCast().length; j++)
                    {
                        FW.write(current.getValue(i).getCast()[j].getName());
                        if (j!= current.getValue(i).getCast().length-1) 
                        {
                            FW.write(", ");
                        }
                        
                    }    
                    FW.write("\n");  
                    FW.write("Votes: "+current.getValue(i).getVotes()+"\n" );
                    
                        FW.write("\n");  
                }
            
                int MAX=0;
                for (MAX = 0; MAX < current.MAX_FIGLI; MAX++) //Itero fino a quando non trovo un figlio uguale a null
                {
                    if (current.getChild(MAX)==null)          //e lascio il valore nella variabile MAX
                    {
                        break;
                    }
                }

                if(!current.isLeaf)
                {
                    for(int l = 0; l <= MAX; l=l+1)
                    {				  
                        if(current.getChild(l) != null)  
                        {
                            Save(current.getChild(l), FW);     
                        }
                    }
                }
        }
        
        /**
         * Conta quante persone sono presenti nell'albero
         * @param current è il nodo di partenza
         * @param people è una lista di persone inizializzata dal chiamante e modificata dalla funzione
         */
        public void ContaPersone(Nodo current, ArrayList people)
        {
            for(int i = 0; i < current.N_keys; i=i+1)
                {
                    if (current.getValue(i).getDirector().getName().trim().toLowerCase()!= null) 
                    {
                        people.add(current.getValue(i).getDirector().getName().trim().toLowerCase());
                    }
                    for (int j = 0; j < current.getValue(i).getCast().length; j++)
                    {
                        people.add(current.getValue(i).getCast()[j].getName().trim().toLowerCase());
                    }      
                }
            
                int MAX=0;
                for (MAX = 0; MAX < current.MAX_FIGLI; MAX++) 
                {
                    if (current.getChild(MAX)==null) 
                    {
                        break;
                    }
                }

                if(!current.isLeaf)
                {
                    for(int l = 0; l <= MAX; l=l+1)
                    {				  
                        if(current.getChild(l) != null)  
                        {
                            ContaPersone(current.getChild(l), people);     
                        }
                    }
                }
        }
        
        /**
         * Restituisce il record del film intitolato title
         * @param title è il titolo del film da cercare
         * @param current è il nodo di partenza
         * @return il record del film cercato
         */
        public Movie GetMovieByTitle(String title, Nodo current)
        {
            if (root == null) 
            {
               throw new NotFoundException();
            }   
            else
            {
                int i=0;
                while(title.trim().toLowerCase().compareTo(current.getValue(i).getTitle().trim().toLowerCase())>0)       
                {
                    i++;
                }
                if (current.getValue(i).getTitle().trim().toLowerCase().equals(title.trim().toLowerCase()))
                {
                    return current.getValue(i);                            
                }
                else if (current.getChild(i) != null)
                {
                return GetMovieByTitle(title, current.getChild(i));
                }
                else
                {
                    throw new NotFoundException();
                }
            }
        }
        /**
         * Restituisce il record associato della persona cercata
         * @param name è il nome della persona cercata
         * @param current è il nodo di partenza
         * @return il record associato della persona cercata
         */
        public Person GetPRecord(String name, Nodo current)
        {
            if (root!=null) 
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {
                    if (current.getValue(i).getDirector().getName().trim().toLowerCase().equals(name.trim().toLowerCase())) 
                    {
                        System.out.print(name + " è un regista "+"\n" );
                        return current.getValue(i).getDirector();
                    }
                    else
                    {
                        for (int j = 0; j < current.getValue(i).getCast().length; j++)
                        {
                            if (current.getValue(i).getCast()[j].getName().trim().toLowerCase().equals(name.trim().toLowerCase())) 
                            {
                                System.out.print(name + " è un attore "+"\n" );
                                return current.getValue(i).getCast()[j];
                            }
                            
                        }

                    }
                }
            }
            else throw new NotFoundException();
            
                int MAX=0;
                for (MAX = 0; MAX < current.MAX_FIGLI; MAX++) 
                {
                    if (current.getChild(MAX)==null) 
                    {
                        break;
                    }
                }
                
                if(!current.isLeaf)
                {
                    for(int l = 0; l <= MAX; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {    
                            Person found = GetPRecord(name, current.getChild(l));
                            return found;
                        }
                    }
                }
                throw new NotFoundException();
        }
        
        /**
         * Modifica la lista in input aggiungendo tutti i film
         * @param current è il nodo di partenza
         * @param mm è la lista passata in input
         */
        public void GetALLM(Nodo current, ArrayList mm)
        {
            if (root == null) 
            {
                throw new NotFoundException();
            }
            else
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {       
                    mm.add(current.getValue(i));                                               
                }
            } 
            {
                int MAX=0;
                for (MAX = 0; MAX < current.MAX_FIGLI; MAX++) 
                {
                    if (current.getChild(MAX)==null) 
                    {
                        break;
                    }
                }

                if(!current.isLeaf)
                {
                    for(int l = 0; l <= MAX; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {
                            GetALLM(current.getChild(l), mm);
                        }
                    }
                }
            }
        }
         
        /**
         * Restituisce tutti i film il cui titolo contiene title
         * @param title è una parte di un titolo di un film
         * @param Movies è la lista passata in in input, modificata aggiungendo i film che rispettano i criteri
         * @param current è il nodo di partenza
         */
        public void GetArrayTitle(String title, ArrayList Movies, Nodo current)
        {
            if (root!=null)
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {
                    if(current.getValue(i).getTitle().trim().toLowerCase().contains(title.trim().toLowerCase()))
                    {
                    Movies.add(current.getValue(i));
                    }
                }
            
                int q=0;
                for (q = 0; q < current.MAX_FIGLI; q++) 
                {
                    if (current.getChild(q)==null) 
                    {
                        break;
                    }
                }
                
                if(!current.isLeaf)
                {
                    for(int l = 0; l <= q; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {
                            GetArrayTitle(title, Movies, current.getChild(l));     
                        }
                    }
                }
            }
            else
            {  
              System.out.println("Albero vuoto\n");  
            }  
        }
        
        /**
         * Restituisce tutti i film girati nell'anno year
         * @param year è una parte di un titolo di un film
         * @param Movies è la lista passata in in input, modificata aggiungendo i film che rispettano i criteri
         * @param current è il nodo di partenza
         */
        public void GetArrayYear(int year, ArrayList Movies, Nodo current)
        {
            if (root!=null)
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {
                    if(current.getValue(i).getYear().equals(year))
                    {
                    Movies.add(current.getValue(i));
                    }
                }
            
                int q=0;
                for (q = 0; q < current.MAX_FIGLI; q++) 
                {
                    if (current.getChild(q)==null) 
                    {
                        break;
                    }
                }
                
                if(!current.isLeaf)
                {
                    for(int l = 0; l <= q; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {
                            GetArrayYear(year, Movies, current.getChild(l));     
                        }
                    }
                }
            }
            else
            {  
              System.out.println("Albero vuoto\n");  
            }  
        }
        
        /**
         * Aggiorna la lista in input inserendo i film diretti da name
         * @param name è il nome del regista
         * @param Movies è la lista passata in in input, modificata aggiungendo i film che rispettano i criteri
         * @param current è il nodo di partenza
         */
        public void GetArrayDirector(String name, ArrayList Movies, Nodo current)
        {
            if (root!=null)
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {
                    if(current.getValue(i).getDirector().getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
                    {
                    Movies.add(current.getValue(i));
                    }
                }
            
                int q=0;
                for (q = 0; q < current.MAX_FIGLI; q++) 
                {
                    if (current.getChild(q)==null) 
                    {
                        break;
                    }
                }
                
                if(!current.isLeaf)
                {
                    for(int l = 0; l <= q; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {
                            GetArrayDirector(name, Movies, current.getChild(l));     
                        }
                    }
                }
            }
            else
            {  
              System.out.println("Albero vuoto\n");  
            }  
        }
        
        /**
         * Modifica la lista in input inserendo tutti i film con l'attore name
         * @param name è il nome dell'attore considerato
         * @param Movies è la lista passata in in input, modificata aggiungendo i film che rispettano i criteri
         * @param current è il nodo di partenza
         */
        public void GetArrayStarred(String name, ArrayList Movies, Nodo current)
        {
            if (root!=null)
            {
                for(int i = 0; i < current.N_keys; i=i+1)
                {
                    for (int j = 0; j < current.getValue(i).getCast().length; j++)
                    {
                        if(current.getValue(i).getCast()[j].getName().trim().toLowerCase().equals(name.trim().toLowerCase()))
                        {
                        Movies.add(current.getValue(i));
                        }
                    }
                }
            
                int q=0;
                for (q = 0; q < current.MAX_FIGLI; q++) 
                {
                    if (current.getChild(q)==null) 
                    {
                        break;
                    }
                }
                
                if(!current.isLeaf)
                {
                    for(int l = 0; l <= q; l=l+1)
                    {				  
                        if(current.getChild(l) != null) 
                        {
                            GetArrayStarred(name, Movies, current.getChild(l));     
                        }
                    }
                }
            }
            else
            {  
              System.out.println("Albero vuoto\n");  
            }    
        }
}
