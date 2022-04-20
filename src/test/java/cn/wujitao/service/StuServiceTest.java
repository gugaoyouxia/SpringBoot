package cn.wujitao.service;
import java.util.Date;

import cn.wujitao.model.domain.Stu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 * @author wujitao
 */
@SpringBootTest
class StuServiceTest {
    @Resource
    private StuService stuService;

    @Test
    void testAddUser(){
        Stu stu = new Stu();
        stu.setUsername("testUser");
        stu.setUserAccount("123");
        stu.setAvatarUrl("https://baomidou.com/img/logo.svg");
        stu.setGender(0);
        stu.setUserPassword("123456");
        stu.setEmail("123456");
        stu.setUserStatus(0);
        stu.setCreateTime(new Date());
        stu.setUpdateTime(new Date());
        stu.setIsDelete(0);
        boolean result = stuService.save(stu);
        System.out.println(stu.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        long result = stuService.userRegister("justTest", "12345678", "12345678");
        Assertions.assertTrue(result > 0);
    }
}