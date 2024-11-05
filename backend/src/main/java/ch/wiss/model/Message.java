package ch.wiss.model;

import java.time.LocalDateTime;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Validated
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @NotNull(message = "Sender cannot be empty")
    // @ManyToOne
    private Long senderId;
    @NotNull(message = "Recipient cannot be empty")
    // @ManyToOne
    private Long recipientId;
    @NotNull(message = "Message cannot be empty")
    @Size(min = 1, max = 500, message = "The Message must be under 500 characters long!")
    private String messageContent;
    private LocalDateTime messageTime;

    public Message() {
        this.messageTime = LocalDateTime.now();
    }

    public Message(Long senderId, Long recipientId, String messageContent) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageContent = messageContent;
        this.messageTime = LocalDateTime.now();
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

}
