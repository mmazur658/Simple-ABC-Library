package pl.mazur.simpleabclibrary.utils;

/**
 * Interface used to prevent of using the words, that might be potentially
 * dangerous for the database.
 * 
 * @author Marcin Mazur
 *
 */
public interface ForbiddenWords {

	/**
	 * Returns TRUE if the given statement contains the one or more forbidden words.
	 * 
	 * @param hqlToCheck
	 *            The String containing the statement to check
	 * @return A boolean representing the result
	 */
	public boolean findForbiddenWords(String hqlToCheck);
}
