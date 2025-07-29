
# Spring AI

## AI - Concepts

```xml 
Models: 
------
AI models are algorithms designed to process and 
generate information, often mimicking human cognitive functions. 
By learning patterns and insights from large datasets, 
these models can make predictions, text, images, 
or other outputs, enhancing various applications across industries.
Eg. GPT-4o


Prompts: 
-------
Prompts serve as the foundation for the language-based inputs that 
guide an AI model to produce specific outputs. 
For those familiar with ChatGPT, a prompt might seem like merely 
the text entered into a dialog box that is sent to the API. 
However, it encompasses much more than that. 
In many AI Models, the text for the prompt is not just a simple string.


Prompt Templates: 
----------------
Creating effective prompts involves establishing the context of the 
request and substituting parts of the request with values 
specific to the user’s input.
For instance, consider the simple prompt template:
Tell me a {adjective} joke about {content}.


Tokens: 
------
Tokens serve as the building blocks of how an AI model works. 
On input, models convert words to tokens. 
On output, they convert tokens back to words.


Building blocks for LLM:
-----------------------
Retrieval (Query Result)
Tool (Call response)
Memory (Read / Write)


LLM Workflows: 
--------------

Prompt Chaining:
It decomposes tasks into sequence of steps where each call 
processes the output of the previous one. We can add programatic 
check (gate) on any intermediate steps to ensure that the process 
is still on track. 

Routing: 
Routing classifies the input and directs it to a specialized 
followup task. This workflow allows for speration of concerns 
and building more specialized prompts. Without this workflow 
optimizing on one kind of input can hurt performance of other
inputs. 

Parallelization:
LLM can sometime work simulataneously on task and have their 
output aggregated programmatically.
Two parts of parallelization are: 
Sectioning: Break task into independent subtask and run it in parallel
Voting: Running the same task multiple times to get diverse output

Orchestator Worker: 
A central LLM dynamically breaks down tasks, delegates them to 
worker LLMs and synthesizes their results 

Evaluator Optimizer:
One LLM call generates a respone, while another provides evaluation
and feeback in a loop.


Reference: 
https://docs.spring.io/spring-ai/reference/concepts.html
https://www.youtube.com/watch?v=tx5OapbK-8A

```

## Setup - OpenAI 
```xml 
OpenAI
------
1. Create an account on OpenAI.
2. Generate the token on the API Keys page
https://platform.openai.com/api-keys

Copy the token to be added later in the spring application properties or yaml file 

Note: Free account wiil not work and so I topped up my account for $5 
before generating my API key. 
If you generated an API key before, please generate another one after 
topping up your account and use that in your application. 

Spring Initilizer
-----------------
1. Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI

2. create a project and download

3. The pom.xml will have the following dependencies: 
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>

4. Application properties
-------------------------
In the application.properties add the following:
spring.ai.openai.api-key=${OPEN_API_KEY}
spring.image.options.model=dall-e-3
spring.image.options.size=1024x1024
spring.image.options.style=vivid
spring.image.options.quality=standard
spring.image.options.response-format=url

or 

in application.yaml file add the following:
spring:
  ai:
    openai:
      api-key: [your OpenAI api key]
    image:
      options:
        model: dall-e-3
        size: 1024x1024
        style: vivid
        quality: standard
		response-format: url

Check here for the list of parameters available:
https://docs.spring.io/spring-ai/reference/api/image/openai-image.html

Current AI provider support by Spring AI: 
https://docs.spring.io/spring-ai/reference/api/chat/comparison.html

```

## OpenAI Image generation (ai-image-generator)
```xml

@Service:
ImageModel is the main interface used for generating images 
and it is called in the service layer

@RestController():
This opens up an URL http://localhost:8080/image/generate
to which you can submit your image generation prompt 

Run the service and call the url like this example below: 
Image Generator (Sample 1 - with default Parameters):
curl --location 'http://localhost:8080/image/generate?prompt=Mountain%20background%20for%20PC'

Image Generator (Sample 2 - with Parameters):
curl --location 'http://localhost:8080/image/generate?prompt=Harry%20Potter%20in%20India&modelName=dall-e-3&noOfImages=1&imageHeight=1024&imageWidth=1024'

You will receive a response as link which can be click and send to server 
to check the generated image or simply copy and run it on a browser

Full details of this example can be found here:
https://dzone.com/articles/spring-ai-generate-images-openai-dalle?edition=958905

More details about spring ai can be found here:
https://docs.spring.io/spring-ai/reference/

```

## OpenAI Chatbot (ai-chat-assistant)
```xml
An Open AI chat assistant

@RestController() -> This opens up an URL http://localhost:8080/chat/query
to which you can submit your chating prompt 

Run the service and call the url like this example below: 
Open AI Chat Assistant:
curl --location 'http://localhost:8080/chat/query?userInput=What%20is%20the%20best%20AI%20tool%20available%20for%20creating%20programming%20code%20in%20Java'

and you will receive a response which can be displayed on the screen 

Full details of this example can be found here:
https://docs.spring.io/spring-ai/reference/api/chatclient.html

Open AI chat assistant with formatted output:
@RestController() -> This opens up an URL http://localhost:8080/chat/query-formatted
to which you can submit your chating prompt 

There is also an overloaded entity method with the signature 
entity(ParameterizedTypeReference<T> type) that lets you specify types 
such as generic Lists:
@GetMapping("/chat/query-formatted")
List<Article> askQuestion(@RequestParam(name = "question") String question) {
    return chatClient.prompt()
      .user(question)
      .call()
      .entity(new ParameterizedTypeReference<List<Article>>() {});
}

Article model consist of the following structure: 
 public class Article {
  
  private String name;
  private String category;
  private String fullDescription;

}

Note: This is how my output will be formatted

Run the service and call the url like this example below: 
Open AI Chat Assistant - Formatted - (Top 20 AI Models):
curl --location 'http://localhost:8080/chat/query-formatted?question=What%20is%20the%20top%205%20language%20model%20for%20AI'

Open AI Chat Assistant - Formatted - (Top 10 Actors - Hollywood):
curl --location 'http://localhost:8080/chat/query-formatted?question=Who%20are%20the%20top%2010%20actors%20in%20the%20world'

and you will receive a formatted response which can be displayed on the screen 

Full details of this example can be found here:
https://spring.io/blog/2024/05/09/spring-ai-structured-output
https://docs.spring.io/spring-ai/reference/api/chatclient.html

```

## OpenAI Chatbot - Step by Step (ai-prompt-assistant)
```xml

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI

2. Create a project 'ai-prompt-assistant' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>

4. Application properties
In the application.properties add the following:
spring.ai.openai.api-key=${OPEN_API_KEY}

5. The fundametal component of OpenAI chat is ChatModel class.
Create a @Service class called AskAIChatService and autowire ChatModel

@Autowired
ChatModel chatModel;

6. Input the prompt to this model and return the response
public String getResponse(String prompt) {
    return chatModel.call(prompt);
}

7. Create a Rest controller called AskAIController 
and autowire this service
@Autowired
AskAIChatService askAIChatService;

8. Add a GetMapping to this controller and pass the request parameter 
named prompt to the askAIChatService and return the response 
@GetMapping("ask-ai")
public String getRespone(@RequestParam String prompt) {
  return askAIChatService.getResponse(prompt);
}

9. Start the application and execute the service from a browser 
or postman or from command line using curl 

Simple Question & Answers:
curl --location 'http://localhost:8080/ask-ai?prompt=What%20is%20the%20top%20rated%20tools%20to%20learn%20and%20format%20the%20output%20as%20a%20json'

```

