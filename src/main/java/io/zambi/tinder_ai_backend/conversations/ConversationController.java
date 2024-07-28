/*******************************************************************************
 * Copyright © 2024, Planview, Inc. and its Affiliates.
 *
 * This program and the accompanying materials are made available under the
 * Planview Subscription Terms which accompany this distribution, and are
 * available at https://www.planview.com/legal/legal-terms/.
 *******************************************************************************/

package io.zambi.tinder_ai_backend.conversations;

import io.zambi.tinder_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ConversationRepository conversationRepository;

    private final ProfileRepository profileRepository;

    public ConversationController(ConversationRepository conversationRepository, ProfileRepository profileRepository) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest createConversationRequest) {
        profileRepository.findById(createConversationRequest.profileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                createConversationRequest.profileId(),
                List.of()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(@PathVariable String conversationId, @RequestBody ChatMessage chatMessage) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        profileRepository.findById(chatMessage.authorId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        ChatMessage chatMessageWithTime = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );
        conversation.messages().add(chatMessageWithTime);

        conversationRepository.save(conversation);

        return conversation;
    }
}
