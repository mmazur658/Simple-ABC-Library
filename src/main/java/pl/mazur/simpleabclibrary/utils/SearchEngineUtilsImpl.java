package pl.mazur.simpleabclibrary.utils;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchEngineUtilsImpl implements SearchEngineUtils {

	
	private ForbiddenWords forbiddenWords;
	
	@Autowired
	public SearchEngineUtilsImpl(ForbiddenWords forbiddenWords) {
		this.forbiddenWords=forbiddenWords;
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
		if ((startResult - range) < 0) {
			return 0;
		} else {
			return startResult - range;
		}
	}

	@Override
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults, int range) {
		if ((startResult + range) > amountOfResults) {
			return startResult;
		} else {
			return startResult + range;
		}
	}

	@Override
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue, int range) {
		if ((startResult + range) > amountOfResults) {
			return "Wyniki od " + (startResult + 1) + " do " + amountOfResults;
		} else {
			return "Wyniki od " + (startResult + 1) + " do " + showMoreLinkValue;
		}
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

			if ((searchParametersValue[i] == null) && !(session.getAttribute(searchParametersName[i]) == null))
				searchParametersValue[i] = (String) session.getAttribute(searchParametersName[i]);

			searchParameters[i] = (searchParametersValue[i] == null) ? "" : searchParametersValue[i];
		}
		for (int i = 0; i < searchParameters.length-1; i++) {
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
