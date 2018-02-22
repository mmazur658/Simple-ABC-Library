package pl.mazur.simpleabclibrary.utils.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.util.ClassLoaderUtil;

import pl.mazur.simpleabclibrary.entity.Book;

@Component
public class BookLabelGeneratorImpl implements BookLabelGenerator {

	private Font tableFontNormal = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED, 10.0f,
			Font.NORMAL, BaseColor.BLACK);
	private Font tableFontBold = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED, 10.0f,
			Font.BOLD, BaseColor.BLACK);

	public File generateBookLabel(Book theBook, String userName) {

		File tempFile = null;
		PdfWriter writer;
		Document document = new Document();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		URL url = ClassLoaderUtil.getResource("ABC_logo_v2.png", BookLabelGeneratorImpl.class);

		String stringBookId = String.valueOf(theBook.getId());
		try {
			tempFile = File.createTempFile("tempFile", ".pdf");
			writer = PdfWriter.getInstance(document, new FileOutputStream(tempFile));

			Image logoImage = Image.getInstance(url);
			Image barcodeImage = Image.getInstance(BarcodeGenerator.generateBarcode(stringBookId));

			PdfPCell cell;
			Paragraph paragraph;

			float[] columnWidths = { 30, 40, 40, 40, 35 };
			PdfPTable table = new PdfPTable(columnWidths);

			Rectangle pageSize = new Rectangle(400, 500);

			document.addTitle("Book Label");
			document.addAuthor("Simple ABC Library");
			document.setPageSize(pageSize);
			document.setMarginMirroring(true);
			document.setMargins(20, 20, 20, 20);
			document.open();

			// sekcja na logo
			cell = new PdfPCell();
			cell.addElement(logoImage);
			// cell.addElement(new Paragraph("logo"));
			cell.setColspan(4);
			cell.setRowspan(2);
			table.addCell(cell);

			// sekcja na id
			cell = new PdfPCell();
			paragraph = new Paragraph("ID");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setExtraParagraphSpace(6);
			cell.addElement(paragraph);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph(stringBookId);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setExtraParagraphSpace(6);
			paragraph.setPaddingTop(20);
			cell.addElement(paragraph);
			table.addCell(cell);

			// sekcja na bar code
			cell = new PdfPCell();
			cell.addElement(barcodeImage);
			cell.setColspan(5);
			table.addCell(cell);

			// sekcja na dane:

			// title
			cell = new PdfPCell();
			paragraph = new Paragraph("Title: ", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(34);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph(theBook.getTitle(), tableFontBold);
			cell.addElement(paragraph);
			cell.setColspan(4);
			cell.setFixedHeight(34);
			cell.setPaddingBottom(4);
			cell.setPaddingTop(0);
			table.addCell(cell);

			// author
			cell = new PdfPCell();
			paragraph = new Paragraph("Author: ", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(30);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph(theBook.getAuthor(), tableFontBold);
			cell.addElement(paragraph);
			cell.setColspan(4);
			cell.setFixedHeight(30);
			table.addCell(cell);

			// Publisher
			cell = new PdfPCell();
			paragraph = new Paragraph("Publ.:", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(30);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph(theBook.getPublisher(), tableFontBold);
			cell.addElement(paragraph);
			cell.setColspan(4);
			cell.setFixedHeight(30);
			table.addCell(cell);

			// ISBN
			cell = new PdfPCell();
			paragraph = new Paragraph("ISBN:", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(30);
			table.addCell(cell);

			cell = new PdfPCell();
			String stringIsbn = String.valueOf(theBook.getIsbn());
			paragraph = new Paragraph(stringIsbn, tableFontBold);
			cell.addElement(paragraph);
			cell.setColspan(4);
			cell.setFixedHeight(30);
			table.addCell(cell);

			// Lang and pages
			cell = new PdfPCell();
			paragraph = new Paragraph("Lang: ", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(25f);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph(theBook.getLanguage().toUpperCase(), tableFontBold);
			cell.addElement(paragraph);
			cell.setFixedHeight(25f);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			cell = new PdfPCell();
			paragraph = new Paragraph("Pages: ", tableFontNormal);
			cell.addElement(paragraph);
			cell.setFixedHeight(25f);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			cell = new PdfPCell();
			String stringPages = String.valueOf(theBook.getPages());
			paragraph = new Paragraph(stringPages, tableFontBold);
			cell.addElement(paragraph);
			cell.setFixedHeight(25f);
			cell.setColspan(2);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			// footer
			cell = new PdfPCell();
			paragraph = new Paragraph("Generated by: " + userName + " at " + sdf.format(new Date()), tableFontNormal);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			cell.addElement(paragraph);
			cell.setColspan(5);
			cell.setPaddingBottom(8);
			table.addCell(cell);

			document.add(table);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;
	}
}
