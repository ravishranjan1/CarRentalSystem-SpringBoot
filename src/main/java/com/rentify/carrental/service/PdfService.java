package com.rentify.carrental.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.rentify.carrental.model.BookingModel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    public ByteArrayInputStream generateBookingPdf(BookingModel booking) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Paragraph title = new Paragraph("Rentify Booking Receipt", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Booking ID : " + booking.getId()));
            document.add(new Paragraph("Customer : " + booking.getCustomer().getName()));
            document.add(new Paragraph("Car : " + booking.getCar().getModel()));
            document.add(new Paragraph("Registration No : " + booking.getCar().getRegistrationNo()));
            document.add(new Paragraph("Price Per Hour : ₹" + booking.getCar().getPricePerHour() + "/hour"));
            document.add(new Paragraph("Start Date Time : " + booking.getStartDateTime()));
            document.add(new Paragraph("End Date Time : " + booking.getEndDateTime()));
            document.add(new Paragraph("Status : " + booking.getStatus()));
            document.add(new Paragraph("Total Amount : ₹" + booking.getTotalAmount()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for using Rentify 🚗"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
