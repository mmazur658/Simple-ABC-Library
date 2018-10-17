package pl.mazur.simpleabclibrary.utils;

import javax.servlet.http.HttpSession;

public interface SearchEngineUtils {
	
	public boolean hasTableAnyParameters(String[] searchParameters);
	
	public long generateShowLessLinkValue(Integer startResult, int range);
	
	public long generateShowMoreLinkValue(Integer startResult, long amountOfResults,int range);
	
	public String generateResultRange(Integer startResult, long amountOfResults, long showMoreLinkValue, int range) ;
	
	public void clearSearchParameters(HttpSession session,String[] searchParametersName);
	
	public String[] prepareTableToSearch(HttpSession session, String[] searchParametersName, String[] searchParametersValue);
	
	public String prepareHqlUsingSearchParameters(String[] searchParameters, String searchType, String[] fieldsName);
}
