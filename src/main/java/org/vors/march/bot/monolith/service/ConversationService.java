package org.vors.march.bot.monolith.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vors.march.bot.monolith.TgBot;

import javax.annotation.Resource;
import java.io.File;


@Service
public class ConversationService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    @Lazy
    TgBot bot;
    @Resource
    private AnswerService answerService;

    public void processText(Long chatId, String text) {
        sendMessage(chatId, "Do we have test for that?");
    }

    public void processAudio(Long chatId, File voiceRecord) {
        answerService.processVoiceAnswer(chatId, voiceRecord);
    }

    public boolean sendMessage(Long chatId, String text) {
        try {
            bot.sendMessage(chatId, text);
        } catch (TelegramApiException e) {
            LOG.error("Can't send TG message", e);
            return false;
        }
        return true;
    }
}