## OpenAI Chatbot - Customizing the ChatModel (ai-prompt-assistant)
```xml

This demonstates the dynamic input parameter that can be sent during 
chat coverstations. In our example we send Model and Temprature as 
dynamic paramters.

Full list could be found in the below link. 
https://platform.openai.com/docs/api-reference/chat/create

10. Create a new method called getResponseOptions and 
customize the chatModel with the dynamic input parameters. 

public String getChatResponseWithOptions(String prompt, String model, Double temprature) {
    ChatResponse response = chatModel.call(
          new Prompt(
              prompt,
              OpenAiChatOptions.builder()
                  .model(model)
                  .temperature(temprature)
              .build()
          ));
    return response.getResult().getOutput().getContent();
  }

11. Add a GetMapping to the controller and pass the request parameter 
named prompt to the askAIChatService's getResponseOptions method 
and return the response: 

@GetMapping("ask-ai-options")
public String getResponeOptions(@RequestParam String prompt,
    @RequestParam(defaultValue ="gpt-4o") String model,
    @RequestParam(defaultValue = "0.4D") Double temprature) {
  return askAIChatService.getChatResponseWithOptions(prompt,model,temprature);
}

12. Start the application and execute the service from a browser 
or postman or from command line using curl

Simple Question & Answers With Options:
curl --location 'http://localhost:8080/ask-ai-options?prompt=What%20is%20the%20best%20programming%20language%20to%20learn%20today%3F&model=gpt-4o&temperature=0.5D'

13. Note:
How Temperature Works:

Range:
Temperature values typically range from 0 to 1, although some models may support higher values.

Lower Temperature (e.g., closer to 0):
This makes the model's output more deterministic and focused. The model is more likely to choose 
the most probable next word or token, resulting in less variability and more predictable responses. 
This is suitable for tasks requiring factual accuracy or consistent output.

Higher Temperature (e.g., closer to 1):
This increases the randomness and creativity of the model's output. The model is more likely to 
explore less probable but still plausible options, leading to more diverse and potentially surprising 
responses. This is useful for creative writing, brainstorming, or open-ended conversations. 

```


## OpenAI Chatbot - Formatting the output of the ChatModel (ai-prompt-assistant)
```xml
14. Create a record that will carry the formatted output
public record MathReasoning(
      @JsonProperty(required = true, value = "steps") Steps steps,
      @JsonProperty(required = true, value = "final_answer") String finalAnswer) {

  record Steps(
      @JsonProperty(required = true, value = "items") Items[] items) {

    record Items(
        @JsonProperty(required = true, value = "explanation") String explanation,
        @JsonProperty(required = true, value = "output") String output) {}
  }
}

15. Add method for getting the formatted output:
public MathReasoning getMathResponseFormatted(String question, String model, Double temprature) {
  var outputConverter = new BeanOutputConverter<>(MathReasoning.class);

  var jsonSchema = outputConverter.getJsonSchema();

  Prompt prompt = new Prompt(question,
    OpenAiChatOptions.builder()
        .model(MODEL)
        .temperature(TEMPRATURE)
        .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
        .build());

  ChatResponse response = chatModel.call(prompt);
  String content = response.getResult().getOutput().getContent();

  MathReasoning mathReasoning = outputConverter.convert(content);
  return mathReasoning;
}

The responseFormat is set in the new Prompt method chaining and result is 
converted in the outputConverter
Check the following links for more information: 
https://spring.io/blog/2024/08/09/spring-ai-embraces-openais-structured-outputs-enhancing-json-response
https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html#_bean_output_converter

16. Add a GetMapping to the controller and pass the request parameter 
named prompt to the askAIChatService's getResponeMath method 
and return the response: 

@GetMapping(value="ask-ai-math",produces = MediaType.APPLICATION_JSON_VALUE)
public MathReasoning getResponeMath(
    @RequestParam String prompt,
    @RequestParam(defaultValue ="gpt-4o") String model,
    @RequestParam(defaultValue = "0.4D") Double temprature) {
  return askAIChatService.getMathResponseFormatted(prompt, model, temprature);
}

17. Start the application and execute the service from a browser 
or postman or from command line using curl 

Math Question With Options And Formatted Json Response:
curl --location 'http://localhost:8080/ask-ai-math?prompt=how%20can%20I%20solve%208x%20+%207%20%3D%20-23&model=gpt-4o&temperature=0.4D'


```

## OpenAI Generating Image From Prompt - Step by Step (ai-prompt-assistant)
```xml
18. To generate an image from prompt lets create a new service 
called AskAIImageService 

@Service
public class AskAIImageService {

}

19. We will inject the OpenAI Image Model into this service 
by autowiring

@Autowired
OpenAiImageModel openAiImageModel;

20. Next we will create an image response 
by setting a few image options like height, width etc. 
public ImageResponse generateImage(String prompt) {
  ImageResponse response = openAiImageModel.call(
          new ImagePrompt(prompt,
          OpenAiImageOptions.builder()
                  .quality("hd")
                  .N(1) // default is dall-e-3 model which support max 1 image generation per request
                  .height(1024)
                  .width(1024).build())

  );
  return response;
}

21. Finally add a controller method in our AskAIController class 
which will build an url from the image response object 
and redirect us to this url. 

@GetMapping("ask-ai-image-generate")
public void generateImage(HttpServletResponse httpServletResponse, @RequestParam String prompt) throws IOException {
  ImageResponse imageResponse= askAIImageService.generateImage(prompt);
  String imageURL = imageResponse.getResult().getOutput().getUrl();

  // Use this in case you need multiple images from the response 
  // Supported in dall-e-2
  /*
  List<String> imageURLs=imageResponse.getResults().stream()
      .map(result-> result.getOutput().getUrl())
      .toList();
  
  */

  httpServletResponse.sendRedirect(imageURL);
}


22. Run the curl commmand as below:
Generate Image:
curl --location 'http://localhost:8080/ask-ai-image-generate?prompt=James%20Bond%20with%20a%20gun%20in%20Russia'

Click the link to see the generated image

```


## OpenAI - Generating Results from Template - Step by Step (ai-prompt-assistant)
```xml
23. Lets create a template for recipie with ingredients, cuisine and dietary restriction choices
Create a service (AskAIRecipeService) and autowire the ChatModel 

24. Create a template 
var template ="""
    I want to create a recipie using the following ingredients: {ingredients}.
    The cuisine type I prefer is: {cuisine}.
    Please consider the following dietaryRestrictions: {dietaryRestrictions}.
    Please provide me a detail recipe including title, list of ingredients, and cooking instructions
  """;

25. Create a PromptTempalate object and inject the template and params into it. 
PromptTemplate promptTemplate=new PromptTemplate(template);
Map<String, Object> params=Map.of(
    "ingredients", ingredients,
    "cuisine", cuisine,
    "dietaryRestrictions", dietaryRestrictions
    );

26. Finally create a prompt and chat model out of it. 
Prompt prompt=promptTemplate.create(params);
return chatModel.call(prompt).getResult().getOutput().getContent();
 

19. We will inject the OpenAI Chat Model into the controller service 
by autowiring
@Autowired
AskAIRecipeService askAIIRecipeService;

27. Add a GetMapping to the controller and pass the request parameter 
named ingredients, cuisine and dietaryRestrictions  to the askAIIRecipeService's 
getResponeRecipe method and return the response: 

@GetMapping("ask-ai-recipe-creator")
public String getResponeRecipe(@RequestParam String ingredients, 
    @RequestParam(defaultValue = "any") String cuisine, 
    @RequestParam(defaultValue = "") String dietaryRestrictions) {
  return askAIIRecipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
}

28. Start the application and execute the service from 
postman or from command line using curl 

RecipeCreator - Create Template Output: 
curl --location 'http://localhost:8080/ask-ai-recipe-creator?ingredients=Chicken%2C%20Curry%20Masala%2C%20Tomotos%2C%20Onions%2C%20Oil&cuisine=Indian%27&dietaryRestrictions=No%20nuts%20and%20sweetner'

```

