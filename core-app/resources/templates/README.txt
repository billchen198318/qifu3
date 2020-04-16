2020-04-16T11:05:25.997+0800 [] DEBUG Creating shared instance of singleton bean 'freeMarkerConfigurer'
2020-04-16T11:05:26.076+0800 [] DEBUG Cannot resolve template loader path [classpath:/templates/] to [java.io.File]: using SpringTemplateLoader as fallback
java.io.FileNotFoundException: class path resource [templates/] cannot be resolved to URL because it does not exist
	at org.springframework.core.io.ClassPathResource.getURL(ClassPathResource.java:195) ~[spring-core-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.core.io.AbstractFileResolvingResource.getFile(AbstractFileResolvingResource.java:150) ~[spring-core-5.2.5.RELEASE.jar:5.2.5.RELEASE]