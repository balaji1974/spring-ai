package com.bala.springboot.ai.ai_vector_db.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bala.springboot.ai.ai_vector_db.jdbc.PassengerRowMapper;
import com.bala.springboot.ai.ai_vector_db.model.Passenger;

@Configuration
public class VectorStoreBuilder {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	VectorStore vectorStore;

	
	private static final Logger log = LoggerFactory.getLogger(VectorStoreBuilder.class);
	
	
	@Bean("pgVectorStore")
	public VectorStore pgVectorStore() {
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
		
		return vectorStore;
	
	}
	
	@Bean("mongoVectorStore")
    public VectorStore mongoVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        // You would configure MongoDBAtlasVectorStore with your specific collection and index details
        MongoDBAtlasVectorStore mvs= MongoDBAtlasVectorStore.builder(mongoTemplate, embeddingModel)
                .collectionName("vector_store")           // Optional: defaults to "vector_store"
                .vectorIndexName("vector_index")          // Optional: defaults to "vector_index"
                .pathName("embedding")                    // Optional: defaults to "embedding"
                .numCandidates(500)                             // Optional: defaults to 200
                //.metadataFieldsToFilter(List.of("author", "year")) // Optional: defaults to empty list
                .initializeSchema(true)                         // Optional: defaults to false
                .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
                .build();  
        
        long count=mongoTemplate.getCollection("vector_store").countDocuments();
        // Just to insert only one time 
		if(count<=0) {
	        List<Document> docs = List.of(
        		new Document("Proper tuber planting involves site selection, proper timing, and exceptional care. Choose spots with well-drained soil and adequate sun exposure. Tubers are generally planted in spring, but depending on the plant, timing varies. Always plant with the eyes facing upward at a depth two to three times the tuber's height. Ensure 4 inch spacing between small tubers, expand to 12 inches for large ones. Adequate moisture is needed, yet do not overwater. Mulching can help preserve moisture and prevent weed growth.", Map.of("author", "A", "type","post")),
                new Document("Successful oil painting necessitates patience, proper equipment, and technique. Begin with a carefully prepared, primed canvas. Sketch your composition lightly before applying paint. Use high-quality brushes and oils to create vibrant, long-lasting artworks. Remember to paint 'fat over lean,' meaning each subsequent layer should contain more oil to prevent cracking. Allow each layer to dry before applying another. Clean your brushes often and avoid solvents that might damage them. Finally, always work in a well-ventilated space.", Map.of("author", "A")),
                new Document("For a natural lawn, selection of the right grass type suitable for your climate is crucial. Balanced watering, generally 1 to 1.5 inches per week, is important; overwatering invites disease. Opt for organic fertilizers over synthetic versions to provide necessary nutrients and improve soil structure. Regular lawn aeration helps root growth and prevents soil compaction. Practice natural pest control and consider overseeding to maintain a dense sward, which naturally combats weeds and pest.", Map.of("author", "B", "type","post"))
		    );
	        mvs.add(docs);
	        System.out.println( "Documents added successfully!");
		}
		return mvs;
    }
}




