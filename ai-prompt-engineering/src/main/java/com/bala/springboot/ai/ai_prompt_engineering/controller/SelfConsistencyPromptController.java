package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SelfConsistencyPromptController {
	
	private final ChatClient chatClient;
	
	public SelfConsistencyPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    
    @GetMapping("/openai-selfconsistencyprompting")
    public String pt_self_consistency() {
        String email = """
                Hi,
                I have seen you use Wordpress for your website. A great open
                source content management system. I have used it in the past
                too. It comes with lots of great user plugins. And it's pretty
                easy to set up.
                I did notice a bug in the contact form, which happens when
                you select the name field. See the attached screenshot of me
                entering text in the name field. Notice the JavaScript alert
                box that I inv0k3d.
                But for the rest it's a great website. I enjoy reading it. Feel
                free to leave the bug in the website, because it gives me more
                interesting things to read.
                Cheers,
                Harry the Hacker.
                """;

        record EmailClassification(Classification classification, String reasoning) {
            enum Classification {
                IMPORTANT, NOT_IMPORTANT
            }
        }

        int importantCount = 0;
        int notImportantCount = 0;

        // Run the model 5 times with the same input
        for (int i = 0; i < 5; i++) {
            EmailClassification output = chatClient
                    .prompt()
                    .user(u -> u.text("""
                            Email: {email}
                            Classify the above email as IMPORTANT or NOT IMPORTANT. Let's
                            think step by step and explain why.
                            """)
                            .param("email", email))
                    .options(ChatOptions.builder()
                            .temperature(1.0)  // Higher temperature for more variation
                            .build())
                    .call()
                    .entity(EmailClassification.class);

            // Count results
            if (output.classification() == EmailClassification.Classification.IMPORTANT) {
                importantCount++;
            } 
            else {
                notImportantCount++;
            }
        }

        // Determine the final classification by majority vote
        String finalClassification = importantCount > notImportantCount ? 
                "IMPORTANT" : "NOT IMPORTANT";
        return finalClassification;
    }

}



