package com.library.bible.upload.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.library.bible.book.service.IBookService;
import com.library.bible.member.model.Member;
import com.library.bible.resolver.AuthMember;
import com.library.bible.upload.service.IUploadService;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    @Autowired
    IBookService bookService;

    private final String UPLOAD_DIR = "uploads/book-images/";
    private final String BOOK_QR_DIR="uploads/book-qr/";
    private final String MEMBER_QR_DIR="uploads/member-qr/";
    
    
    private final IUploadService uploadService;

    public UploadController(IUploadService uploadService) {
        this.uploadService = uploadService;
    }

    //DELETE IMAGE////////////////////////////////////////////////////////////
    @DeleteMapping("/book-image")
    public ResponseEntity<String> deleteBookImage(@RequestParam("bookid") Long bookId) {
        return getDeleteResponse(uploadService.deleteBookImage(bookId), "Book image", bookId);
    }


    @DeleteMapping("/book-qr-image")
    public ResponseEntity<String> deleteBookQRImage(@RequestParam("bookid") Long bookId) {
        return getDeleteResponse(uploadService.deleteBookQRImage(bookId), "Book QR image", bookId);
    }

    @DeleteMapping("/member-qr-image")
    public ResponseEntity<String> deleteMemberQRImageByToken(@AuthMember Member member) {
    	System.out.println("delete member qr image by token 지금 비활성화해놓음!!!");
    	return null;
        //return getDeleteResponse(uploadService.deleteMemberQRImage(member.getMemId()), "Member QR image", member.getMemId());
    }

    private ResponseEntity<String> getDeleteResponse(boolean success, String type, Long id) {
        if (success) {
            return ResponseEntity.ok(type + " deleted for ID: " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(type + " not found for ID: " + id);
        }
    }
    
    
    //INSERT BOOK IMG///////////////////////////////////////////////////////
    @PostMapping("/book-image")
    public ResponseEntity<String> uploadBookImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bookid") Long bookId) {

        boolean uploaded = uploadService.uploadBookImage(bookId, file);
        if (uploaded) {
            return ResponseEntity.ok("Book image uploaded successfully for book ID: " + bookId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload book image for book ID: " + bookId);
        }
    }

    //GET IMG/////////////////////////////////////////////////////////
    @GetMapping("/book-image")
    public ResponseEntity<byte[]> getBookImage(@RequestParam("bookid") Long bookId) {
        //System.out.println("here is get obok img");
    	byte[] imageBytes = uploadService.getBookImage(bookId);

        if (imageBytes != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(imageBytes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    
    @GetMapping("/member-qr-image")
    public ResponseEntity<byte[]> getMemberQRImageByToken(@AuthMember Member member) {
    	byte[] imageBytes = uploadService.getMemberQRImage(member.getMemId());
    	
    	if (imageBytes != null) {
    		return ResponseEntity.ok()
    				.header("Content-Type", "image/jpeg")
    				.body(imageBytes);
    	} else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }
    
}
