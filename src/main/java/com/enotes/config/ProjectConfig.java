package com.enotes.config;

import java.beans.BeanProperty;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();

	}
}
