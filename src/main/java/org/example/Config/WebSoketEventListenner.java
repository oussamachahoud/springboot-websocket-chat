package org.example.Config;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Chat.ChatMessage;
import org.example.Chat.TypeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
@Slf4j
public class WebSoketEventListenner {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void HandelWebSokectListenner(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User Disconnected " , username);
            var chatMessae = ChatMessage.builder()
                    .type(TypeMessage.leave)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessae);

        }
    }

}
