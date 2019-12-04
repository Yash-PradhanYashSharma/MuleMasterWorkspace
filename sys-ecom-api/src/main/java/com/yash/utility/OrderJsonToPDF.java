package com.yash.utility;

import java.io.ByteArrayOutputStream;
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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class OrderJsonToPDF {


	public byte[] jsontopdf(String filename, String content, String varr) throws IOException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try

		{
			int unitPrice=0;
			int totalPrice=0;
			
			String orderId= "";
			JSONObject jsonObject = new JSONObject(content);
			JSONObject jsonVar = new JSONObject(varr);

			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			float[] columnWidths = { 4f, 4f, 4f };
			table.setWidths(columnWidths);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			
			table.addCell("Price");
			table.addCell("Quantity");
			table.addCell("ProductName");
			
			

			table.setHeaderRows(1);
			PdfPCell[] cells = table.getRow(0).getCells();
			for (int j = 0; j < cells.length; j++) {
				cells[j].setBackgroundColor(BaseColor.GRAY);
			}
			JSONArray arr = jsonObject.getJSONArray("orderItems");
			System.out.print(arr.length());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject json = arr.getJSONObject(i);
				Iterator<String> keys = json.keys();

				while (keys.hasNext()) {
					String key = keys.next();
					System.out.print(key.contains("productId"));
					if (key.contains("productId")) {
					
						table.addCell((String) json.get("productId"));
					}
					
					if (key.contains("unitPrice")) {
						table.addCell(String.valueOf(json.get("unitPrice")));
					
					unitPrice= json.getInt("unitPrice");
					}
					if (key.contains("quantity")) {
						table.addCell(String.valueOf(json.get("quantity")));
						totalPrice =totalPrice + (json.getInt("quantity") * unitPrice);
					}
					if (key.contains("orderId")) {
					
						orderId= String.valueOf( json.get("orderId"));
					}
				}

			}
			System.out.print("totalPrice"+ jsonVar.getInt("orderTotal"));
			PdfWriter.getInstance(document, out);

			Font f = new Font(FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.DARK_GRAY);
			Paragraph p = new Paragraph("Thank you for your order! ", f);

			p.setAlignment(Paragraph.ALIGN_LEFT);
			Font f2 = new Font(FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.DARK_GRAY);
			Paragraph p4 = new Paragraph("Order Details", f2);
			p4.setAlignment(Paragraph.ALIGN_CENTER);

			document.open();
			document.add(p4);
			document.add(new Paragraph("\n\n"));
			document.add(p);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Customer Name: " + jsonVar.getString("userName")));
			document.add(new Paragraph("\n\n"));
			document.add(new Paragraph("Dear " + jsonVar.getString("userName")
					+ ",\nThank you for your order from Yash Website Store. Once your package ships we will send you tracking number.\n\nIf you have any queries about your order, you can email us at yashtech@gmail.com."));

			document.add(new Paragraph("\n\n"));

			Paragraph p1 = new Paragraph("Your Order #" + orderId, f);
			document.add(p1);

			document.add(table);
			document.add(new Paragraph("\n"));
			Font f3 = new Font(FontFamily.TIMES_ROMAN, 14.0f, Font.UNDEFINED, BaseColor.BLACK);
			Paragraph p5 = new Paragraph("Discounted Total: $" + String.valueOf(totalPrice - jsonVar.getInt("orderTotal")), f3);
			p5.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(p5);
			Paragraph p3 = new Paragraph("Amount Total: $" + String.valueOf(jsonVar.getInt("orderTotal")), f3);
			p3.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(p3);
			document.add(new LineSeparator(0.5f, 100, null, 0, -5));
			
			document.add(new Paragraph(
					"Thanks for your purchase! \nFor any futher questions do not hesitate to contact us!"));

			document.close();

		} catch (DocumentException e)

		{

			e.printStackTrace();

		}
		System.out.print("DONEEEE");
		return out.toByteArray();

	}
}