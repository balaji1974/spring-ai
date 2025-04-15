
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

@Service -> ImageModel is the main interface used for generating images 
and it is called in the service layer

@RestController() -> This opens up an URL http://localhost:8080/image/generate
to which you can submit your image generation prompt 

Run the service and call the url like this example below: 
curl -X GET "http://localhost:8080/image/generate?prompt=Monkey%20playing%20cricket"

and you will receive a response which can be run on a browser to check the generated image 

Full details of this example can be found here:
https://dzone.com/articles/spring-ai-generate-images-openai-dalle?edition=958905

More details about spring ai can be found here:
https://docs.spring.io/spring-ai/reference/

```

## OpenAI Chatbot (ai-chat-assistant)
```xml

@RestController() -> This opens up an URL http://localhost:8080/chat/query
to which you can submit your chating prompt 

Run the service and call the url like this example below: 
curl --location 'http://localhost:8080/chat/query?userInput=What%20is%20the%20best%20language%20model%20for%20AI%3F%20'

and you will receive a response which can be displayed on the screen 

Full details of this example can be found here:
https://docs.spring.io/spring-ai/reference/api/chatclient.html

***Note: This sample is just a tip of an iceberg of what chat assistant could do******

```

## OpenAI Chatbot - Formatting the output (ai-chat-assistant-formatted-output)
```xml

@RestController() -> This opens up an URL http://localhost:8080/chat/query
to which you can submit your chating prompt 


There is also an overloaded entity method with the signature 
entity(ParameterizedTypeReference<T> type) that lets you specify types 
such as generic Lists:
@GetMapping("/chat/query")
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
curl --location 'http://localhost:8080/chat/query?question=Who%20are%20the%20top%2010%20actors%20in%20the%20world%3F'
curl --location 'http://localhost:8080/chat/query?question=What%20is%20the%20top%205%20language%20model%20for%20AI%3F'

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
curl --location 'http://localhost:8080/ask-ai?prompt=What%20is%20the%20top%20rated%20AI%20tool%20to%20learn%3F%20and%20format%20the%20output'

```

## OpenAI Chatbot - Customizing the ChatModel (ai-prompt-assistant)
```xml
10. Create 2 static final variables in the Service class
This variables and many others could be customized. 
Full list could be found in the below link. 
https://platform.openai.com/docs/api-reference/chat/create
private static final String MODEL="gpt-4o"; // https://platform.openai.com/docs/models
private static final Double TEMPRATURE=0.4D; 


11. Create a new method called getResponseOptions and 
customize the chatModel with the final varibale values created. 

public String getResponseOptions(String prompt) {
  ChatResponse response = chatModel.call(
        new Prompt(
            prompt,
            OpenAiChatOptions.builder()
                .model(MODEL)
                .temperature(TEMPRATURE)
            .build()
        ));
  return response.getResult().getOutput().getContent();
}

12. Add a GetMapping to the controller and pass the request parameter 
named prompt to the askAIChatService's getResponseOptions method 
and return the response: 

@GetMapping("ask-ai-options")
public String getResponeOptions(@RequestParam String prompt) {
  return askAIChatService.getResponseOptions(prompt);
}

13. Start the application and execute the service from a browser 
or postman or from command line using curl 
curl --location 'http://localhost:8080/ask-ai-options?prompt=What%20is%20the%20best%20programming%20language%20to%20learn%20today%3F'


```


## OpenAI Chatbot - Formatting the output of the ChatModel (ai-prompt-assistant)
```xml
14. Create a record in the Service class that will carry the formatted output
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
public MathReasoning getResponeFormatted() {
  var outputConverter = new BeanOutputConverter<>(MathReasoning.class);

  var jsonSchema = outputConverter.getJsonSchema();

  Prompt prompt = new Prompt("how can I solve 8x + 7 = -23",
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
public MathReasoning getResponeMath() {
  return askAIChatService.getResponeFormatted();
}

17. Start the application and execute the service from a browser 
or postman or from command line using curl 
curl --location 'http://localhost:8080/ask-ai-math'


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

22. Run this on the browser and add any prompt you need 
and see the result.
http://localhost:8080/ask-ai-image-generate?prompt=Harry%20Potter%20in%20India

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
curl --location 'http://localhost:8080/ask-ai-recipe-creator?ingredients=chicken%2C%20curry%20masala%2C%20olives%2C%20onion%2C%20tomatoes&cuisine=Indian'

```

## OpenAI - Audio Transcription model - Step by Step (ai-audio-transcribe)
```xml
1. Add the Spring AI dependency. 

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

6. Run the following from command line or postman:
curl --location 'http://localhost:8080/ai-audio-transcriptor?file=null' \
--form 'file=@"/<your-file-location>/hello.mp3"'

Note: I created the hello.mp3 from the following website: https://ttsmp3.com/

```

## OpenAI RAG - Retrieval Augmented Generation (ai-rag-textreader)
```xml
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


## Running a Machine Learning model locally - Ollama
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

### Reference
```xml
https://dzone.com/articles/spring-ai-generate-images-openai-dalle?edition=958905
https://docs.spring.io/spring-ai/reference/
https://docs.spring.io/spring-ai/reference/api/image/openai-image.html

https://www.youtube.com/watch?v=9Crrhz0pm8s
https://docs.spring.io/spring-ai/reference/concepts.html

https://www.youtube.com/watch?v=tx5OapbK-8A
https://www.youtube.com/watch?v=4-rG2qsTrAs
https://www.youtube.com/watch?v=-KrqLaJ0uaQ

https://www.cloudflare.com/learning/ai/what-is-vector-database/

```