## OpenAI - Audio Transcription model - Step by Step (ai-audio-transcribe)
```xml
This program converts an Mp3 file to a text file and translates the file
to any of the supported languages

Languages as per ISO_639 language codes
https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes

Open AI Support Languages:
https://platform.openai.com/docs/guides/speech-to-text/supported-languages

1. Add the Spring AI dependency. 
Spring Web
Open AI
Dev Tools

2. In the application.properties file add the following: 
spring.ai.openai.api-key=<your api key>
spring.ai.openai.audio.transcription.base-url=https://api.openai.com
spring.ai.openai.audio.transcription.options.model=whisper-1
spring.ai.openai.audio.transcription.options.response-format=json

3. Create a controller 'TranscriptionController' and inject the following:
private final OpenAiAudioTranscriptionModel transcriptionModel;

public TranscriptionController(@Value("${spring.ai.openai.api-key}") String apiKey) {
    OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(apiKey);
    this.transcriptionModel
            = new OpenAiAudioTranscriptionModel(openAiAudioApi);
}

4. Create a ResponseEntity as follows:

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@PostMapping("ai-audio-transcriptor")
public ResponseEntity<String> transcribeAudio(
        @RequestParam("file") MultipartFile file) throws IOException {
    File tempFile = File.createTempFile("audio",".wav");
    file.transferTo(tempFile);

    
    OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
            .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
            .language("en")
            .temperature(0f)
            .build();

    FileSystemResource audioFile = new FileSystemResource(tempFile);

    AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
    AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);

    tempFile.delete();
    return new ResponseEntity<>(response.getResult().getOutput(), HttpStatus.OK);
}

5. Run the application. 
To run the application once you import the Postman curl commmand below, 
go to Body -> Form data -> and in re-import the hello.mp3 file once 
from your local machine before you can execute the below curl command 
or fix the <your-file-location> to the correct location of your mp3 file 
before importing the curl into Postman. 

6. Run the following from command line or postman:
Audio To Text - French - Response(JSON):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="fr"' \
--form 'responseFormat="JSON"'

Audio To Text - French - Response(VTT):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="fr"' \
--form 'responseFormat="VTT"'

Audio To Text - Tamil - Response(JSON):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="ta"' \
--form 'responseFormat="JSON"'

Audio To Text - Tamil - Response(VTT):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="ta"' \
--form 'responseFormat="VTT"'

Audio To Text - Arabic - Response(JSON):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="ar"' \
--form 'responseFormat="JSON"'

Audio To Text - Arabic - Response(VTT):
curl --location 'http://localhost:8080/ai-audio-transcriptor' \
--form 'file=@"<your-file-location>/hello.mp3"' \
--form 'language="ar"' \
--form 'responseFormat="VTT"'

Note: I created the hello.mp3 from the following website: https://ttsmp3.com/

```

## OpenAI - Text to Speech - Step by Step (ai-audio-text2speech)
```xml
This program converts input text to an audio file using Open AI libraries 

1. Add the Spring AI dependency. 
Spring Web
Open AI
Dev Tools

2. In the application.properties file add the following: 
spring.ai.openai.api-key=<your api key>

3. Create a Service 'TextToSpeechService'
We use OpenAiAudioSpeechModel, which Spring AI preconfigures using our properties. 
We also define the makeSpeech() method to convert text into audio file bytes 
with OpenAiAudioSpeechModel.
We configure 3 different formats of makeSpeech 
makeSpeech, makeSpeech with Options and makeSpeechStream

4. Create a controller 'TextToSpeechController' and inject the following:
private final TextToSpeechService textToSpeechService;

@Autowired
public TextToSpeechController(TextToSpeechService textToSpeechService) {
    this.textToSpeechService = textToSpeechService;
}

5. Create three different ResponseEntity as follows:
@GetMapping("/text-to-speech-customized")
public ResponseEntity<byte[]> generateSpeechForTextCustomized(@RequestParam("text") String text, @RequestParam Map<String, String> params) {
    OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
      .model(params.get("model"))
      .voice(OpenAiAudioApi.SpeechRequest.Voice.valueOf(params.get("voice")))
      .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.valueOf(params.get("responseFormat")))
      .speed(Float.parseFloat(params.get("speed")))
      .build();

    return ResponseEntity.ok(textToSpeechService.makeSpeech(text, speechOptions));
}

@GetMapping("/text-to-speech")
public ResponseEntity<byte[]> generateSpeechForText(@RequestParam("text") String text) {
    return ResponseEntity.ok(textToSpeechService.makeSpeech(text));
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

Note: Check the actual code, which is refactored to handle errors. 

5. Run the application. 

6. Run the following from command line or postman:

Test2Speech With Static Parameters:
curl --location 'http://localhost:8080/text-to-speech?text=This%20is%20balaji%20saying%20hello%20to%20all'

Text2Speech With Dynamic Parameters:
curl --location 'http://localhost:8080/text-to-speech-customized?text=Hello%20from%20Balaji&model=tts-1&voice=coral&responseFormat=MP3&speed=1.0'

Text2Speech Streams With Static Parameters:
curl --location 'http://localhost:8080/text-to-speech-stream?text=This%20is%20a%20nice%20test%20to%20demonstrate%20text%20to%20speech%20using%20streams%20and%20default%20speech%20parameters'

Text2Speech Streams With Dynamic Parameters:
curl --location 'http://localhost:8080/text-to-speech-stream-customize?text=This%20is%20a%20nice%20test%20to%20demonstrate%20text%20to%20speech%20using%20streams%20and%20dynamic%20speech%20parameters&model=tts-1&voice=shimmer&responseFormat=MP3&speed=1.0'

Note: For curl response 3 and 4, I saved the response into an mp3 file from postman and later played it. 
In real senarios this can be streamed on to a browser directly and played using javascript libraries.

```


## OpenAI RAG - Retrieval Augmented Generation (ai-rag-textreader)
```xml
# Retrieve: When you ask a question, the system first searches your specific knowledge base 
(documents, databases, etc.) for information relevant to your query.

# Augment: This retrieved information (the “context”) is then added to your original question 
and sent as a more detailed prompt to the LLM.

# Generate: The LLM uses both your question and the provided context to 
generate an informed answer.

# Vector Database: 
A vector database is a collection of data stored as mathematical representations. 
Vector databases make it easier for machine learning models to remember previous inputs, 
allowing machine learning to be used to power search, recommendations, 
and text generation use-cases. Data can be identified based on similarity metrics instead 
of exact matches, making it possible for a computer model to understand data contextually.

1. Add the Spring OpenAI dependency. 

2. In the application.properties file add the following: 
spring.ai.openai.api-key=<your api key>

3. To generate an RAG response from prompt lets create a new service 
called RagConfiguration 

@Configuration
public class RagConfiguration {

}

4. Creat a method called simpleVectorStore which will 
read a text file from a path containing the faq and convert 
it back to a vector file. If the file already exist, then it 
will not create the vector file again. 


5. Create a controller 'RagController' which will read the 
message from the user and also read the Vector file, stuff everything 
into a template, and create a Prompt before sending it to OpenAI 
to get the output against the Vector content provided 


6. Run the application. 

7. Run the following from command line or postman:
curl --location 'http://localhost:8080/faq?message=how%20many%20sports%20%20does%20Olympics%20have'


```


