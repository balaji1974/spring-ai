
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


Reference: 
https://docs.spring.io/spring-ai/reference/concepts.html

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
spring.ai.openai.api-key=<your-api-key>
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


```




### Reference
```xml
https://dzone.com/articles/spring-ai-generate-images-openai-dalle?edition=958905
https://docs.spring.io/spring-ai/reference/
https://docs.spring.io/spring-ai/reference/api/image/openai-image.html

https://www.youtube.com/watch?v=9Crrhz0pm8s
https://docs.spring.io/spring-ai/reference/concepts.html

```
