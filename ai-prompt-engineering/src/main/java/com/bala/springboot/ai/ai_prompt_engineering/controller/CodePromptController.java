package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CodePromptController {
	
	private final ChatClient chatClient;
	
	public CodePromptController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
    
    @GetMapping("/openai-codeprompting")
    public String pt_code_prompting_writing_code() {
        String bashScript = chatClient
                .prompt("""
                        Write a code snippet in Bash, which asks for a folder name.
                        Then it takes the contents of the folder and renames all the
                        files inside by prepending the name draft to the file name.
                        """)
                .options(ChatOptions.builder()
                        .temperature(0.1)  // Low temperature for deterministic code
                        .build())
                .call()
                .content();
        return bashScript;
    }

    @GetMapping("/openai-explaincodeprompting")
    public String pt_code_prompting_explaining_code() {
        String code = """
                #!/bin/bash
                echo "Enter the folder name: "
                read folder_name
                if [ ! -d "$folder_name" ]; then
                echo "Folder does not exist."
                exit 1
                fi
                files=( "$folder_name"/* )
                for file in "${files[@]}"; do
                new_file_name="draft_$(basename "$file")"
                mv "$file" "$new_file_name"
                done
                echo "Files renamed successfully."
                """;

        String explanation = chatClient
                .prompt()
                .user(u -> u.text("""
                        Explain to me the below Bash code:
                        ```
                        {code}
                        ```
                        """).param("code", code))
                .call()
                .content();
        return explanation;
    }

    @GetMapping("/openai-translatecodeprompting")
    public String pt_code_prompting_translating_code() {
        String bashCode = """
                #!/bin/bash
                echo "Enter the folder name: "
                read folder_name
                if [ ! -d "$folder_name" ]; then
                echo "Folder does not exist."
                exit 1
                fi
                files=( "$folder_name"/* )
                for file in "${files[@]}"; do
                new_file_name="draft_$(basename "$file")"
                mv "$file" "$new_file_name"
                done
                echo "Files renamed successfully."
                """;

        String pythonCode = chatClient
                .prompt()
                .user(u -> u.text("""
                        Translate the below Bash code to a Python snippet:                        
                        {code}                        
                        """).param("code", bashCode))
                .call()
                .content();
        return pythonCode;
    }

}



