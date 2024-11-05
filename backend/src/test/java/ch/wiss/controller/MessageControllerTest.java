package ch.wiss.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
	public void whenQuestionControllerInjected_thenNotNull() throws Exception {
	    assertThat(messageController).isNotNull(); //hier wird nur geprüft, ob unser SUT existiert
	}

	@Test
	public void whenGetAllQuestions_getValidQuestions() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/question"))
    // damit kannst du das eigentliche Ergebnis der Abfrage auf der Konsole ausgeben
    .andDo(res -> System.out.println(res.getResponse().getContentAsString())) 
    .andExpect(MockMvcResultMatchers.status().isOk())		
    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    /* Hinweis: da keine Datenbank im Hintergrund aktiv ist, wird eine leere Liste geliefert. */
  }

  @Test
	public void whenPostRequestToQuestionAndInValidQuestion_thenCorrectResponse() throws Exception {
	  // Frage-JSON String ohne Frage und ohne Antworten erstellen
	  String question = "{\"question\": null }";
	  // führt POST-Request mit unvollständiger Frage aus  
	  mockMvc.perform(MockMvcRequestBuilders.post("/question/1")
	    .content(question) // setzt Request-Content
	    .contentType(MediaType.APPLICATION_JSON))
	    //teste, ob Systemantwort korrekt ist
	    .andExpect(MockMvcResultMatchers.status().isBadRequest())
	    // teste erwartete Fehlermeldungen 
	    .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("Question is mandatory"))
	    .andExpect(MockMvcResultMatchers.jsonPath("$.answers").value("There need to be 3 answers to a question"))
	    .andExpect(MockMvcResultMatchers.content()
	    .contentType(MediaType.APPLICATION_JSON));
  }

}
