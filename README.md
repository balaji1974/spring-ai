
# Spring AI

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

```
