package pl.mazur.simpleabclibrary.utils;

import org.springframework.stereotype.Component;

/**
 * Utility class used to prevent of using the words, that might be potentially
 * dangerous for the database.
 * 
 * @author Marcin Mazur
 *
 */
@Component
public class ForbiddenWordsImpl implements ForbiddenWords {

	/**
	 * The array containing the forbidden words
	 */
	private final String[] FORBIDDEN_WORDS = { "insert into", "drop table", "show databases", "show tables",
			"create table", "alter table", "truncate table", "delete from", "drop index" };

	@Override
	public boolean findForbiddenWords(String hqlToCheck) {

		hqlToCheck = hqlToCheck.toLowerCase();

		boolean hasForbiddenWord = false;

		for (String word : FORBIDDEN_WORDS) {

			if (hqlToCheck.contains(word)) {
				hasForbiddenWord = true;
				break;
			}

		}

		return hasForbiddenWord;
	}

}
