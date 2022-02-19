package lorettischinoppi.controlli;

/**
 *
 * @author Andrea
 */
public class NotAValidAlgorithmException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "\nScegliere un algoritmo valido: le scelte disponibili sono InsertionSort o HeapSort";
	}
}
