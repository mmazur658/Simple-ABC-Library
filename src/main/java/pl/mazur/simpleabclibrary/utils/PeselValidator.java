package pl.mazur.simpleabclibrary.utils;

import java.util.Date;

public interface PeselValidator {

	public int getBirthDay();

	public int getBirthMonth();

	public int getBirthYear();

	public String getSex(String PESELNumber);

	public boolean validatePesel(String PESELNumber);

	public Date getBirthDate(String PESELNumber);
}
