package com.library.bible.book.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.library.bible.book.model.Book;


@Mapper
@Repository
public interface IBookRepository {
	
	int getBookCount();
	int getBookCount(int categoryid);
	
	List<Map<String, Object>> getBookListMap();
	Map<String, Object> getBookInfoMap(Long bookid);
	List<Book> getBookList();
	Book getBookInfo(Long bookid);
	
	void updateBookImgPath(Long bookId, String bookImgPath);
	void updateBookQrPath(Long bookId, String bookQrPath);
	void updateBook(Book book);
	
	void insertBook(Book book);
	
	void deleteBook(Long bookid);
	
	@Transactional("transactionManager")
	int deleteBook(Long bookid, String author);
	
	List<Map<String, Object>> getAllAuthor();
	List<Map<String, Object>> getAllPublisher();
	List<Map<String, Object>> getAllCategory();
	List<Map<String, Object>> getBooksByCategory(String category);
	List<Map<String, Object>> searchBooks(String keyword);
	
}