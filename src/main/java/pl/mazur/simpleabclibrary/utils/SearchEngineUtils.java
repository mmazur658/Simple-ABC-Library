package pl.mazur.simpleabclibrary.utils;

import javax.servlet.http.HttpSession;

/**
 * Interface used to perform operations for service classes.
 * 
 * @author Marcin
 *
 */
public interface SearchEngineUtils {

	/**
	 * Returns TRUE if the given array contains the value different than null or "".
	 * 
	 * @param searchParameters
	 *            The String[] containing the array to be checked
	 * @return A boolean representing the result
	 */
	public boolean hasTableAnyParameters(String[] searchParameters);

	/**
	 * Calculates and returns the value of the "ShowLessLink".
	 * 
	 * @param startResult
	 *            The Integer containing the value of first index
	 * @param range
	 *            The int containing the number that specifies how much results are
	 *            displayed on the page
	 * @return A long representing the value of the "ShowLessLink"
	 */
	public long generateShowLessLinkValue(Integer startResult, int range);

	/**
	 * Calculates and returns the value of the "ShowMoreLink".
	 * 
	 * @param startResult
	 *            The Integer containing the value of first index
	 * @param amountOfResults
	 *            The long containing the value of total results
	 * @param range
	 *            The int containing the number that specifies how much results are
	 *            displayed on the page
	 * @return A long representing the value of the "ShowMoreLink"
	 */
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults, int range);

	/**
	 * Calculates and returns the result range.
	 * 
	 * @param startResult
	 *            The Integer containing the value of first index
	 * @param amountOfResults
	 *            The long containing the value of total results
	 * @param showMoreLinkValue
	 *            The long containing the value of "ShowMoreLink"
	 * @param range
	 *            The int containing the number that specifies how much results are
	 *            displayed on the page
	 * @return A String representing the result range
	 */
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue, int range);

	/**
	 * Clears session attributes with given names.
	 * 
	 * @param session
	 *            The HttpSession containing the user's session
	 * @param searchParametersName
	 *            The String[] containing the names of the parameters to be cleared
	 */
	public void clearSearchParameters(HttpSession session, String[] searchParametersName);

	/**
	 * Adds the session attributes with the given name and value.<br>
	 * 
	 * @param session
	 *            The HttpSession containing the user's session
	 * @param searchParametersName
	 *            The String[] containing the names of search parameters
	 * @param searchParametersValue
	 *            The String[] containing the values of search parameters
	 * @return A String[] representing the prepared array
	 */
	public String[] prepareTableToSearch(HttpSession session, String[] searchParametersName,
			String[] searchParametersValue);

	/**
	 * Creates and returns the HQL Statement for given search parameters
	 * 
	 * @param searchParameters
	 *            The String[] containing the search parameters
	 * @param searchType
	 *            The String[] containing the type of the search
	 * @param fieldsName
	 *            The String[] containing the name of the entity fields
	 * @return A String representing the HQL Statement
	 */
	public String prepareHqlUsingSearchParameters(String[] searchParameters, String searchType, String[] fieldsName);
}
