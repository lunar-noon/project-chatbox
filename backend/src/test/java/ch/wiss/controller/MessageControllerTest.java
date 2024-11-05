package ch.wiss.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ch.wiss.model.Message;
import ch.wiss.repositories.MessageRepository;
import ch.wiss.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class MessageControllerTest {

  @MockBean //simuliert DOC
  MessageRepository messageRepository;
  @MockBean //simuliert DOC
  UserRepository userRepository;
  
  @Autowired // einbinden SUT
  MessageController messageController;
  
  @Autowired // eigentlicher "Testautomat"
  MockMvc mockMvc;
  
  @Test
  public void assertSetupWorks() {
    assertTrue(true);
  }

  /**
	* Wie immer bei Unit-Tests achtest Du auf:
	* sprechende Methodennamen
	* @Test Annotation darüber
	*/
	@Test
	public void whenMessageControllerInjected_thenNotNull() throws Exception {
	    assertThat(messageController).isNotNull(); //hier wird geprüft, ob das SUT existiert
	}

	@Test
	public void whenGetAllMessages_getValidMessages() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/messages/"))
    // damit kannst du das eigentliche Ergebnis der Abfrage auf der Konsole ausgeben
    .andDo(res -> System.out.println(res.getResponse().getContentAsString())) 
    .andExpect(MockMvcResultMatchers.status().isOk())		
    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    /* Hinweis: da keine Datenbank im Hintergrund aktiv ist, wird eine leere Liste geliefert. */
  }

  @Test
	public void whenPostRequestToMessageAndInValidMessage_thenCorrectResponse() throws Exception {
	  // Frage-JSON String ohne Frage und ohne Antworten erstellen
	  String message = "{\"messageContent\": null }";
	  // führt POST-Request mit unvollständiger Frage aus  
	  mockMvc.perform(MockMvcRequestBuilders.post("/messages")
	    .content(message) // setzt Request-Content
	    .contentType(MediaType.APPLICATION_JSON))
	    //teste, ob Systemantwort korrekt ist
	    .andExpect(MockMvcResultMatchers.status().isBadRequest())
	    // teste erwartete Fehlermeldungen 
	    .andExpect(MockMvcResultMatchers.content()
	    .contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void testSendMessage_Positive() {
    Message message = new Message(1L, 2L, "Hello!");

    when(messageRepository.save(any(Message.class))).thenReturn(message);
    Message sentMessage = messageController.sendMessage(message);

    assertNotNull(sentMessage);
    assertEquals("Hello!", sentMessage.getMessageContent());
    verify(messageRepository, times(1)).save(message);
}

@Test
void testUpdateMessage_Positive() {
    Message existingMessage = new Message(1L, 2L, "Old content");
    Message updatedMessage = new Message(1L, 2L, "Updated content");

    when(messageRepository.findById(1L)).thenReturn(Optional.of(existingMessage));
    when(messageRepository.save(any(Message.class))).thenReturn(updatedMessage);

    ResponseEntity<Message> response = messageController.updateMessage(1L, updatedMessage);

    assertNotNull(response.getBody());
    assertEquals("Updated content", response.getBody().getMessageContent());
    verify(messageRepository, times(1)).save(existingMessage);
}

void testGetMessages_Positive() {
  Message message1 = new Message(1L, 2L, "Hello!");
  Message message2 = new Message(1L, 2L, "How are you?");

  when(messageRepository.findBySenderIdAndRecipientId(1L, 2L)).thenReturn(List.of(message1, message2));
  List<Message> messages = messageController.getMessages(1L, 2L);

  assertEquals(2, messages.size());
  assertEquals("Hello!", messages.get(0).getMessageContent());
  assertEquals("How are you?", messages.get(1).getMessageContent());
  verify(messageRepository, times(1)).findBySenderIdAndRecipientId(1L, 2L);
}

@Test
void testGetMessages_Negative_InvalidSender() {
    // Assuming 99L is a non-existent sender ID
    when(messageRepository.findBySenderIdAndRecipientId(99L, 2L)).thenReturn(List.of());

    List<Message> messages = messageController.getMessages(99L, 2L);

    assertTrue(messages.isEmpty(), "Messages should be empty for a non-existent sender");
    verify(messageRepository, times(1)).findBySenderIdAndRecipientId(99L, 2L);
}


}