## Running a Machine Learning model locally - Ollama (ai-rag-ollama-pdfreader)
```xml 

1. Download ollama and install it according to your OS version
https://ollama.com/download

Once installed run the following commands to check: 
ollama --version
ollama ls => This command will list all the models that are currently installed locally 

Supported models and the command to install them are given in the below link:
https://github.com/ollama/ollama
Example command to download and install one of the models is:
ollama run llama3.3

2. Run Olloma from the PC after installation (installed under applications in MAC)
Check at http://localhost:11434/ if it is running fine

3. Create a spring project with following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
</dependency>

4. Add one more dependency 
PDF Document reader and implementation of Apache PDFBox® - A Java PDF Library
The Apache PDFBox® library is an open source Java tool for working 
with PDF documents. This project allows creation of new PDF documents, 
manipulation of existing documents and the ability to 
extract content from documents.

<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-pdf-document-reader</artifactId>
</dependency>

5. Add the following in application.properties file:

spring.ai.ollama.chat.options.model=llama3.2:latest
spring.ai.ollama.embedding.enabled=true
spring.ai.ollama.embedding.options.model=llama3.2:latest

file.path=classpath:data/sample.pdf

The file path is where the sample pdf file resides which will be read and 
convered into an in memory vector database 


6. Create 2 model files for request and response: 
ChatResponse.java
QueryRequest.java

7. Create a configuration file called VectorBuilder.java 
This file will read the PDF file in the <classpath>/data folder and 
will convert the content to be stored in a VectorStore (database)

8. Finally create a PDFController file which will inject the 
VectorStore and ChatClient
Use the VectorStore to find a similarity search and convert 
the respone into the ChatResponse object before returning it 

9. Run the application. 

10. Run the following from command line or postman:
curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data '{
    "query": "Who has the highest salary?"
}'

```

## OpenAI RAG - Vector Database (PGVector) (ai-rag-pgvector)
```xml 
1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
Spring JDBC
OpenAI
PGVector
Lombok
DevTools and
Docker Compose
PDF Document Reader


2. Create a project 'ai-rag-pgvector' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-vector-store-pgvector</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-pdf-document-reader</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-docker-compose</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-spring-boot-docker-compose</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>


4. Their is a docker compose.yaml file.
Modifiy it as below to add pgadmin, adding db user and password

services:
  pgvector:
    image: 'pgvector/pgvector:pg16'
    environment:
      - 'POSTGRES_DB=chat-engine-db'
      - 'POSTGRES_PASSWORD=secret'
      - 'POSTGRES_USER=chat-engine-user'
    labels:
      - "org.springframework.boot.service-connection=postgres"
    ports:
      - '5432:5432'
  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@example.com
      PGADMIN_DEFAULT_PASSWORD: adminpassword
    ports:
      - "8081:80"
    depends_on:
      - pgvector 

5. Go to command promt and run 
docker compose up 

All the depenedencies for pgvector and pgadmin gets pulled 
and the container starts running

6. Verify the postgres admin panel  
http://localhost:8081/browser/
Enter User ID: admin@example.com
Enter Password: adminpassword

7. Add a new server in the admin browser 
Add new server -> General tab 
Name: localhost

Connection tab 
Host name / address: pgvector
User Name: chat-engine-user
Password: secret

Save and check if the server is added & connected 

8. Using Spring ETL pipeline for Extracting, Transforming and Loading PDF document
https://docs.spring.io/spring-ai/reference/1.0/api/etl-pipeline.html

The VectorBuilder.java inside the config folder reads data from the resources/static 
folder, in our case NVIDIAAn.pdf file. Note: mulitple PDF files can be placed in this 
folder and it will all be extracted pagewise, content split and stored in the 
pgvector database.

9. Add configuration in applications.properties file
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}
# needed for creating the schema automatically in pgvector store 
spring.ai.vectorstore.pgvector.initialize-schema=true
# the storage location of the pdf content file that needs to be read 
documents.directory.path=classpath:static

10. Add a controller file that accepts a message, 
finds the similarity search using the vector database, 
and sends fetched document and the message to the ChatGPT engine 
to return the response. 

Note: the below code uses an in-memory chat advisor to store the state of the previous queries

Check the below part in the constructor: 
chatClientBuilder
  .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
  .build();

And the ChatMessage method for details 
in the ChatController.java file. 

11. Run the application 
Run the application and excute a curl command to see the respone:
curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data '{
    "chatId": "2216b193-d85f-4f86-b8cb-26a22bf05652",
    "message": "Give me its revenue growth?"
}'

If no chatId parameter is sent, the program generates a random UUID 

```

## Vector Database - Similarity Search (ai-vector-db)
```xml 
1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
Spring JDBC
OpenAI
PGVector
DevTools and
Docker Compose


2. Create a project 'ai-vector-db' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-vector-store-pgvector</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-docker-compose</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-spring-boot-docker-compose</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>


4. Their is a docker compose.yaml file.
Modifiy it to set db user, password and exposed port, in our case 5432
services:
  pgvector:
    image: 'pgvector/pgvector:pg16'
    environment:
      - 'POSTGRES_DB=mydatabase'
      - 'POSTGRES_PASSWORD=secret'
      - 'POSTGRES_USER=myuser'
    labels:
      - "org.springframework.boot.service-connection=postgres"
    ports:
      - '5432:5432'

5. Go to command promt and run 
docker compose up 

All the depenedencies for pgvector gets pulled 
and the container starts running

6. Import the sql dump data that is needed for this project
into the our database  
docker exec -i <our running container name> psql -U myuser -d mydatabase < <path to our sql file>/titanic.sql

7. Login to our database and check if passenger table is created and check the record count.
Check and verify if 1309 records are imported. 

7. Read the data from 
https://docs.spring.io/spring-ai/reference/1.0/api/etl-pipeline.html

The VectorBuilder.java inside the config folder reads data from the our imported table 
(only once if created) and imports the data into the vector_store table. 

9. Add configuration in applications.properties file
# To start the docker container for pgvector only once
spring.docker.compose.lifecycle-management=start_only
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}
# needed for creating the schema automatically in pgvector store 
spring.ai.vectorstore.pgvector.initialize-schema=true


10. Modify the prompt and run the application
In the VectorBuilder.java we have a variable called 
PROMPT_STRING which can be modified to fetch query results. 

Note: to query we can get the passenger name, 
if the passenger died or alive, 
the passenger final destination city, country 
and if the passenger is male or female. 

I have inserted only these 4 parameters from our original passenger table
into the vector database but please note that the possibilies are limitless

```


## Text to SQL - Static DDL  (ai-text2sql)
```xml

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI
DevTools
Note: No database required here, as we give a static DDL statement to AI 
which generates a query based on this. 

2. Create a project 'ai-text2sql' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>


4. Add configuration in applications.properties file
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}
# set logging level for spring ai to debug  
logging.level.org.springframework.ai=debug

5. Create Ruquest and Response objects
Create 2 record objects 
TextToSQLRequest and 
TextToSQLResponse
to warp up the request and response object 

6. Add a controller file that accepts a message, 
wraps a the incoming query with the user prompt template,
which also contains the DDL based on which AI must return SQL response  
and sends it to the ChatGPT engine to return the response. 

Check the below part in the TextToSqlController.java controller file: 
private static final String USER_PROMPT_TEMPLATE = """
      You are a Postgres expert. Please generate SQL statements to answer user's query. 
      
      The table name is netflix_shows. Column names and data types are shown as below: 
      show_id, text; type, text; title, text; director, text; cast_members, text; country, text; 
      date_added, date; release_year, int4; rating, text; duration, text; 
      listed_in, text; description, text.
      
      Output the SQL only. Don't use Markdown format and output the query in a single line.
      
      {user_input}
      """;
This is where we send DDL and the user_input to ChatGPT model that then generates the SQL.  

Check the textToSql method for details in the TextToSqlController.java file. 

11. Run the application 
Run the application and excute a curl command to see the respone:
curl --location 'http://localhost:8080/texttosql' \
--header 'Content-Type: application/json' \
--data '{
    "input": "How many shows were produced in 2020?"
}'

This will result in a response containing the SQL query like below:
{
    "sql": "SELECT COUNT(*) FROM netflix_shows WHERE release_year = 2020;"
}

Verify it with the netflix.sql file to check the correctness 
(this could be inserted into a live database and checked for results)

The command to do this would be:
docker pull postgres
docker run --name ai-text2sql -e POSTGRES_PASSWORD=secret -e POSTGRES_USER=myuser -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres   
docker exec -i <our running container name> psql -U myuser -d mydatabase < <path to our sql file>/netflix.sql

```

