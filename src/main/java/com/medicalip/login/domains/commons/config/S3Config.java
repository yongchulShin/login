package com.medicalip.login.domains.commons.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
	
//	@Value("${cloud.aws.credentials.access-key}")
//	private String accessKey;
//	
//	@Value("${cloud.aws.credentials.secret-key}")
//	private String secretKey;
//	
//	@Value("${cloud.aws.region.static}")
//	private String region;
//	
//	@Bean
//	@Primary
//	public BasicAWSCredentials awsCredentialsProvider() {
//		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return basicAWSCredentials;
//	}
//	
//	@Bean
//    public AmazonS3 amazonS3() {
//        AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
//                .build();
//        return s3Builder;
//    }

}
