package com.holisticon.nimgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holisticon.nimgame.request.DrawMatchesRequest;
import com.holisticon.nimgame.request.StartGameRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired GameStateService gameStateService;

    @Autowired ComputerService computerService;

    final int matches = 2;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void correctNumberOfDrawnMatches_whenDrawMatches_thenStatus200() throws Exception {
        DrawMatchesRequest json = new DrawMatchesRequest(matches);

        this.mockMvc.perform(post("/draw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(json)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("player_move").value(matches));
    }

    @Test
    public void correctNumberOfDrawnMatches_whenDrawMatches_optimalStrategy() throws Exception {
        DrawMatchesRequest json = new DrawMatchesRequest(matches);

        this.computerService.setStrategy(true);

        this.mockMvc.perform(post("/draw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(json)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("player_move").value(matches))
                .andExpect(jsonPath("computer_move").value(4 - matches));
    }

    @Test
    public void correctSetOfStrategy_whenStartingGame_thenStatus200() throws Exception {
        // Select optimal strategy
        StartGameRequest request = new StartGameRequest(true);

        this.mockMvc.perform(post("/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("optimal_strategy").value(true));

        assertThat(computerService.isPlaysOptimalStrategy()).isTrue();
    }

    @Test
    public void drawMatchesBeforeGameStart() throws Exception {
        DrawMatchesRequest json = new DrawMatchesRequest(matches);

        this.mockMvc.perform(post("/draw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(json)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }
}
