package ch.wiss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.wiss.model.Message;
import ch.wiss.repositories.MessageRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/{senderId}/{recipientId}")
    public List<Message> getMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
        return messageRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    }

    @GetMapping("/{senderId}")
    public List<Message> getMessagesByID(@PathVariable Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    @GetMapping("/")
    public ResponseEntity<Iterable<Message>> getMessages() {
        return ResponseEntity.ok().body(messageRepository.findAll());
    }

    @PostMapping
    public Message sendMessage(@Valid @RequestBody Message message) {
        return messageRepository.save(message);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@Valid @PathVariable Long messageId, @Valid @RequestBody Message updatedMessage) {
        return messageRepository.findById(messageId)
        .map(existingMessage -> {
            existingMessage.setMessageContent(updatedMessage.getMessageContent());
            Message savedMessage = messageRepository.save(existingMessage);
            return ResponseEntity.ok(savedMessage);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
