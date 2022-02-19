/*
 *  Header
 */
package lorettischinoppi.controlli;

/**
 *
 * @author Andrea
 */
public class NotFoundException extends RuntimeException 
{
    private static final long serialVersionUID = 1L;

    @Override
	public String getMessage() {
		return "\nL'elemento cercato non è stato trovato";
	}
}