## Text To SQL - Dynamic DDL (ai-text2sql-dynamic)
### (we have it stored in a file schema.sql which is fed to AI engine)
```xml 
1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
Spring JDBC
OpenAI
DevTools and
Postgres

2. Create a project 'ai-text2sql-dynamic' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>


4. Install postgres using docker image and start the container. 
docker pull postgres
docker run --name ai-text2sql-dynamic -e POSTGRES_PASSWORD=secret -e POSTGRES_USER=myuser -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres

5. The schema and data are stored in 
schema.sql
data.sql

6. Add configuration in applications.properties file
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}
# this ensures that schema and data files are read and created while running the application
spring.sql.init.mode=always
# below 3 lines are database connection parameters
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret

7. Run the program once to make sure that schema and data are created in the database

8. The below 3 files are used to warp Exceptions, Request, Response and a Custom Exception Handler. 
AiException.java
AiRequest.java
AiResponse.java
CustomExceptionHandler.java

9. Add a controller file that accepts a message, 
wraps a the incoming query with the user prompt template stored in sql-prompt-template.st file,
which also contains has the DDL attached with it based on which AI must return SQL response  
and sends it to the ChatGPT engine to return the response. 

Check the below part in the SqlController.java controller file 
Check the two methods, sql & sql-dynamic method for details. 

In the sql method we read a static ddl that is stored in the classpath 
which we feed into the AI engine.

In the sql-dynamic method we build our own ddl based on database and table
metadata, then convert into into a json format which we feed into the AI engine.

10. Run the application in 2 diffent way based on static & dynamic metadata fetching
From static DDL statements: 
---------------------------
Run the application and excute a curl command to see the respone:
curl --location 'http://localhost:8080/sql' \
--header 'Content-Type: application/json' \
--data '{
    "text": "Find the account with maximum balance."
}'

This will result in a response containing the SQL query and its response like below:
{
    "sqlQuery": "select * from TBL_ACCOUNT where balance = (select max(balance) from TBL_ACCOUNT);",
    "results": [
        {
            "id": 6,
            "accountnumber": "ACC006",
            "user_id": 4,
            "balance": 3000.00,
            "opendate": "2024-07-09"
        }
    ]
}
Verify it with the database to check the correctness 

From dynamic DDL statements: 
---------------------------
Run the application and excute a curl command to see the respone:
curl --location 'http://localhost:8080/sql-dynamic' \
--header 'Content-Type: application/json' \
--data '{
    "text": "Find the account with maximum balance."
}'

This will result in a response containing the SQL query and its response like below:
{
    "sqlQuery": "select * from TBL_ACCOUNT where balance = (select max(balance) from TBL_ACCOUNT);",
    "results": [
        {
            "id": 6,
            "accountnumber": "ACC006",
            "user_id": 4,
            "balance": 3000.00,
            "opendate": "2024-07-09"
        }
    ]
}
Verify it with the database to check the correctness 

```

## Inject Custom Functions (Tools) - (ai-inject-custom-function)
```xml 
Tool calling (also known as function calling - now deprecated) is a common pattern in 
AI applications allowing a model to interact with a set of APIs, or tools, 
augmenting its capabilities.

Tools are mainly used for:

Information Retrieval: Tools in this category can be used to retrieve information from 
external sources, such as a database, a web service, a file system, or a web search engine. 
The goal is to augment the knowledge of the model, allowing it to answer questions that it 
would not be able to answer otherwise. 

As such, they can be used in Retrieval Augmented Generation (RAG) scenarios. 
For example, a tool can be used to retrieve the current weather for a given location, 
to retrieve the latest news articles, or to query a database for a specific record.

Taking Action: Tools in this category can be used to take action in a software system, 
such as sending an email, creating a new record in a database, submitting a form, 
or triggering a workflow. The goal is to automate tasks that would otherwise require 
human intervention or explicit programming. 
For example, a tool can be used to book a flight for a customer interacting with a chatbot, 
to fill out a form on a web page, or to implement a Java class based on an automated test 
(TDD) in a code generation scenario.


1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI
DevTools 

2. Create a project 'ai-inject-custom-function' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>


4. Add configuration in applications.properties file
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}

5. Run the application once to make sure that it starts without errors by 
injecting your OPEN_AI_KEY 

6. Create 2 service classes and their associated util classes 
WeatherService.java 
RectangularAreaService.java
These 2 classes hold the business logic needed for the application

The following util classes are used for carrying the request and 
response objects to the service layers: 
Request.java
Response.java
WeatherRequest.java
WeatherResponse.java

7. Add the following Configuration files: 
CustomFunctions.java
WeatherTools.java

The purpose of these files are: 
Instead of specifying tools programmatically, you can define tools as Spring beans 
and let Spring AI resolve them dynamically at runtime using the 
ToolCallbackResolver interface (via the SpringBeanToolCallbackResolver implementation). 
This option gives you the possibility to use any Function, Supplier, Consumer, 
or BiFunction bean as a tool. 


8. The Controller has 3 different ways of calling a custom functions:

areaOfRectangle: This is used to directly call the function using Prompt 
which is deprecated and not to be used in future. This returns the actual respone 
returned from the AI model which is the Generation class.

getTemperature: This is used within a FunctionToolCallback that has a method 
called inputType and returns a ChatClient. The result is wrapped around the 
WeatherResponse class.

getTemperatureWithTools: This is used to directly register a tool using
the tools function in the ClientClient class (most ideal way. The result is wrapped 
around the WeatherResponse class.

All 3 methods are used in different ways to register a function and call it,
within the boundaries of AI models. 


9. Run the application - 3 differnt ways:
AreaOfRectangle: 
curl --location 'http://localhost:8080/area-rectangle?message=What%20is%20the%20area%20of%20the%20rectangle%20with%20height%2030%20and%20base%2022'

WhatIsTheWeather (Static):
curl --location 'http://localhost:8080/weather' \
--header 'Content-Type: application/json' \
--data '{
    "question": "Riyadh"
}'

WhatIsTheWeather (Dynamic):
curl --location 'http://localhost:8080/weather-tools' \
--header 'Content-Type: application/json' \
--data '{
    "question": "What'\''s the weather like in Abu Dhabi?"
}'

```

