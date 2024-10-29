package io.github.agramar.aidemo.controller;

import io.github.agramar.aidemo.dto.ChatPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.AssistantMessage.ToolCall;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ollama")
@RestController
public class OllamaController {

    private final OllamaChatModel ollamaChatModal;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatPostDto chatPost) {
        ChatResponse chatResponse = ollamaChatModal.call(new Prompt(chatPost.getMessage(),
                OllamaOptions.builder()
                        .withModel(OllamaModel.LLAMA3_2)
                        .withTemperature(0.4)
                        .withTopP(0.4)
                        .build())
        );
        Generation generation = chatResponse.getResult();
        AssistantMessage assistantMessage = generation.getOutput();
        List<ToolCall> toolCalls = assistantMessage.getToolCalls();
        String textContent = assistantMessage.getContent();
        return chatResponse;
    }
}
