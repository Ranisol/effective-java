package com.effectivejava.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;

@SpringBootApplication
public class EffectiveJavaApplication {

	public static void main(String[] args) 	{
		SpringApplication.run(EffectiveJavaApplication.class, args);
		BigInteger test = BigInteger.valueOf(1000);
	}

}
