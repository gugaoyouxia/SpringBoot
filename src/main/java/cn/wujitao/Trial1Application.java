package cn.wujitao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.wujitao.mapper")
public class Trial1Application {

    public static void main(String[] args) {
        SpringApplication.run(Trial1Application.class, args);
    }

}
