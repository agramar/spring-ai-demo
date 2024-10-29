package io.github.agramar.aidemo.controller;

import io.github.agramar.aidemo.dto.ChatPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/open-ai")
@RestController
public class OpenAiController {

    private final ChatModel openAiChatModel;

    @GetMapping("/sse")
    public Flux<String> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "data: " + LocalTime.now().toString() + "\n\n");
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatPostDto chatPost) {

        OpenAiChatOptions.builder()
                .withModel(OpenAiApi.ChatModel.O1_MINI)
                .withTemperature(0.4)
                .build();

        return openAiChatModel.call(
                new Prompt(
                        chatPost.getMessage(),
                        OpenAiChatOptions.builder()
                                .withModel(OpenAiApi.ChatModel.GPT_3_5_TURBO)
                                .withTemperature(0.4)
                                .withTopP(0.4)
                                .build()
                ));
    }
}
