package vn.intracom.chuongtrinhdaotao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "vn.intracom.chuongtrinhdaotao")
public class ChuongtrinhdaotaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChuongtrinhdaotaoApplication.class, args);
    }
}