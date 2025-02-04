package com.library.bible.upload.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
import com.library.bible.book.model.Book;
import com.library.bible.exception.CustomException;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.member.model.Member;
import com.library.bible.qr.QRCodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadService implements IUploadService {

    private static final String MEMBER_QR_DIR = "uploads/member-qr/";
    private static final String BOOK_QR_DIR = "uploads/book-qr/";
    private static final String BOOK_IMAGE_DIR = "uploads/book-images/";
    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".png", ".jpeg"};
    
    
  //GET IMG!!//////////////////////////////////////////////////////////////////////////////
    /*
     @param directory 이미지가 저장된 디렉토리 경로
     @param id 이미지 파일명 (bookId 또는 memId)
     @return 이미지 바이트 배열 또는 null (이미지 없음)
     */
    
    private byte[] getImageFromDirectory(String directory, Long id, String imageType) {
        for (String extension : IMAGE_EXTENSIONS) {
            Path filePath = Paths.get(directory, id + extension);

            if (Files.exists(filePath)) {
                try {
                    log.info("Serving {} image: {}", imageType, filePath);
                    return Files.readAllBytes(filePath);
                } catch (IOException e) {
                    log.error("Failed to read {} image for ID: {}", imageType, id, e);
                    return null;
                }
            }
        }

        log.warn("No {} image found for ID: {}", imageType, id);
        return null;
    }


    @Override
    public byte[] getBookImage(Long bookId) {
        return getImageFromDirectory(BOOK_IMAGE_DIR, bookId, "book");
    }


	@Override
	public byte[] getMemberQRImage(int memId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public byte[] getBookQRImage(Long bookId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
    //DELETE/////////////////////////////////////////////////////////////////////////////
    private boolean deleteFile(String dir, Long id) {
        String[] extensions = {".jpg", ".png", ".jpeg"};

        for (String extension : extensions) {
            Path filePath = Paths.get(dir, Long.toString(id) + extension);

            if (Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                    log.info("Deleted file: {}", filePath);
                    return true;
                } catch (IOException e) {
                    log.error("Failed to delete file: {}", filePath, e);
                    return false;
                }
            }
        }

        log.warn("No file found for ID: {} in directory: {}", Long.toString(id), dir);
        return false;
    }


    @Override
    public boolean deleteMemberQRImage(int memId) {
        return false;
    	//return deleteFile(MEMBER_QR_DIR, memId);
    }


    @Override
    public boolean deleteBookQRImage(Long bookId) {
        return deleteFile(BOOK_QR_DIR, bookId);
    }

    @Override
    public boolean deleteBookImage(Long bookId) {
        return deleteFile(BOOK_IMAGE_DIR, bookId);
    }

    
    
    
    
    
    
    //INSERT///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean uploadBookImage(Long bookId, MultipartFile file) {
    	try {
        	
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
            String fileName = Long.toString(bookId) + fileExtension;
            Path filePath = Paths.get(BOOK_IMAGE_DIR, fileName);
            
            Files.createDirectories(filePath.getParent()); // 디렉토리 생성 (없을 경우)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Uploaded book image to: {}", filePath);
            return true;
        } catch (IOException e) {
            log.error("Failed to upload book image for book ID: {}", bookId, e);
            return false;
        }

    }

    
  //CREATE QR///////////////////////////////////////
    @Override
    public void createMemberQRImage(Member member) {
        try {
        	String memId=Integer.toString(member.getMemId());
        	
            String data = "Member ID: " + memId + ", Email: " + member.getMemEmail();
            String filePath = MEMBER_QR_DIR + memId + ".png";
            QRCodeGenerator.generateQRCode(data, filePath);
            log.info("Generated QR Code for member ID: {}", memId);
        } catch (WriterException | IOException e) {
            log.error("Error generating QR code for member ID: {}", member.getMemId(), e);
            throw new CustomException(ExceptionCode.QR_IMAGE_CREATION_FAIL);
        }
    }
    
    @Override
    public void createBookQRImage(Book book, Long bookId) {
    	try {
    		//System.out.println("book qr id="+bookId);
        	//create qr
            String data = "Book ID: " + Long.toString(bookId) + 
            		", Title: " + book.getBookTitle() + 
            		", Author: " + book.getBookAuthor() + 
            		", Publisher: "+ book.getBookPublisher() +
            		", Category: "+book.getBookCategory();
            String filePath = BOOK_QR_DIR + Long.toString(bookId) + ".png";
            QRCodeGenerator.generateQRCode(data, filePath);
        } catch (WriterException | IOException e) {
            log.error("Error generating QR code for book ID: {}", bookId, e);
            throw new CustomException(ExceptionCode.QR_IMAGE_CREATION_FAIL);
        }

    }
    
    


}
