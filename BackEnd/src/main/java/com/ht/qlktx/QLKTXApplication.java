package com.ht.qlktx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class QLKTXApplication {

	public static void main(String[] args) {
		// Enable this script if enter the error TLS version
		String disabledAlgorithms = Security.getProperty("jdk.tls.disabledAlgorithms");
		disabledAlgorithms = disabledAlgorithms.replace("TLSv1, TLSv1.1,", "");
		Security.setProperty("jdk.tls.disabledAlgorithms", disabledAlgorithms);
		//
		SpringApplication.run(QLKTXApplication.class, args);
	}
}