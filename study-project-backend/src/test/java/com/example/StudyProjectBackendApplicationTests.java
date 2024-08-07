package com.example;

//import com.example.entity.Account;
//import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class StudyProjectBackendApplicationTests {
//    @Resource
//    UserMapper mapper;
    //$2a$10$UmuMl6KsfcJeljQzNufudeYl.r48uoRDrUDFJoGOg.KMYX3n9Hujq
    //$2a$10$o9xbbMTr13CI8C9EM15XCuY2TxPC50z/MDKlKEi/uVq/zjAWiKIVK
    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
//        Account account = mapper.findAccountByIdOrEmail("admin");
//        System.out.println(account.toString());
    }
}
