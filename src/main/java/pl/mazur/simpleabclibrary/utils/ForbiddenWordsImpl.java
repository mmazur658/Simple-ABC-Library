package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

@Component
public class ForbiddenWordsImpl implements ForbiddenWords{

	String[] forbiddenWords = { "insert into", "drop table", "show databases", "show tables", "create table",
			"alter table", "truncate table", "delete from","drop index",";"};

	public boolean findForbiddenWords(String hqlToCheck) {

		hqlToCheck = hqlToCheck.toLowerCase();

		boolean hasRestrictedWord = false;

		for (String word : forbiddenWords) {

			if (hqlToCheck.contains(word)) {
				hasRestrictedWord = true;
				break;
			}

		}

		return hasRestrictedWord;
	}

}
