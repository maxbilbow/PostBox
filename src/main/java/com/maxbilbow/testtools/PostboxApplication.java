package com.maxbilbow.testtools;

import click.rmx.web.Browser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan
public class PostboxApplication
{

  public static void main(String[] args)
  {
    ConfigurableApplicationContext context = SpringApplication.run(PostboxApplication.class, args);
    String port = context.getEnvironment().getProperty("server.port");
    new Browser().launch(Integer.valueOf(port));
  }
}
