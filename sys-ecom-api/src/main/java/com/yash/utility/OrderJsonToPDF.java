package com.yash.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.parser.clipper.Path;

public class OrderJsonToPDF {

	public byte[] jsontopdf(String filename, String content, String varr) throws IOException {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int unitPrice = 0;
			int totalPrice = 0;

			String orderId = "";
			String path = "D:\\muleecomdemo\\sys-ecom-api-mule\\src\\main\\resources\\images\\yashlogo.jpg";
			JSONObject jsonObject = new JSONObject(content);
			JSONObject jsonVar = new JSONObject(varr);

			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			float[] columnWidths = { 4f, 4f, 4f };
			table.setWidths(columnWidths);

			Font f6 = new Font(FontFamily.UNDEFINED, 10.0f, Font.UNDEFINED, BaseColor.BLACK);
			PdfPCell cellValue1 = new PdfPCell(new Phrase("Description", f6));
			cellValue1.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell cellValue2 = new PdfPCell(new Phrase("UnitPrice", f6));
			cellValue2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell cellValue3 = new PdfPCell(new Phrase("Quantity", f6));
			cellValue3.setHorizontalAlignment(Element.ALIGN_CENTER);

			table.addCell(cellValue1).setBorder(PdfPCell.NO_BORDER);
			table.addCell(cellValue3).setBorder(PdfPCell.NO_BORDER);
			table.addCell(cellValue2).setBorder(PdfPCell.NO_BORDER);

			// table.setHeaderRows(1);
			/*
			 * PdfPCell[] cells = table.getRow(0).getCells(); for (int j = 0; j <
			 * cells.length; j++) { cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY); }
			 */

			PdfPTable table2 = new PdfPTable(3);
			table2.setWidthPercentage(100);
			table2.setSpacingBefore(10f);
			table2.setSpacingAfter(10f);
			table2.setWidths(columnWidths);
			table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// table2.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

			JSONArray arr = jsonObject.getJSONArray("orderItems");
			Font f7 = new Font(FontFamily.UNDEFINED, 10.0f, Font.UNDEFINED, BaseColor.BLACK);

			for (int i = 0; i < arr.length(); i++) {

				JSONObject json = arr.getJSONObject(i);

				PdfPCell cellValue4 = new PdfPCell(new Phrase((String) json.get("productId"), f7));
				cellValue4.setHorizontalAlignment(Element.ALIGN_CENTER);
				// cellValue4.setBackgroundColor(BaseColor.LIGHT_GRAY);
				PdfPCell cellValue5 = new PdfPCell(new Phrase(String.valueOf(json.get("unitPrice")), f7));
				cellValue5.setHorizontalAlignment(Element.ALIGN_CENTER);
				// cellValue5.setBackgroundColor(BaseColor.LIGHT_GRAY);
				PdfPCell cellValue6 = new PdfPCell(new Phrase(String.valueOf(json.get("quantity")), f7));
				cellValue6.setHorizontalAlignment(Element.ALIGN_CENTER);
				// cellValue6.setBackgroundColor(BaseColor.LIGHT_GRAY);

				table2.addCell(cellValue4).setBorder(PdfPCell.NO_BORDER);
				table2.addCell(cellValue6).setBorder(PdfPCell.NO_BORDER);
				;
				table2.addCell(cellValue5).setBorder(PdfPCell.NO_BORDER);
				unitPrice = json.getInt("unitPrice");
				totalPrice = totalPrice + (json.getInt("quantity") * unitPrice);
				orderId = String.valueOf(json.get("orderId"));

			}

			System.out.print("totalPrice" + jsonVar.getInt("orderTotal"));
			PdfWriter.getInstance(document, out);

			Font f = new Font(FontFamily.UNDEFINED, 10.0f, Font.UNDEFINED, BaseColor.BLACK);
			Paragraph p = new Paragraph("Thank you for your order! ", f);

			p.setAlignment(Paragraph.ALIGN_LEFT);
			Font f2 = new Font(FontFamily.UNDEFINED, 18.0f, Font.UNDEFINED, BaseColor.BLACK);
			Paragraph p4 = new Paragraph("Order Invoice", f2);
			p4.setAlignment(Paragraph.ALIGN_CENTER);

			document.open();

			Image img = Image.getInstance(path);
			document.add(img);
			document.add(p4);
			// document.add(new LineSeparator(0.5f, 100, null, 0, -5));
			document.add(new Paragraph("\n"));
			document.add(p);
			document.add(new Paragraph("\n"));
			// document.add(new Paragraph("Customer Name: " +
			// jsonVar.getString("userName")));
			// document.add(new Paragraph("\n\n"));
			document.add(new Paragraph("Dear " + jsonVar.getString("userName")
					+ ",\nThank you for your order from Yash Website Store. Once your package ships we will send you tracking number.\n\nIf you have any queries about your order, you can email us at contact@yash.com.",
					f));

			document.add(new Paragraph("\n"));

			Paragraph p1 = new Paragraph("Your Order #" + orderId, f);
			document.add(p1);
			document.add(new Paragraph("\n"));
			document.add(table);
			document.add(new LineSeparator(0.5f, 100, null, 0, -5));
			document.add(table2);
			document.add(new LineSeparator(0.5f, 100, null, 0, -5));
			document.add(new Paragraph("\n"));
			Font f3 = new Font(FontFamily.UNDEFINED, 10.0f, Font.UNDEFINED, BaseColor.BLACK);
			Font f4 = new Font(FontFamily.UNDEFINED, 10.0f, Font.BOLD, BaseColor.BLACK);
			Paragraph p5 = new Paragraph(
					"Discounted Total: $" + String.valueOf(totalPrice - jsonVar.getInt("orderTotal")), f3);
			p5.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(p5);
			Paragraph p3 = new Paragraph("Amount Total: $" + String.valueOf(jsonVar.getInt("orderTotal")), f4);
			p3.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(p3);
			document.add(new LineSeparator(0.5f, 100, null, 0, -5));

			document.add(new Paragraph(
					"Thanks for your purchase! \nFor any futher questions do not hesitate to contact us!", f));
			document.close();
		} catch (DocumentException e){
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}