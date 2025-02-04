package com.library.bible.websocket.handler;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler implements InitializingBean{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<WebSocketSession> sessionSet = new HashSet<>();

    public WebSocketHandler() {
        logger.info("websocket handler - 인스턴스 생성");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionSet.remove(session);
        logger.info("접속 세션 삭제");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionSet.add(session);
        logger.info("새 접속 세션 추가");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        logger.info("수신 메시지: " + message.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("웹소켓 에러!", exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
    
    public int getSessionCount() {
        return sessionSet.size();
    }

    
    private class AlarmRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    long time = System.currentTimeMillis() + 32400000;
                    int randomNum = (int) (Math.random() * 45 + 1);
                    String messageContent = "{\"time\":" + time + ",\"randomNum\":" + randomNum + "}";

                    for (WebSocketSession session : sessionSet) {
                        if (session.isOpen()) {
                            TextMessage message = new TextMessage(messageContent);
                            session.sendMessage(message);
                            logger.info("메시지 전송: {}", messageContent);
                        }
                    }
                    Thread.sleep(100000); // 간격으로 메시지 전송
                    
                } catch (InterruptedException e) {
                    logger.error("쓰레드 중단!", e);
                    break;
                } catch (Exception e) {
                    logger.error("메시지 전송 실패!", e);
                }
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    	logger.info("Runnable 시작");
        Thread t = new Thread(new AlarmRunnable());
        t.start();
    }
}
