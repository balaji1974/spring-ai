package com.bala.springboot.ai.ai_audio_transcribe.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscriptionController {

	private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscriptionController(@Value("${spring.ai.openai.api-key}") String apiKey) {
        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(apiKey);
        this.transcriptionModel
                = new OpenAiAudioTranscriptionModel(openAiAudioApi);
    }
    
    @CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
    @PostMapping("ai-audio-transcriptor")
    public ResponseEntity<?> transcribeAudio(
    		@RequestParam("language") String lang,
    		@RequestParam("responseFormat") String reponseFormat,
            @RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("audio",".wav");
        file.transferTo(tempFile);
        
        
        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                //.responseFormat(OpenAiAudioApi.TranscriptResponseFormat.valueOf("VTT"))
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.valueOf(reponseFormat))
                .language(lang)
                .temperature(0f)
                .build();

        FileSystemResource audioFile = new FileSystemResource(tempFile);

        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
        AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);

        tempFile.delete();
        return new ResponseEntity<>(response.getResult().getOutput(), HttpStatus.OK);
    }
}
