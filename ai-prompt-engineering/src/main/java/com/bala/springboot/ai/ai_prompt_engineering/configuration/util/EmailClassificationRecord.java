package com.bala.springboot.ai.ai_prompt_engineering.configuration.util;

public record EmailClassificationRecord(Classification classification, String reasoning) {
    public enum Classification {
        IMPORTANT, NOT_IMPORTANT
    }
}
