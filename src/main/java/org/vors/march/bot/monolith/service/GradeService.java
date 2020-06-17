package org.vors.march.bot.monolith.service;

import org.springframework.stereotype.Service;

@Service
public class GradeService {
    public int grade(String answerText) {
        // should call external system: neural network in python
        return 56;
    }
}
