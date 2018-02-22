package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;

import pl.mazur.simpleabclibrary.entity.Book;

public interface BookLabelGenerator {

	public File generateBookLabel(Book theBook, String userName);

}