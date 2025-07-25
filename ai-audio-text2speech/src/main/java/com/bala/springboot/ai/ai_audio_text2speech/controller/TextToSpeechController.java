package com.bala.springboot.ai.ai_audio_text2speech.controller;

import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions.Builder;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.TtsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.bala.springboot.ai.ai_audio_text2speech.service.TextToSpeechService;

import reactor.core.publisher.Flux;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TextToSpeechController {
    private final TextToSpeechService textToSpeechService;
    
    @Value("${app.storage.path}")
    private String storagePath;
    
    @Value("${app.audio.filename}")
    private String audioFileName;
    
    private static final Set<String> SUPPORTED_MODELS = 
    		Arrays.stream(OpenAiAudioApi.TtsModel.values())
        	.map(TtsModel::getValue)
        	.collect(Collectors.toSet());
    private static final Set<String> SUPPORTED_VOICE = 
    		Arrays.stream(OpenAiAudioApi.SpeechRequest.Voice.values())
    		.map(OpenAiAudioApi.SpeechRequest.Voice::getValue)
    		.map(String::toUpperCase)
    		.collect(
            Collectors.toSet());
    private static final Set<String> SUPPORTED_RESPONSE_FORMAT =
		    Arrays.stream(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.values())
			.map(OpenAiAudioApi.SpeechRequest.AudioResponseFormat::getValue)
			.map(String::toUpperCase)
			.collect(
		    Collectors.toSet());
    private static final Float SUPPORTED_SPEED_MIN=0.25f; // The acceptable range is from 0.25 (slowest) to 4.0 (fastest)
    private static final Float SUPPORTED_SPEED_MAX=4.0f; // The acceptable range is from 0.25 (slowest) to 4.0 (fastest)
    
    
    
    @Autowired
    public TextToSpeechController(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @GetMapping("/text-to-speech-customized")
    public ResponseEntity<String> generateSpeechForCustomizedParams(@RequestParam("text") String text, @RequestParam Map<String, String> params) throws IOException {
    	OpenAiAudioSpeechOptions speechOptions=buildSpeedOptions(params);
        Optional<OpenAiAudioSpeechOptions> optionalSpeechOptions = Optional.of(speechOptions);
        return saveFile(text, optionalSpeechOptions);
    }

    @GetMapping("/text-to-speech")
    public ResponseEntity<String> generateSpeechForText(@RequestParam("text") String text) {
    	Optional<OpenAiAudioSpeechOptions> optionalSpeechOptions = Optional.empty();
    	return saveFile(text, optionalSpeechOptions);
    }

    @GetMapping(value = "/text-to-speech-stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> streamSpeech(@RequestParam("text") String text) {
        Flux<byte[]> audioStream = textToSpeechService.makeSpeechStream(text);
        
        StreamingResponseBody responseBody = outputStream -> {
            audioStream.toStream().forEach(bytes -> {
                try {
                    outputStream.write(bytes);
                    outputStream.flush();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        };

        return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(responseBody);
    }
    
    @GetMapping(value = "/text-to-speech-stream-customize", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> streamSpeechCustomizedParams(@RequestParam("text") String text, @RequestParam Map<String, String> params) throws IOException {
    	OpenAiAudioSpeechOptions speechOptions=buildSpeedOptions(params);
    	Flux<byte[]> audioStream = textToSpeechService.makeSpeechStream(text, speechOptions);
        
        StreamingResponseBody responseBody = outputStream -> {
            audioStream.toStream().forEach(bytes -> {
                try {
                    outputStream.write(bytes);
                    outputStream.flush();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        };

        return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(responseBody);
    }
    
    private ResponseEntity<String> saveFile(String text, Optional<OpenAiAudioSpeechOptions> optionalSpeechOptions) {
    	byte[]  audioBytes=null;
    	
    	try {
    		if(optionalSpeechOptions.isEmpty())
    			audioBytes=textToSpeechService.makeSpeech(text);
    		else 
    			audioBytes=textToSpeechService.makeSpeech(text,optionalSpeechOptions.get());
    	}
    	catch (Exception e) {
    		return new ResponseEntity<String>("Error while creating the audio file: " +e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	String fileSavePath="";
    	try {
    		fileSavePath=saveMp3ToFile(audioBytes, audioFileName);
		} 
    	catch (IOException e) {
    		return new ResponseEntity<String>("Error occured during file write: " +e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<String>("Audio file created successfully in the path: " +fileSavePath,HttpStatus.OK);
    }
    
    private String saveMp3ToFile(byte[] mp3Content, String fileName) throws IOException {
        // Define target storage directory (can be relative or absolute)
        Path directory = Paths.get(storagePath); 
        
        // Make sure the path exists or create dynamically
        if (!directory.toFile().exists()) {
            directory.toFile().mkdirs();
        }
		
        // Define full path to file
        Path filePath = directory.resolve(fileName);

        // Write byte array to file
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(mp3Content);
        }

        return filePath.toAbsolutePath().toString();
    }
    
    private OpenAiAudioSpeechOptions buildSpeedOptions(Map<String, String> params) {
    	Builder builder=OpenAiAudioSpeechOptions.builder();

    	// Supported model: tts-1, tts-1-hd or gpt-4o-mini-tt
    	if(params.get("model")!=null && SUPPORTED_MODELS.contains(params.get("model").toLowerCase()))
    		builder.model(params.get("model").toLowerCase());
    	
    	// Supported voice: alloy, ash, ballad, coral, echo, fable, onyx, nova, sage, shimmer, and verse
        if(params.get("voice")!=null && SUPPORTED_VOICE.contains(params.get("voice").toUpperCase()))
        	builder.voice(OpenAiAudioApi.SpeechRequest.Voice.valueOf(params.get("voice").toUpperCase()));
        
        // Supported response format: mp3, opus, aac, flac, wav, and pcm
        if(params.get("responseFormat")!=null && SUPPORTED_RESPONSE_FORMAT.contains(params.get("responseFormat").toUpperCase()))
        	builder.responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.valueOf(params.get("responseFormat").toUpperCase()));
        
        // Supported speed: 0.25 to 4.0. Default is 1.0
        if(params.get("speed")!=null && Float.parseFloat(params.get("speed"))>= SUPPORTED_SPEED_MIN 
        		&& Float.parseFloat(params.get("speed"))<= SUPPORTED_SPEED_MAX)
        	builder.speed(Float.parseFloat(params.get("speed")));
        /*
        	If any of the above is not set then the following default parameters will go 
        	into the speech options for each option that is not set
        	Model = tts-1 
    		Speed = 1.0 
    		Voice = alloy
    		Output format = mp3
    	 */
         return builder.build();
    }
}