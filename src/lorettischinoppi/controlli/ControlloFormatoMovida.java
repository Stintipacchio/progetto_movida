/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lorettischinoppi.controlli;

import java.io.FileReader;
import movida.commons.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * @author Andrea
 */
public class ControlloFormatoMovida 
{  
   /**
    * La funzione verifica se un file rispetta il formato MOVIDA
    * @param f è il file da cui si vogliono prendere i dati
    * @throws MovidaFileException se il file non rispetta il formato MOVIDA o ci sono problemi in lettura
    */
    public void ControlloMovida(File f)
    {
        String campi[]=
        {        
            "Title: ",	
            "Year: ",
            "Director: ",
            "Cast: ",
            "Votes: "
        };
        
        if(f.exists()==false || f.canRead()==false) //Se il file non esiste o ci sono problemi in lettura
        {
            throw new MovidaFileException();
        }


        try
        {
            FileReader fr=new FileReader(f);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while(line!=null) 
            {
                for(int i = 0; i < campi.length; i++) //Finchè ci sono dei campi
                {
                    if(line.startsWith(campi[i]))     //e le righe iniziano con uno dei campi
                    {
                        //System.out.println(line);     //stampo la riga
                        line=reader.readLine();       //leggo riga successiva
                    }
                    else if (line.isBlank())          //se si tratta di una riga vuota
                    {
                        //System.out.println(line);
                        line = reader.readLine();
                        i = -1;                    //riporta i a -1 (i campi ricominciano e l'incremento avviene successivamente)

                        if(line.isBlank())        //se ci sono due righe vuote consecutive
                        {
                            throw new MovidaFileException(); 
                        }
                    }
                    else
                    {
                      throw new MovidaFileException();
                    }
                }
            }
            reader.close();
        } 
        catch(IOException e)
        {
        System.out.println(e.getMessage());
        }   
   }
}
