package com.example.documents.service;

import com.itextpdf.text.DocumentException;

import java.io.OutputStream;

public interface PdfService {

    void generatePdf(OutputStream out) throws DocumentException;
}
