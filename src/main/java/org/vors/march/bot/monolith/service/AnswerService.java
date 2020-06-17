package org.vors.march.bot.monolith.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class AnswerService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Resource
    private VoiceRecognitionService voiceRecognitionService;
    @Resource
    private GradeService gradeService;
    @Resource
    private ConversationService conversationService;

    public void processVoiceAnswer(Long chatId, File voiceRecord) {
        String answerText = voiceRecognitionService.transcribe(voiceRecord);
        int grade = gradeService.grade(answerText);
        conversationService.sendMessage(chatId, "Your grade is " + grade);
    }
}
