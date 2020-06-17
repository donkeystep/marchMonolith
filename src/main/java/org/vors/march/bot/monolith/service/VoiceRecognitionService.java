package org.vors.march.bot.monolith.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class VoiceRecognitionService {
    public String transcribe(File voice) {
        return "Some transcribed text";
    }
}
