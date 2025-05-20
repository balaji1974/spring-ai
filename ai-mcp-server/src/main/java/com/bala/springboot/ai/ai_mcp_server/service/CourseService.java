package com.bala.springboot.ai.ai_mcp_server.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import com.bala.springboot.ai.ai_mcp_server.dto.Course;

import jakarta.annotation.PostConstruct;

@Service
public class CourseService {
	
	public static final Logger log = LoggerFactory.getLogger(CourseService.class);
	public List<Course> courses=new ArrayList<>();
	
	@PostConstruct	
	public void init() {
		courses.addAll(
				List.of(
						new Course("Java Programming", "http://example.com/java"),
						new Course("Python Programming", "http://example.com/python"),
						new Course("JavaScript Programming", "http://example.com/javascript"),
						new Course("C++ Programming", "http://example.com/cpp"),
						new Course("C# Programming", "http://example.com/csharp")
				)
		);
	}
	
	@Tool(name="dv_get_courses", description="Fetch a list of all available courses")
	public List<Course> getAllCourses() {
		log.info("Fetching all courses");
		return courses;
	}
	
	@Tool(name="dv_get_course", description="Fetch a course based on its title")
	public Course getCourse(String title) {
		log.info("Fetching course with name: {}", title);
		return courses.stream().filter(course -> course.title().equalsIgnoreCase(title)).findFirst().orElse(null);
	}
}