## Chat Memory (a session managment alternative for AI models) - (ai-chat-memory)
```xml 

Prerequisite:
a. Docker desktop to be installed
b. Pull command: docker pull postgres
c. Run Command: docker run --name chat-memory -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
d. Connect with PGAdmin into the running container 
e. Create a user called 'myuser' with password 'secret'
f. Create a database called 'mydatabase' and make user 'myuser' as the owner of this database
g. When the server starts a table called ai_chat_memory will be created automatically under the 
public schema if mydatabase automatically 


1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI
DevTools 
Spring Data JPA
Postgres


2. Create a project 'ai-chat-memory' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>

Note: An additional chat memory dependency needs to be added as below:
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-chat-memory-jdbc</artifactId>
</dependency>

4. Add configuration in applications.properties file
# needed for open ai access
spring.ai.openai.api-key=${OPEN_AI_KEY}
# Data source url 
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
# User name for DB
spring.datasource.username=myuser
# Password for DB
spring.datasource.password=secret
# Initilize the chat memory schema automatically 
spring.ai.chat.memory.repository.jdbc.initialize-schema=true

5. Run the application once to make sure that it starts without errors by 
injecting your OPEN_AI_KEY 

6. Create a configuration class which connects to the database 
using the jdbcTemplate and stores all chat conversations. 
Note: We used MessageWindowChatMemory which is an implementation
of ChatMemory which stores maximum no. of messages, before it deletes
the older messages. 
In our configuration we set the maximum message limit to 10 
using MAX_MESSAGE_SIZE=10

Refer the config class at 
ChatClientConfig.java

7. Two Controller classes have 2 different ways of implementations of chat memory:

InMemoryChatMemoryController.java -> Is an in memory store which stores max 10 messages
but the messages get reset when the application terminates. 

JdbcChatMemoryController.java -> Stores all chat converstation into a database table. 
We set the maximum storage limit to 10 messages using our custom conversation id 
set in the variable 'CONVERSTATION_ID'. In real-time application this could be replaced 
with user id or session id or any other id as per our application choice. 


9. Run the application in two different ways: In Memory and Database Table storage

Way 1: In Memory Conversation Storage (will get deleted when server restarts)

Insert into Table (InMemory) - 01:
curl --location 'http://localhost:8080/in-memory-chat' \
--header 'Content-Type: application/json' \
--data 'My name is Vikram, I am 35 years of age and I travelled to London last winter'

Insert into Table (InMemory) - 02:
curl --location 'http://localhost:8080/in-memory-chat' \
--header 'Content-Type: application/json' \
--data 'Do you know where I travelled last winter?'


Way 2: Database Table Storage (will get stored even if server restarts)

Insert into Table (JDBC): 
curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data 'My name is balaji, I am 50 years of age and I come from India'

Retreive from Chat (JDBC): 
curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data 'What is my name and where do I come from?'

Retreive Conversation History (JDBC):
curl --location 'http://localhost:8080/chat/history' \
--header 'Content-Type: application/json' \
--data ''

Delete Conversation History (JDBC): 
curl --location --request DELETE 'http://localhost:8080/chat/history' \
--header 'Content-Type: application/json'

** Important **
Remember the limitations of InMemoryChatMemory: it's not persistent and 
won't work across multiple application instances. For production scenarios, 
you'd explore database-backed or cache-backed ChatMemory implementations 
(or build your own).

```

## Mutiple AI Models - (ai-multi-model)
```xml
Why you might want to use multiple LLMs in your application:
-----------------------------------------------------------
Comparative Analysis: Different models may excel in various tasks. 
By using multiple LLMs, you can compare outputs and choose the best result.

Specialized Capabilities: Some models might be better at certain tasks, 
like code generation or creative writing.

Redundancy: Having multiple LLMs can provide fallback options 
if one service is unavailable or rate-limited.

Cost Optimization: Different providers have varying pricing models. 
You can route requests to the most cost-effective option based on the task.

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI
Anthropic
DevTools 

2. Create a project 'ai-multi-model' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-anthropic</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# Disable the auto-configuration for ChatClient
# This is important for using multiple ChatClient 
# as default injected by spring must be disabled 
spring.ai.chat.client.enabled=false
# Inject OpenAI key through environment variable
spring.ai.openai.api-key=${OPENAI_API_KEY}
# Select the OpenAI model to use
spring.ai.openai.chat.model=gpt-4
# Inject Anthropic key through environment variable
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
# Select the Anthropic model to use
spring.ai.anthropic.chat.model=claude-3-sonnet-20240229

5. Run the application once to make sure that it starts without errors by 
injecting your OPENAI_API_KEY & ANTHROPIC_API_KEY through env variables

6. Manually create bean definitions for each ChatClient 
Refer the config class at 
ChatClientConfiguration.java

Here two beans are injected as openAIChatClient and anthropicAIChatClient
(manually configuring our ChatClient)

7. Two Controller classes are created:

OpenAiChatController.java -> This is used for creating the ChatClient
using OpenAI  

AnthropicChatController.java -> This is used for creating the ChatClient
using Anthropic (Claude) 

8. Run the application to test the two Controllers: 

curl --location 'http://localhost:8080/claude'

curl --location 'http://localhost:8080/openai'

Each request will return an interesting fact about the respective company, 
demonstrating that we're successfully communicating with two different LLMs 
in the same application.

```

