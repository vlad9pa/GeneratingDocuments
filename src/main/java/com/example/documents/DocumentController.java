package com.example.documents;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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

    private final Float TABLE_WIDTH = 580f;
    private final float PAGE_HEIGHT = 842f;
    private final float PAGE_WIDTH = 595.0f;

    @GetMapping(value = "/pdf")
    public void generatePdf(@RequestParam(value = "text", required = false, defaultValue = "Hello There") String text,
                            HttpServletResponse response) throws Exception {

        Document document = new Document(PageSize.A4);

        PdfWriter writer = PdfWriter.getInstance(document,
                response.getOutputStream());

        ArrayList<Map<String, String>> maps = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("Name", "Vlad");
        map1.put("Address", "SomeAddress");
        map1.put("Email", "email@email.com");
        map1.put("Website", "google.com");
        map1.put("Info", "Cloud");
        map1.put("BusinessType", "IDK)");

        Map<String, String> map2 = new HashMap<>();
        map2.put("Name", "Vovo");
        map2.put("Address", "New Address");
        map2.put("Email", "vovo_email@email.com");
        map2.put("Website", "apple.com");
        map2.put("Info", "Blockchain");
        map2.put("BusinessType", "IDK");

        Map<String, String> map3 = new HashMap<>();
        map3.put("Name", "Artem");
        map3.put("Address", "Artem Address");
        map3.put("Email", "artem_email@email.com");
        map3.put("Website", "itunes.com");
        map3.put("Info", "musssiccc");
        map3.put("BusinessType", "A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. A LOT OF TEXT. ");

        maps.add(map1);
        maps.add(map2);
        maps.add(map3);

        document.setPageSize(PageSize.A4);
        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk(text, font);

        document.setMargins(2, 2, 2, 2);

        document.add(chunk);
        document.addTitle("Custom Generated PDF");

        document.newPage();
        renderTable(document, maps);

        document.newPage();
        renderRotatedTable(document, writer, maps);

        document.close();

        response.getOutputStream().close();
    }

    private void renderTable(Document document, List<Map<String, String>> data) throws DocumentException {
        System.out.println("'renderTable' invoked");

        document.setPageSize(PageSize.A4.rotate());
        PdfPTable table = new PdfPTable(data.get(0).size());
        table.setTotalWidth(583);
        table.setWidthPercentage(100);

        data.get(0).forEach((key, value) -> {
            addTableHeader(table, key);
        });

        data.forEach( map -> {
            map.forEach((key, value) -> {
                addRow(table, value);
            });
        });

        document.add(table);

        System.out.println("'renderTable' table drown");
    }

    private void addTableHeader(PdfPTable table, String text){
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(text));
        table.addCell(header);
    }

    private void addRow(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        table.addCell(cell);
    }

    private void addRotatedTableHeader(PdfPTable table, String text){

        Phrase phrase = new Phrase(text);

        Font font = FontFactory.getFont(FontFactory.COURIER, 6, BaseColor.RED);
        phrase.setFont(font);

        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(phrase);
        header.setRotation(90);
        table.addCell(header);
    }

    private void addRotatedRow(PdfPTable table, String text) {
        Phrase phrase = new Phrase(text);

        Font font = FontFactory.getFont(FontFactory.COURIER, 6, BaseColor.RED);
        phrase.setFont(font);

        PdfPCell cell = new PdfPCell(phrase);
        cell.setRotation(90);

        if(text.length() > 15){
            float oldWidth = generateWidth(table.size());
            table.setLockedWidth(false);
            table.setTotalWidth(3 * oldWidth);
            table.setLockedWidth(true);
        }

        table.addCell(cell);
    }

    private void renderRotatedTable(Document document, PdfWriter writer, List<Map<String, String>> data) throws DocumentException {
        System.out.println("'renderRotatedTable' invoked");

        Rectangle pageSize = PageSize.A0.rotate();
        document.setPageSize(pageSize);

        int columnNumber = data.get(0).size();

//        writer.addPageDictEntry(PdfName.ROTATE, PdfPage.LANDSCAPE);

        PdfPTable table = new PdfPTable(columnNumber);

//        for(int i = 0; i < freespace; i++){
//            Paragraph paragraph = new Paragraph(" ");
//            document.add(paragraph);
//        }

        table.setTotalWidth(PAGE_HEIGHT - 40);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidthPercentage(100);
        table.setLockedWidth(true);

//        data.get(0).forEach((key, value) -> {
//            System.out.println("Key = " + key);
//            Iterator<Map<String, String>> iterator = data.iterator();
//
//            boolean first = true;
//
//            while (iterator.hasNext()){
//                Map<String, String> interestingMap = iterator.next();
//
//                if(first){
//                    System.out.println("drawing header with name = " + key);
//                    addRotatedTableHeader(table, key);
//                    first = false;
//                }
//
//                System.out.println("drawing row with text = " + interestingMap.get(key));
//                addRotatedRow(table, interestingMap.get(key));
//            }
//
//        });


        //Drawing headers
        data.get(0).forEach((key,value) -> {
            addTableHeader(table, key);
        });

        //Drawing rows and columns
        data.forEach(map -> {
            map.forEach((key, value) -> {
                addRow(table, value);
            });
        });

        document.add(table);

        System.out.println("'renderRotatedTable' table drown");
    }

    private float generateWidth(int size){
        System.out.println("'generateWidth invoked with size = " + size);

        int columnSize = 6;

        if(size <= 4){
            System.out.println("'generateWidth' size <= 4");

            columnSize = 40;
        }

        if(size > 4 && size <= 10){
            System.out.println("'generateWidth' 4 < size <= 10");

            columnSize = 16;
        }

        if(size > 10 && size <= 20){

            System.out.println("'generateWidth' 10 < size <= 20");

            columnSize = 12;
        }

        System.out.println("ColumnSize = " + columnSize);
        System.out.println("Width = " + columnSize * size);
        return columnSize * size;
    }
}
