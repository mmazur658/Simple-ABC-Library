package pl.mazur.simpleabclibrary.utils.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * Helper class used to generate bar codes.
 * 
 * @author Marcin Mazur
 *
 */
public class BarcodeGenerator {

	/**
	 * Generates the bar code for given value
	 * 
	 * @param barcodeValue
	 *            The String containing the value
	 * @return A byte[] representing the generated bar code
	 */
	public static byte[] generateBarcode(String barcodeValue) {

		final int dpi = 60;
		Code39Bean code39Bean = new Code39Bean();
		code39Bean.setModuleWidth(UnitConv.in2mm(2.0f / dpi)); // width of the bar code
		code39Bean.setWideFactor(3);
		code39Bean.doQuietZone(false);
		code39Bean.setMsgPosition(HumanReadablePlacement.HRP_NONE); // no description under the bar code
		File tempFile = null;
		try {
			tempFile = File.createTempFile("barcode", ".png");
			OutputStream out = new FileOutputStream(tempFile);
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			code39Bean.generateBarcode(canvas, barcodeValue);
			canvas.finish();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] bytesArray = new byte[(int) tempFile.length()];

		FileInputStream fis;
		try {
			fis = new FileInputStream(tempFile);
			fis.read(bytesArray);
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return bytesArray;
	}
}
