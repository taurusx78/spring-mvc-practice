package config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import common.CommonExceptionHandler;
import common.RestCommonExceptionHandler;
import interceptor.AuthCheckInterceptor;

// @EnableWebMvc
// - 스프링 MVC 설정 활성화
// - 스프링 MVC를 사용하는데 필요한 다양한 빈 설정 자동 생성

// WebMvcConfigurer 인터페이스
// - WebMvcConfigurer 인터페이스는 스프링 MVC의 개별 설정을 조정할 때 사용

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	// DispatcherServlet의 매핑 경로를 '/'로 주었을 때, JSP/HTML/CSS 등을 올바르게 처리하기 위한 설정
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// JSP를 이용해서 컨트롤러의 실행 결과를 보여주기 위한 설정
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/view/", ".jsp");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/main").setViewName("main");
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		ObjectMapper om = Jackson2ObjectMapperBuilder.json().featuresToEnable(SerializationFeature.INDENT_OUTPUT)
				.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter))
				.simpleDateFormat("yyyy-MM-dd HH:mm:ss").build();
		converters.add(0, new MappingJackson2HttpMessageConverter(om));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authCheckInterceptor()).addPathPatterns("/edit/**");
	}

	@Bean
	public AuthCheckInterceptor authCheckInterceptor() {
		return new AuthCheckInterceptor();
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasenames("message.label");
		ms.setDefaultEncoding("UTF-8");
		return ms;
	}

	@Bean
	public CommonExceptionHandler commonExceptionHandler() {
		return new CommonExceptionHandler();
	}
	
	@Bean
	public RestCommonExceptionHandler restCommonExceptionHandler() {
		return new RestCommonExceptionHandler();
	}

	// RegisterRequestValidator 객체를 글로벌 Validator로 사용
	// 해당 객체가 검증하는 RegisterRequest 타입에 @Valid 애노테이션 사용해 Validator 적용 가능
//	@Override
//	public Validator getValidator() {
//		return new RegisterRequestValidator();
//	}
}
