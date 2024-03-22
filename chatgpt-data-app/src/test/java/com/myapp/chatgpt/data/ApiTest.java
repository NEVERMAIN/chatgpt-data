package com.myapp.chatgpt.data;

import com.myapp.chatgpt.data.infrastructure.dao.IUserAccountDao;
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
    private IUserAccountDao IUserAccountDao;

    @Test
    public void test_query(){
        UserAccountQuotaPo altman = IUserAccountDao.query("Altman");
        System.out.println(altman);
    }

    @Test
    public void test_subAccountQuota(){
        Integer res = IUserAccountDao.subAccountQuota("Altman");
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
        Integer account = IUserAccountDao.createAccount(userAccountQuotaPo);
        System.out.println(account);
    }
}
