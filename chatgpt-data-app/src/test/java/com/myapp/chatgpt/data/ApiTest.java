package com.myapp.chatgpt.data;

import com.myapp.chatgpt.data.infrastructure.dao.UserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@SpringBootTest
public class ApiTest {

    @Autowired
    private UserAccountDao userAccountDao;

    @Test
    public void test(){
        UserAccountQuotaPo altman = userAccountDao.query("Altman");
        System.out.println(altman);
    }

    @Test
    public void test02(){
        Integer res = userAccountDao.subAccountQuota("Altman");
        System.out.println(res);
    }
}
