package com.library.bible.alarm.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {
	
	//"0 50 2 * * ?"
	//반납 마감 당일 알림
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendBookReturnDDayReminder() {
        System.out.println("내일 책 반납 마감일 입니다!!!!!!!!!!!!!!!!");
        System.out.println("실행 시간: " + new Date());
    }
    
    //반납 마감 임박 알림
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendBookReturnReminder() {
        System.out.println("오늘 책 반납 마감일 입니다");
        System.out.println("실행 시간: " + new Date());
    }
    
	// 초,분,시,일,월,요일(연도)
    // ‘*’는 모든값
	// ‘?’는	사용 안함,	‘-’는 기간, ‘,’는 값 지정+나열, ‘/’는 시작/반복 간격
}

