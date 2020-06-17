package org.vors.march.bot.monolith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vors.march.bot.monolith.service.ConversationService;

import javax.annotation.Resource;
import java.io.File;
import java.util.Optional;

@Component
public class TgBot extends TelegramLongPollingBot {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;
    @Resource
    private ConversationService conversationService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() == null) {
            return;
        }
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if (text != null) {
            conversationService.processText(chatId, text);
        }
        Voice voiceMessage = update.getMessage().getVoice();
        if (voiceMessage != null) {
            Optional<File> fileOpt = loadFromTelegram(voiceMessage.getFileId());
            fileOpt.ifPresent(file -> conversationService.processAudio(chatId, file));
        }
    }

    public void sendMessage(Long chatId, String text) throws TelegramApiException {
        execute(new SendMessage(chatId, text));
    }

    private Optional<File> loadFromTelegram(String fileId) {
        GetFile getFile = new GetFile().setFileId(fileId);

        Optional<File> file;
        try {
            file = Optional.of(downloadFile(execute(getFile)));
        } catch (TelegramApiException e) {
            LOG.error("Voice answer file download failed {}", e.toString(), e);
            file = Optional.empty();
        }
        return file;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void callHandlers(Update update) {
        if (update.getMessage() != null) {
            LOG.debug("Handling update for chat {}", update.getMessage().getChatId());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