## AI Prompt Engineering (ai-prompt-engineering)
```xml

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Spring Web
OpenAI
Anthropic
DevTools 

2. Create a project 'ai-prompt-engineering' and download

3. The pom.xml will have the following dependencies: 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-anthropic</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# Disable the auto-configuration for ChatClient
# This is important for using multiple ChatClient 
# as default injected by spring must be disabled 
spring.ai.chat.client.enabled=false
# Inject OpenAI key through environment variable
spring.ai.openai.api-key=${OPENAI_API_KEY}
# Inject Anthropic key through environment variable
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}

5. Run the application once to make sure that it starts without errors by 
injecting your OPENAI_API_KEY & ANTHROPIC_API_KEY through env variables

6. Manually create bean definitions for each ChatClient. 
This will allow us to pick the chat client we want during runtime.
Refer the config class at 
MultiModelChatClientConfiguration.java

Here two beans are injected as openAIChatClient and anthropicAIChatClient
(manually configuring our ChatClient)

7. Controller classes:

7.1. OpenAiChatController.java
This is used for creating the ChatClient
using OpenAI and general OpenAI prompt option parameters are 
explained here.
Run this controller with the below curl command: 
curl --location 'http://localhost:8080/openai-chatoptions'


7.2. AnthropicChatController.java
This is used for creating the ChatClient
using Anthropic (Claude) and general Anthropic prompt option parameters are 
explained here.
Run this controller with the below curl command:
curl --location 'http://localhost:8080/claude-chatoptions'


7.3. Zero-Shot Prompting
Zero-shot prompting involves asking an AI to perform a task without providing any examples. 
This approach tests the model's ability to understand and execute instructions from scratch. 
Large language models are trained on vast corpora of text, allowing them to understand what 
tasks like "translation," "summarization," or "classification" entail 
without explicit demonstrations.
Zero-shot is ideal for straightforward tasks where the model likely has seen similar examples 
during training, and when you want to minimize prompt length. However, performance may vary 
depending on task complexity and how well the instructions are formulated.

Sample Controller: ZeroShotController.java
Run this controller with the below curl command:
curl --location 'http://localhost:8080/openai-zeroshotprompting'


7.4. One-Shot & Few-Shot Prompting
Few-shot prompting provides the model with one or more examples to help guide its responses, 
particularly useful for tasks requiring specific output formats. 
By showing the model examples of desired input-output pairs, 
it can learn the pattern and apply it to new inputs without explicit parameter updates.

One-shot provides a single example, which is useful when examples are costly or 
when the pattern is relatively simple. 
Few-shot uses multiple examples (typically 3-5) to help the model better understand patterns in 
more complex tasks or to illustrate different variations of correct outputs.

Sample Controller: OneShotFewShotController.java
Run this controller with the below curl command:
curl --location 'http://localhost:8080/openai-oneshotfewshotprompting'


7.5. System prompting
System prompting sets the overall context and purpose for the language model, 
defining the "big picture" of what the model should be doing. 
It establishes the behavioral framework, constraints, 
and high-level objectives for the model's responses, separate 
from the specific user queries.

System prompts act as a persistent "mission statement" throughout the conversation, 
allowing you to set global parameters like output format, tone, ethical boundaries, 
or role definitions. Unlike user prompts which focus on specific tasks, 
system prompts frame how all user prompts should be interpreted.

Sample Controller: SystemPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-systemprompting1'
curl --location 'http://localhost:8080/openai-systemprompting2'


7.6 Role prompting
Role prompting instructs the model to adopt a specific role or persona, 
which affects how it generates content. By assigning a particular identity, 
expertise, or perspective to the model, you can influence the style, tone, 
depth, and framing of its responses.

Role prompting leverages the model's ability to simulate different expertise domains 
and communication styles. Common roles include expert 
(e.g., "You are an experienced data scientist"), professional (e.g., "Act as a travel guide"), 
or stylistic character (e.g., "Explain like you're Shakespeare").

Sample Controller: RolePromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-roleprompting1'
curl --location 'http://localhost:8080/openai-roleprompting2'


7.7 Contextual Prompting
Contextual prompting provides additional background information to the model 
by passing context parameters. This technique enriches the model's understanding 
of the specific situation, enabling more relevant and tailored responses 
without cluttering the main instruction.

By supplying contextual information, you help the model understand the specific domain, 
audience, constraints, or background facts relevant to the current query. 
This leads to more accurate, relevant, and appropriately framed responses.

Sample Controller: ContextPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-contextprompting'


7.8 Step-back prompting
Step-back prompting breaks complex requests into simpler steps by first acquiring 
background knowledge. This technique encourages the model to first "step back" 
from the immediate question to consider the broader context, fundamental principles, 
or general knowledge relevant to the problem before addressing the specific query.

By decomposing complex problems into more manageable components and establishing 
foundational knowledge first, the model can provide more accurate responses to 
difficult questions.

Sample Controller: StepbackPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-stepbackprompting'


7.9 Chain of Thought (CoT)
Chain of Thought prompting encourages the model to reason step-by-step through a problem, 
which improves accuracy for complex reasoning tasks. By explicitly asking the model to show 
its work or think through a problem in logical steps, you can dramatically improve performance 
on tasks requiring multi-step reasoning.

CoT works by encouraging the model to generate intermediate reasoning steps before 
producing a final answer, similar to how humans solve complex problems. This makes the model's 
thinking process explicit and helps it arrive at more accurate conclusions.

Sample Controller: ChainOfThoughtPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-cotzeroshotprompting'
curl --location 'http://localhost:8080/openai-cotsinglefewshotprompting'


7.10 Self-Consistency
Self-consistency involves running the model multiple times and aggregating results 
for more reliable answers. This technique addresses the variability in LLM outputs by 
sampling diverse reasoning paths for the same problem and selecting the most 
consistent answer through majority voting.

By generating multiple reasoning paths with different temperature or sampling settings, 
then aggregating the final answers, self-consistency improves accuracy on complex reasoning 
tasks. It's essentially an ensemble method for LLM outputs.

Sample Controller: SelfConsistencyPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-selfconsistencyprompting'


7.11 Tree of Thoughts (ToT)
Tree of Thoughts (ToT) is an advanced reasoning framework that extends Chain of Thought 
by exploring multiple reasoning paths simultaneously. It treats problem-solving as a 
search process where the model generates different intermediate steps, 
evaluates their promise, and explores the most promising paths.

This technique is particularly powerful for complex problems with multiple possible 
approaches or when the solution requires exploring various alternatives 
before finding the optimal path.

Sample Controller: TreeOfThoughtPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-treeofthoughtprompting'


7.12 Automatic Prompt Engineering
Automatic Prompt Engineering uses the AI to generate and evaluate alternative prompts. 
This meta-technique leverages the language model itself to create, refine, 
and benchmark different prompt variations to find optimal formulations for specific tasks.

By systematically generating and evaluating prompt variations, 
APE can find more effective prompts than manual engineering, 
especially for complex tasks. It's a way of using AI to improve its own performance.

Sample Controller: AutomaticPromptController.java
Run this controller with the below curl commands:
curl --location 'http://localhost:8080/openai-automaticprompting'


7.13 Code prompting
Code prompting refers to specialized techniques for code-related tasks. 
These techniques leverage LLMs' ability to understand and generate programming languages, 
enabling them to write new code, explain existing code, debug issues, and translate between languages.

Effective code prompting typically involves clear specifications, appropriate context 
(libraries, frameworks, style guidelines), and sometimes examples of similar code. 
Temperature settings tend to be lower (0.1-0.3) for more deterministic outputs.

Sample Controller: CodePromptController.java 
Run this controller with the below curl commands:

# The below curl is used for code prompting
curl --location 'http://localhost:8080/openai-codeprompting'

# The below curl is used for code prompting along with explaination
curl --location 'http://localhost:8080/openai-explaincodeprompting'

# The below curl is used for code in one programming lanuage to another programming language 
curl --location 'http://localhost:8080/openai-translatecodeprompting'


Reference: 
https://spring.io/blog/2025/04/14/spring-ai-prompt-engineering-patterns
```

## Prompt Engineering Patterns
```xml
Must read to understand Prompt Engineering Patterns 

https://spring.io/blog/2025/04/14/spring-ai-prompt-engineering-patterns

Important tips for prompt engineering:
For production applications, always remember to:
1. Test prompts with different parameters (temperature, top-k, top-p)
2. Consider using self-consistency for critical decision-making
3. Leverage Spring AI's entity mapping for type-safe responses
4. Use contextual prompting to provide application-specific knowledge

```

## ChromaDB (same as pgvector)
```xml
docker pull chromadb/chroma
docker run -v <path-to-store-chromadb> -p 8000:8000 chromadb/chroma
eg. docker run -v ./chroma-data:/data -p 8000:8000 chromadb/chroma

After starting run the following command to check: 
http://localhost:8000/docs/


```

## Model Context Protocol and why we need it? 
```xml
The Model Context Protocol (MCP) is a standardized protocol that enables AI models 
to interact with external tools and resources in a structured way. 
It supports multiple transport mechanisms to provide flexibility across 
different environments.

The MCP Java SDK provides a Java implementation of the Model Context Protocol, 
enabling standardized interaction with AI models and tools through both synchronous 
and asynchronous communication patterns.

Spring AI MCP extends the MCP Java SDK with Spring Boot integration, 
providing both client and server starters. 
Bootstrap your AI applications with MCP support using Spring Initializer.
```

## Spring AI MCP Server (ai-mcp-server)
```xml

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Model Context Protocol Server
DevTools 

2. Create a project 'ai-mcp-server' and download

3. The pom.xml will have the following dependencies: 
Note: Replace the spring-ai-mcp-server-spring-boot-starter with 
spring-ai-starter-mcp-server-webflux -> as we will use webflux 

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# MCP server name
spring.ai.mcp.server.name=ai-mcp-server
# MCP server version
spring.ai.mcp.server.version=0.0.1
# Port to start the application
server.port: 8060
# Logging level set to DEBUG to check what happens in the server 
# when the client sends the request
logging.level.org.springframework.ai: DEBUG

5. Run the application once to make sure that it starts without errors 

6. Create a DTO called Course that holds the output 
Course.java
We keep it simple with just 2 attributes title and url

7. Next we create the Service layer which hold the Tools that 
we want to enable our AI to refer to. 
CourseService.java 
Two tools will be registered from this service:
getAllCourses -> Which will list all courses available 
getCourse -> Which will fetch a course based on the title 

8. Next we create a config class where we register our bean 
which will list our tools using ToolsCallBack. 
ToolsConfig.java

9. Finally run the server to check if our tools are registered properly.
-> Registered tools: 2, notification: true (from the log)

```


