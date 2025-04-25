package com.bala.springboot.ai.ai_vector_db.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bala.springboot.ai.ai_vector_db.jdbc.PassengerRowMapper;
import com.bala.springboot.ai.ai_vector_db.model.Passenger;

@Configuration
public class VectorBuilder {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	VectorStore vectorStore;
	
	private static final Logger log = LoggerFactory.getLogger(VectorBuilder.class);
	
	private static final String PROMPT_STRING="male passenger who survived and final desitination was Newyork";
	
	@Bean
	public VectorStore buildVectorStore() {
		String sql = """
		           SELECT passengerid, survived, pclass, name, sex, age, sibsp,
		           parch, ticket, fare, cabin, embarked, wikiid, name_wiki, age_wiki,
		           hometown, boarded, destination, lifeboat, body, class 
		           FROM passenger;
		           """;
		
		//ValueConverters intConverter = ValueConverters.ENGLISH_INTEGER;
		String sqlTableChecker = "SELECT count(*) FROM vector_store";
		Integer count = jdbcTemplate.queryForObject(sqlTableChecker, Integer.class); 
		List<Passenger> passengers=jdbcTemplate.query(sql,new PassengerRowMapper());
		if(count == 0) { // Check to prevent reinserting data into the same table repeatedly during startup
			var textSplitter = new TokenTextSplitter();
			log.info("Size of records read from database is :" +passengers.size());
			log.info("Inserting records into vector database");
			passengers.parallelStream().forEach( passenger-> {
				log.info("Inserting record into vector database for passenger :"+passenger.getName());
				// Prompt Engineering
				String content="The passenger's name was " +passenger.getName() + " and final destination was "+ passenger.getDestination() +".";
				
				
				String survival=new String();
				if(passenger.getSurvived()==0) {
					survival= "This passenger died in titanic accident and ";
				}
				else {
					survival= "This passenger survived in titanic accident and ";
				}
				String sex=new String();
				if(passenger.getSex().trim().equalsIgnoreCase("male")) {
					sex= "he was a male passenger.";
				}
				else if(passenger.getSex().trim().equalsIgnoreCase("female")) {
					sex= "she was a female passenger.";
				}
				else {
					sex= "this passenger's sex was unknown.";
				}
				content=content + survival + sex; 
				// End Prompt Engineering
				log.info(content);
				
				
				var document = new Document( content ,
					Map.ofEntries (
						Map.entry("passengerid", passenger.getPassengerId()),
						Map.entry("name", passenger.getName())
					)
				);
				var split = textSplitter.apply(List.of(document));
				vectorStore.add(split);
				log.info("Record inserted into vector database for passenger :"+passenger.getName());
			
			});
			log.info("All records inserted into vector database successfully");
		}
		else log.info("Vector table already exist and no records were inserted");
		
		log.info("Getting similar results for passengers who died");
		var similarRecords = vectorStore
				.similaritySearch(SearchRequest.builder().topK(10)
				.query(PROMPT_STRING)
				.build()
		);
		similarRecords.stream().forEach(s -> {
			System.out.println(s);
		});
		
		return vectorStore;
	
	}

}




