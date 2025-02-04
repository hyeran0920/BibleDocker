package com.library.bible.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.library.bible.book.model.Book;

import jakarta.servlet.http.HttpServletResponse;

public class ExcelService {
	
	//Excel file 로 받은 booklist를 db에 저장
    public static List<Book> parseExcelFile(MultipartFile file) throws IOException {
        List<Book> bookList = new ArrayList<>();
        
        System.out.println("parse excel file");
        
        if(file.isEmpty()) {System.out.println("there is no file");}

        
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

        	Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 건너뛰기

                Book book = new Book();
                System.out.println("bookTitle"+row.getCell(0).getStringCellValue());
                book.setBookTitle(row.getCell(0).getStringCellValue());
                book.setBookAuthor(row.getCell(1).getStringCellValue());
                book.setBookPublisher(row.getCell(2).getStringCellValue());
                book.setBookReleaseDate(row.getCell(3).getStringCellValue());
                book.setBookCategory(row.getCell(4).getStringCellValue());
                book.setBookPrice(Long.valueOf((long) row.getCell(5).getNumericCellValue()));
                book.setBookImgPath("");
                book.setBookDetail(row.getCell(6).getStringCellValue());
                book.setBookTotalStock(Long.valueOf((long) row.getCell(7).getNumericCellValue()));
                book.setBookTotalStock(Long.valueOf((long) row.getCell(8).getNumericCellValue()));
                book.setBookLocation(row.getCell(9).getStringCellValue());
				book.setBookQrPath("");
                
                bookList.add(book);
            }

        }

        return bookList;
    }
    
    
    
    public static void generateExcelFile(List<Book> books, HttpServletResponse response) throws IOException {
        // 워크북 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Book List");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Book ID", "Title", "Author", "Publisher", "Release Date", "Category", "Price", "Stock", "Rent Stock", "Location"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // 데이터 추가
        int rowNum = 1;
        for (Book book : books) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getBookTitle());
            row.createCell(1).setCellValue(book.getBookAuthor());
            row.createCell(2).setCellValue(book.getBookPublisher());
            row.createCell(3).setCellValue(book.getBookReleaseDate().toString());
            row.createCell(4).setCellValue(book.getBookCategory());
            row.createCell(5).setCellValue(book.getBookPrice());
            row.createCell(6).setCellValue(book.getBookDetail());
            row.createCell(7).setCellValue(book.getBookTotalStock());
            row.createCell(8).setCellValue(book.getBookRentStock());
            row.createCell(9).setCellValue(book.getBookLocation());
        }

        // HTTP 응답 설정 및 파일 반환
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        response.getOutputStream().write(outputStream.toByteArray());
    }
    
    
    
}