## Spring AI MCP Client (ai-mcp-client)
```xml

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Model Context Protocol Client
Spring AI
Spring Web
DevTools 

2. Create a project 'ai-mcp-client' and download

3. The pom.xml will have the following dependencies: 
Note: Replace the spring-ai-mcp-client-spring-boot-starter with 
spring-ai-starter-mcp-client-webflux -> as we will use webflux

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-mcp-client-webflux</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# MCP Client name
spring.application.name=ai-mcp-client
# Open AI API key 
spring.ai.openai.api-key=${OPEN_AI_KEY}
# MCP Server URL 
spring.ai.mcp.client.sse.connections.ai-mcp-server.url=http://localhost:8060

5. Run the application once by injecting the OPEN_AI_KEY as environment variable 
to make sure that it starts without errors 

6. Create a controller called 
ToolUsageController.java 
where we inject tools callback provider in our constructor and set the 
default tool callback: 
defaultToolCallbacks(toolsCallbackProvider.getToolCallbacks())

We also create two methods getAllCourses and getDetailsOfACourse with HTTP end points 
that will use the tools already injected as default tool callback in the constructor. 

7. Finally run the client application and test it: 
curl --location 'http://localhost:8080/tool1'
curl --location 'http://localhost:8080/tool2' 

Note: Hardcoded PromptTemplate values will be dropped and dynamic data 
will be passed in production applications. 

```

## Spring AI Ollama (ai-ollama)
```xml
We can use Ollama, an open-source tool, to run LLMs on our local machines. 
In this tutorial, we’ll explore how to use Spring AI and Ollama. 

Prerequisite:
Download and install Ollama for the local machine:
https://ollama.com/download

Pull a chat model (Examples below):
ollama pull llama3.2
ollama pull MISTRAL
ollama pull deepseek-r1

List and check all the currently installed models:
ollama list

List of models supported by Ollama: 
https://ollama.com/library

Now we’ll build a simple Mathematical equation solver using a Ollama model. 

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Ollama
Spring Web
DevTools 

2. Create a project 'ai-ollama' and download

3. The pom.xml will have the following dependencies: 

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# Application name
spring.application.name=ai-ollama
# Enables automatic model pulling only when missing at startup time. 
# For production, you should pre-download the models to avoid delays
spring.ai.ollama.init.pull-model-strategy=when_missing
# During startup pull all supported models
spring.ai.ollama.chat.options.model=LLAMA3.2
spring.ai.ollama.init.chat.additional-models=mistral,DeepSeek-R1


5. Create a controller called OllamaController.java 
which is a RestController and we inject the 
ChatModel into this controller. 

We also create a method called solveEquation that takes 
UserInput as input parameters and returns a String response.

6. Finally run the application and test it with different 
Ollama supported models:

SolveEquation - Using Llama3.2:
curl --location 'http://localhost:8080/solve' \
--header 'Content-Type: application/json' \
--data '{
    "equation": "how can I solve 8x + 7 = -29",
    "model" : "LLAMA3.2"
}' 

SolveEquation - Using Mistral:
curl --location 'http://localhost:8080/solve' \
--header 'Content-Type: application/json' \
--data '{
    "equation": "how can I solve 8x + 7 = -29",
    "model" : "MISTRAL"
}'

SolveEquation - Using Deepseek R1: 
curl --location 'http://localhost:8080/solve' \
--header 'Content-Type: application/json' \
--data '{
    "equation": "how can I solve 8x + 7 = -29",
    "model" : "DeepSeek-R1"
}'

```


## Spring AI Ollama Huggingface (ai-chat-ollama-huggingface)
```xml

We can use Ollama, an open-source tool, to run LLMs on our local machines. 
It supports running GGUF format models from Hugging Face.

Ollama now supports all GGUF models from Hugging Face, 
allowing access to over 45,000 community-created models through Spring AI's 
Ollama integration, runnable locally.

In this tutorial, we’ll explore how to use Hugging Face models with Spring AI and Ollama. 
We’ll build a simple chatbot using a chat completion model

1. Spring Initilizer
Go to spring initilizer page https://start.spring.io/ 
and add the following dependencies: 
Ollama
Spring Web
DevTools 

2. Create a project 'ai-chat-ollama-huggingface' and download

3. The pom.xml will have the following dependencies: 

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>

4. Add configuration in applications.properties file
# Application name
spring.application.name=ai-chat-ollama-huggingface
# Enables automatic model pulling at startup time. 
# For production, you should pre-download the models to avoid delays
spring.ai.ollama.init.pull-model-strategy=always
# Specifies the Hugging Face GGUF model to use using the format: hf.co/{username}/{repository}
# List of available models an be found here:
# https://huggingface.co/models?library=gguf&sort=trending
spring.ai.ollama.chat.options.model=hf.co/microsoft/Phi-3-mini-4k-instruct-gguf


5. Create a configuration class ChatbotConfiguration.java  
to configure 2 beans: 
ChatMemory -> using InMemoryChatMemory
ChatClient -> using MessageChatMemoryAdvisor

6. Create 2 record classes as for DTOs.
ChatRequest.java
ChatResponse.java

7. Create a service class called ChatbotService.java 
to inject the ChatClient 
and a ChatResponse chat method that takes a ChatRequest and 
results in a ChatResponse


8. Create a controller called 
ChatController.java 
which is a RestController and we inject the 
ChatbotService into this controller. 

We also create a method called chat that takes 
ChatRequest and sends a ChatResponse as ResponseEntity 

7. Finally run the application and test it: 
curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data '{
    "question": "Who wanted to kill Harry Potter?"
}'

Copy the  chatId and pass it as input to the second call,
 to maintain the same context of the chat.  

curl --location 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data '{
"chatId": "<your UUID>",
"question" : "Who should he have gone after instead?"
}'

```


## Additional Resources
```xml
Model Context Protocol (MCP) - 
Standardized protocol that enables AI models to interact with external tools 
and resources in a structured way
https://docs.spring.io/spring-ai/reference/1.0/api/mcp/mcp-overview.html

Evalautors - To testing the output of a model 
https://docs.spring.io/spring-ai/reference/1.0/api/testing.html

Langchain4j: 
https://dzone.com/articles/chat-with-your-knowledge-base-java-langchain4j-guide?edition=598293

Building Effective Agents with Spring AI
https://spring.io/blog/2025/01/21/spring-ai-agentic-patterns

```






### Reference
```xml
https://dzone.com/articles/spring-ai-generate-images-openai-dalle?edition=958905
https://docs.spring.io/spring-ai/reference/
https://docs.spring.io/spring-ai/reference/api/image/openai-image.html
https://docs.spring.io/spring-ai/reference/1.0/api/etl-pipeline.html

https://www.youtube.com/watch?v=9Crrhz0pm8s
https://docs.spring.io/spring-ai/reference/concepts.html

https://www.youtube.com/watch?v=tx5OapbK-8A
https://www.youtube.com/watch?v=4-rG2qsTrAs
https://www.youtube.com/watch?v=-KrqLaJ0uaQ

https://www.cloudflare.com/learning/ai/what-is-vector-database/

https://docs.spring.io/spring-ai/reference/1.0/api/tools.html
https://www.devturtleblog.com/sring-ai-function-calling-tutorial/

https://spring.io/blog/2025/04/14/spring-ai-prompt-engineering-patterns
https://bootcamptoprod.com/spring-ai-chat-memory-guide/

https://www.danvega.dev/blog/spring-ai-multiple-llms
https://huggingface.co/models

https://dzone.com/articles/chat-with-your-knowledge-base-java-langchain4j-guide?edition=598293
https://www.baeldung.com/spring-ai-openai-tts
https://www.baeldung.com/spring-ai-ollama-hugging-face-models

```
