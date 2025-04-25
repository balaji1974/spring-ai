package com.bala.springboot.ai.ai_vector_db.jdbc;


import org.springframework.jdbc.core.RowMapper;

import com.bala.springboot.ai.ai_vector_db.model.Passenger;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PassengerRowMapper implements RowMapper<Passenger> {
	   @Override
	   public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
	       return new Passenger(
	               rs.getInt("passengerid"),
	               rs.getInt("survived"),
	               rs.getInt("pclass"),
	               rs.getString("name"),
	               rs.getString("sex"),
	               rs.getDouble("age"),
	               rs.getInt("sibsp"),
	               rs.getInt("parch"),
	               rs.getString("ticket"),
	               rs.getDouble("fare"),
	               rs.getString("cabin"),
	               rs.getString("embarked"),
	               rs.getDouble("wikiid"),
	               rs.getString("name_wiki"),
	               rs.getDouble("age_wiki"),
	               rs.getString("hometown"),
	               rs.getString("boarded"),
	               rs.getString("destination"),
	               rs.getString("lifeboat"),
	               rs.getString("body"),
	               rs.getInt("class")
	       );
	   }

}


