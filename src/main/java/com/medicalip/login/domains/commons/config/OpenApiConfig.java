package com.medicalip.login.domains.commons.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	  @Bean
	  public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
		  	Info info = new Info()
			        .title("Medicalip Login API")
			        .version(springdocVersion)
			        .description("Medicalip Login API");
		    
		    SecurityScheme securityScheme = new SecurityScheme()
		          	.type(SecurityScheme.Type.HTTP). scheme("Bearer").bearerFormat("JWT")
		          	.in(SecurityScheme.In.HEADER).name("Authorization");
		    
		    SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");
		    
		    return new OpenAPI()
		        .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
		        .security(Arrays.asList(schemaRequirement))
		        .info(info);
	  }
	
}
