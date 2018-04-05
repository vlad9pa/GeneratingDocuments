package com.example.documents;

import com.example.documents.service.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.List;

/**
 * @author Vlad Miliutin
 */
@RestController
public class DocumentController {

    @Autowired
    private PdfService pdfService;

    @GetMapping(value = "/pdf")
    public void generatePdf(@RequestParam(value = "text", required = false, defaultValue = "Hello There") String text,
                            HttpServletResponse response) throws Exception {

        pdfService.generatePdf(response.getOutputStream());
        response.getOutputStream().close();
    }
}
