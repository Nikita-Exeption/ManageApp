package org.nikita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class ManagerApp implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApp.class, args);
    }

    @Bean
    LocaleResolver resolver(){
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }

//    @Bean
//    LocaleChangeInterceptor interceptor(){
//        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
//        interceptor.setParamName("lang");
//        return interceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interceptor());
//    }
}
