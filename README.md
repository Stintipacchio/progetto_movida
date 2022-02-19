Progetto di Algoritmi e Strutture di dati

Algoritmi e Strutture dati implementate:
    -Algoritmi di Ordinamento: HeapSort e InsertionSort
    -Strutture Dati: ABR e Btree

Contenuti:
    -src
        -movida
            -commons: Contiene le interfacce e le classi fornite in partenza
        -lorettischnoppi
            -controlli: Contiene tutte le eccezioni utilizzate, la classe che controlla se un file rispetta il formato MOVIDA, le funzioni per rimuovere i duplicati dagli 						array e una classe utile per lavorare con le stringhe
            -grafi: Contiene l'implementazione dei grafi
            -Map: Contiene l'implementazione delle strutture dati
            -sorting: Contiene l'implementazione degli algoritmi di sorting
            -IMovidaCore: contiene la classe principale
            -Main: Classe di test delle funzioni

Scelte implementative:
Nella classe Collaboration abbiamo aggiunto la possibilità di passare in input al costruttore una lista di film per inizializzare il campo movies.

Strutture dati: Abbiamo deciso di inserire nei nostri alberi elementi di tipo Movie e abbiamo scelto come parametro per l'ordinamento all'interno delle strutture il titolo dei film convertito in caratteri minuscoli e senza gli spazi prima e dopo (trim).

Algoritmi di sorting: entrambi gli algoritmi HeapSort e InserionSort sono impostati per ordinare in ordine decrescente inoltre, abbiamo effettuato una modifica in modo da riutilizare la stessa funzione per diverse chiamate attraverso il parametro booleano choose che permette di decidere se si desidera ordinare secondo l'anno di pubblicazione del film o secondo il numero di voti.

Strutture Dati: siccome le nostre strutture dati mantengono intrisecamente un proprio ordine, nella maggior parte dei casi per creare una funzione abbiamo sfruttato l'algoritmo standard di visita, adattandolo secondo i casi.
Tuttavia per far si che anche gli algoritmi di ordinamento fossero utilizzati, abbiamo implementato una parte delle funzioni utilizzando un array contenente dati di tipo Movie, ordinato opportunamente.

Grafo: abbiamo scelto l'implementazione tramite matrice di adiacenza perchè permette con relativa facilità di trovare i collaboratori dei diversi attori, nonostante non sia quella più efficiente dal punto di vista del costo computazionale.
Inoltre si tratta di un'implementazione che entrambi i membri del gruppo volevano provare.

Sempre per quanto riguarda i grafi, riteniamo di dover specificare l'utilizzo diffuso della variabile NeedZero: siccome l'inizializzazione di default di un array di interi prevede che tutte le posizioni siano occupate dal valore 0, noi lavorando con gli indici che rappresentano i vari attori all'interno della matrice di adiacenza abbiamo ritenuto necessario distinguere il caso in cui lo 0 fosse causato dall'inizializzazione e quello in cui 0 rappresentasse il primo degli attori. NeedZero acquisisce quindi il valore 0 se e solo se lo zero precedentemente citato rappresenta un attore e resta 1 altrimenti. Utilizzando questa variabile possiamo dunque escludere o includere la prima posizione dell'array grazie ad Arrays.CopyofRange.

Per quanto concerne alcune funzioni, abbiamo cercato documentazione su internet per comprendere meglio il funzionamento di alcune operazioni e strutture.
In particolare:
- Alcune operazioni su ArrayList
- Alcune operazioni su Array
- Operazione .ToArray
- Alcune operazioni sugli alberi
- BufferedReader, BufferedWriter, FileReader, FileWriter



