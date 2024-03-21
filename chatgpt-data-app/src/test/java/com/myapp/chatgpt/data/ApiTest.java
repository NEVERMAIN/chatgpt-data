package com.myapp.chatgpt.data;
import java.util.Date;

import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
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
    public void test_query(){
        UserAccountQuotaPo altman = userAccountDao.query("Altman");
        System.out.println(altman);
    }

    @Test
    public void test_subAccountQuota(){
        Integer res = userAccountDao.subAccountQuota("Altman");
        System.out.println(res);
    }

    @Test
    public void test_create(){
        UserAccountQuotaPo userAccountQuotaPo = new UserAccountQuotaPo();
        userAccountQuotaPo.setOpenid("altman");
        userAccountQuotaPo.setTotalQuota(0);
        userAccountQuotaPo.setSurplusQuota(0);
        userAccountQuotaPo.setModelTypes("");
        userAccountQuotaPo.setStatus(0);
        Integer account = userAccountDao.createAccount(userAccountQuotaPo);
        System.out.println(account);
    }
}
