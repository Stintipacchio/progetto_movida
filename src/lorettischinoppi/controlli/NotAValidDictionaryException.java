/*
 *  Header
 */
package lorettischinoppi.controlli;

/**
 *
 * @author Andrea
 */
public class NotAValidDictionaryException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "\nScegliere un dizionario valido: le scelte disponibili sono ABR o BTree";
	}

}
