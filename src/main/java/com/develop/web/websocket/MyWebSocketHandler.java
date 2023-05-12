package com.develop.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {
    private final MyWebSocketClient webSocketClient;

    @Autowired
    public MyWebSocketHandler(MyWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("웹 소켓 연결이 열렸습니다. : " + session);
        webSocketClient.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("받은 메시지: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("웹 소켓 연결이 닫혔습니다.");
        webSocketClient.removeSession(session);
    }
}

