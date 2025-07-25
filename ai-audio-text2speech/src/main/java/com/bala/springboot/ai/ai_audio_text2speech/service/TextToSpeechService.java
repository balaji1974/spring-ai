package com.bala.springboot.ai.ai_audio_text2speech.service;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.audio.speech.Speech;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TextToSpeechService{

    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @Autowired
    public TextToSpeechService(OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    public byte[] makeSpeech(String text) throws Exception{
    	// Call OpenAiAudioSpeechModel with default OpenAiAudioSpeechOptions
    	// By using defaults we have the following config set for OpenAiAudioSpeechOptions in Spring AI version 1.0
    	// Model = tts-1 
    	// Speed = 1.0 
    	// Voice = alloy
    	// Output format = mp3
    	return openAiAudioSpeechModel.call(text);
    }

    public byte[] makeSpeech(String text, OpenAiAudioSpeechOptions speechOptions) throws Exception{
    	SpeechPrompt speechPrompt = new SpeechPrompt(text, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        return response.getResult().getOutput();
    }

    public Flux<byte[]> makeSpeechStream(String text) {
    	// Call OpenAiAudioSpeechModel with default OpenAiAudioSpeechOptions
    	// By using defaults we have the following config set for OpenAiAudioSpeechOptions in Spring AI version 1.0
    	// Model = tts-1 
    	// Speed = 1.0 
    	// Voice = alloy
    	// Output format = mp3
        SpeechPrompt speechPrompt = new SpeechPrompt(text);
        Flux<SpeechResponse> responseStream = openAiAudioSpeechModel.stream(speechPrompt);

        return responseStream
          .map(SpeechResponse::getResult)
          .map(Speech::getOutput);
    }
    
    public Flux<byte[]> makeSpeechStream(String text, OpenAiAudioSpeechOptions speechOptions) {
        SpeechPrompt speechPrompt = new SpeechPrompt(text, speechOptions);
        Flux<SpeechResponse> responseStream = openAiAudioSpeechModel.stream(speechPrompt);

        return responseStream
          .map(SpeechResponse::getResult)
          .map(Speech::getOutput);
    }
}