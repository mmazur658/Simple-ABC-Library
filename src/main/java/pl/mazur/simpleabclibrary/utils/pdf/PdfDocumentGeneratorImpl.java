package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.util.ClassLoaderUtil;

import pl.mazur.simpleabclibrary.entity.Book;
import pl.mazur.simpleabclibrary.entity.User;

/**
 * Utility class used to generate PDF files containing confirmations.
 * 
 * @author Marcin
 *
 */
@Component
public class PdfDocumentGeneratorImpl implements PdfDocumentGenerator {

	private Font headerFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED, 12.0f,
			Font.BOLD, BaseColor.BLACK);
	private Font basicFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED, 11.0f,
			Font.BOLD, BaseColor.BLACK);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public File generateCreatedAccountConfirmation(User theUser) {

		File tempFile = null;
		Document document = new Document(PageSize.A4, 36, 36, 24, 72);

		try {
			tempFile = File.createTempFile("tempfile", ".pdf");
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			Footer event = new Footer();
			writer.setPageEvent(event);

			document.addTitle("Abc Library");
			document.addAuthor("Simple ABC Library");
			document.open();
			document.add(addHeader("Potwierdzenie utworzenia konta"));

			Paragraph paragraph = new Paragraph("\nWitamy w ABC Library!!\n\n", headerFont);
			document.add(paragraph);

			paragraph = new Paragraph(
					"Dziêkujemy za utworzenie konta! Teraz mo¿esz zalogowaæ siê, przegl¹daæ ksiêgozbiory oraz rezerwowaæ ksi¹¿ki."
							+ " Rezerwacja jest wa¿na 24h, nie mo¿na zarezerwowaæ wiêcej jak 3 ksi¹¿ki. Pamiêtaj,"
							+ " aby w wolnej chwili uzupe³niæ pozosta³e dane. \n\nDo zobaczenia!\n\n",
					basicFont);
			document.add(paragraph);

			paragraph = new Paragraph("Twoje dane: \n", headerFont);
			document.add(paragraph);

			float[] columnWidths = { 45, 55 };
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(60);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("ID: ", basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(theUser.getId()), basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Imiê: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getFirstName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Nazwisko: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getLastName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("email: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getEmail(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			table.setHorizontalAlignment(Element.ALIGN_LEFT);

			document.add(table);
			document.newPage();
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;

	}

	@Override
	public File generateBorrowedBookConfirmation(List<Book> bookList, User theUser, Date expectedEndDate,
			String employeeName) {

		File tempFile = null;
		Document document = new Document(PageSize.A4, 36, 36, 24, 72);

		try {
			tempFile = File.createTempFile("tempfile", ".pdf");
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			Footer event = new Footer(employeeName);
			PdfPCell cell;
			Paragraph paragraph;
			// customer table
			float[] userTableColumnsWidth = { 45, 55 };
			PdfPTable userTable = new PdfPTable(userTableColumnsWidth);

			// book table
			float[] bookTableColumnsWidth = { 20, 80, 60, 40 };
			String[] columnsName = { "LP.", "Tytu³", "Autor", "ISBN" };
			PdfPTable bookTable = new PdfPTable(bookTableColumnsWidth);

			for (int i = 0; i < columnsName.length; i++) {
				cell = new PdfPCell(new Phrase(columnsName[i], headerFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);
			}

			writer.setPageEvent(event);
			document.addTitle("Abc Library");
			document.addAuthor("Simple ABC Library");
			document.open();
			document.add(addHeader("Potwierdzenie wydania ksi¹¿ek"));

			// customer details:
			paragraph = new Paragraph("Klient: \n", headerFont);
			document.add(paragraph);

			cell = new PdfPCell(new Phrase("ID: ", basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(theUser.getId()), basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("Imiê: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getFirstName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("Nazwisko: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getLastName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("email: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getEmail(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			userTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			userTable.setWidthPercentage(60);

			document.add(userTable);

			paragraph = new Paragraph("\n\n");
			document.add(paragraph);

			paragraph = new Paragraph("Wypo¿yczone ksi¹¿ki: \n", headerFont);

			for (int i = 0; i < bookList.size(); i++) {

				cell = new PdfPCell(new Phrase(String.valueOf(i + 1), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getTitle(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getAuthor(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getIsbn(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

			}
			bookTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			document.add(bookTable);

			paragraph = new Paragraph("\n\n Ksi¹¿ki nale¿y zwróciæ do: " + sdf.format(expectedEndDate), headerFont);
			document.add(paragraph);

			paragraph = new Paragraph("\n\n\n .................................. \n");
			document.add(paragraph);

			paragraph = new Paragraph("         Odbiorca");
			document.add(paragraph);

			document.newPage();
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;
	}

	@Override
	public File generateReturnBookConfirmation(User theUser, String employeeName, List<Book> bookList) {
		File tempFile = null;
		Document document = new Document(PageSize.A4, 36, 36, 24, 72);

		try {
			tempFile = File.createTempFile("tempfile", ".pdf");
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			Footer event = new Footer(employeeName);
			PdfPCell cell;
			Paragraph paragraph;
			// customer table
			float[] userTableColumnsWidth = { 45, 55 };
			PdfPTable userTable = new PdfPTable(userTableColumnsWidth);

			// book table
			float[] bookTableColumnsWidth = { 20, 80, 60, 40 };
			String[] columnsName = { "LP.", "Tytu³", "Autor", "ISBN" };
			PdfPTable bookTable = new PdfPTable(bookTableColumnsWidth);

			for (int i = 0; i < columnsName.length; i++) {
				cell = new PdfPCell(new Phrase(columnsName[i], headerFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);
			}

			writer.setPageEvent(event);
			document.addTitle("Abc Library");
			document.addAuthor("Simple ABC Library");
			document.open();
			document.add(addHeader("Potwierdzenie zwrotu ksi¹¿ek"));

			// customer details:
			paragraph = new Paragraph("Klient: \n", headerFont);
			document.add(paragraph);

			cell = new PdfPCell(new Phrase("ID: ", basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(theUser.getId()), basicFont));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("Imiê: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getFirstName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("Nazwisko: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getLastName(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			cell = new PdfPCell(new Phrase("email: ", basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);
			cell = new PdfPCell(new Phrase(theUser.getEmail(), basicFont));
			cell.setBorder(Rectangle.NO_BORDER);
			userTable.addCell(cell);

			userTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			userTable.setWidthPercentage(60);

			document.add(userTable);

			paragraph = new Paragraph("\n\n");
			document.add(paragraph);

			paragraph = new Paragraph("Zwrócone ksi¹¿ki: \n", headerFont);

			for (int i = 0; i < bookList.size(); i++) {

				cell = new PdfPCell(new Phrase(String.valueOf(i + 1), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getTitle(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getAuthor(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

				cell = new PdfPCell(new Phrase(bookList.get(i).getIsbn(), basicFont));
				cell.setBorder(Rectangle.NO_BORDER);
				bookTable.addCell(cell);

			}
			bookTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			document.add(bookTable);

			paragraph = new Paragraph("\n\n Ksi¹¿ki zosta³y zwrócone w dniu: " + sdf.format(new Date()), headerFont);
			document.add(paragraph);

			paragraph = new Paragraph("\n\n\n .................................. \n");
			document.add(paragraph);

			paragraph = new Paragraph("          Odebra³", basicFont);
			document.add(paragraph);

			document.newPage();
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;
	}

	public static PdfPTable addHeader(String listTitle) throws BadElementException, MalformedURLException, IOException {

		Font secondLineFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED, 18.0f,
				Font.BOLD, BaseColor.BLACK);

		URL url = ClassLoaderUtil.getResource("ABC_logo_v2.png", BookLabelGeneratorImpl.class);
		Image logoImage = Image.getInstance(url);

		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell();
		cell.addElement(logoImage);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setPaddingBottom(8);
		cell.setBorderWidth(2);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(listTitle, secondLineFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setRowspan(1);
		cell.setPaddingTop(10);
		table.addCell(cell);
		return table;
	}
}

class Footer extends PdfPageEventHelper {

	private PdfPTable footerTable;
	private PdfTemplate total;
	private String fullUserName;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public Footer(String fullUserName) {
		this.fullUserName = fullUserName;
	}

	public Footer() {

	}

	public void onEndPage(PdfWriter writer, Document document) {

		footerTable = new PdfPTable(new float[] { 30, 10, 5 });
		footerTable.setTotalWidth(527);

		PdfPCell cell;

		if (fullUserName == null)
			cell = new PdfPCell(new Phrase("Generated at: " + sdf.format(new Date())));
		else
			cell = new PdfPCell(new Phrase("Generated by: " + fullUserName + " " + sdf.format(new Date())));
		cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);

		cell.setBorder(Rectangle.TOP);
		cell.setBorderWidth(1);
		footerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(String.format("Page %d of", writer.getPageNumber())));
		cell.setHorizontalAlignment(Rectangle.ALIGN_RIGHT);

		cell.setBorder(Rectangle.TOP);
		cell.setBorderWidth(1);
		footerTable.addCell(cell);

		try {
			cell = new PdfPCell((Image.getInstance(total)));
			cell.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
			cell.setPaddingTop(2);
			cell.setBorder(Rectangle.TOP);
			cell.setBorderWidth(1);
			footerTable.addCell(cell);
		} catch (Exception e) {
			e.printStackTrace();
		}

		footerTable.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 16);
	}

	public void onCloseDocument(PdfWriter writer, Document document) {

		ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2,
				4, 0);
	}

}