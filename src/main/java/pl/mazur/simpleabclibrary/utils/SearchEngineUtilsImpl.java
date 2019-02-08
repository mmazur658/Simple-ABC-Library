package pl.mazur.simpleabclibrary.utils;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class used to perform operations for service classes.
 * 
 * @author Marcin
 *
 */
@Component
public class SearchEngineUtilsImpl implements SearchEngineUtils {

	/**
	 * The ForbiddenWords interface
	 */
	private ForbiddenWords forbiddenWords;

	/**
	 * Constructs a SearchEngineUtilsImpl with the ForbiddenWords.
	 * 
	 * @param forbiddenWords
	 *            The ForbiddenWords interface
	 */
	@Autowired
	public SearchEngineUtilsImpl(ForbiddenWords forbiddenWords) {
		this.forbiddenWords = forbiddenWords;
	}

	@Override
	public boolean hasTableAnyParameters(String[] searchParameters) {
		
		boolean hasAnyParameters = false;
		for (int i = 0; i < searchParameters.length; i++) {
			if (searchParameters[i] != "")
				hasAnyParameters = true;
		}
		
		return hasAnyParameters;
	}

	@Override
	public long generateShowLessLinkValue(Integer startResult, int range) {

		long showLessLinkValue = ((startResult - range) < 0) ? 0 : (startResult - range);
		
		return showLessLinkValue;

	}

	@Override
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults, int range) {

		long showMoreLinkValue = ((startResult + range) > amountOfResults) ? startResult : (startResult + range);
		
		return showMoreLinkValue;

	}

	@Override
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue, int range) {

		String resultRange = ((startResult + range) > amountOfResults)
				? ("Wyniki od " + (startResult + 1) + " do " + amountOfResults)
				: ("Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue);

		return resultRange;
	}

	@Override
	public void clearSearchParameters(HttpSession session, String[] searchParametersName) {
		
		for (String paramName : searchParametersName)
			session.setAttribute(paramName, null);
		
	}

	@Override
	public String[] prepareTableToSearch(HttpSession session, String[] searchParametersName,
			String[] searchParametersValue) {

		String[] searchParameters = new String[searchParametersValue.length];

		for (int i = 0; i <= searchParametersValue.length - 1; i++) {

			if (searchParametersValue[i] != null)
				session.setAttribute(searchParametersName[i], searchParametersValue[i]);

			if ((searchParametersValue[i] == null) && !(session.getAttribute(searchParametersName[i]) == null)) {
				searchParametersValue[i] = (String) session.getAttribute(searchParametersName[i]);
			}
			searchParameters[i] = (searchParametersValue[i] == null) ? "" : searchParametersValue[i];
		}
		
		for (int i = 0; i < searchParameters.length - 1; i++) {
			if (forbiddenWords.findForbiddenWords(searchParameters[i]))
				searchParameters[i] = "";
		}

		return searchParameters;
	}

	@Override
	public String prepareHqlUsingSearchParameters(String[] searchParameters, String searchType, String[] fieldsName) {
		
		boolean isContent = false;

		StringBuilder sb = new StringBuilder();
		sb.append(searchType);
		if (!searchParameters[0].equals("")) {
			sb.append(fieldsName[0] + " like '%" + searchParameters[0] + "%'");
			isContent = true;
		}
		
		for (int i = 1; i <= fieldsName.length - 1; i++) {
			if (!searchParameters[i].equals("")) {
				if (isContent) {
					sb.append(" AND ");
					sb.append(fieldsName[i] + " like '%" + searchParameters[i] + "%'");
				} else {
					sb.append(fieldsName[i] + " like '%" + searchParameters[i] + "%'");
					isContent = true;
				}
			}
		}
		sb.append(" AND isActive = true ORDER BY id ASC");

		return sb.toString();
	}

}
