package com.myapp.chatgpt.data;

import com.myapp.chatgpt.data.infrastructure.dao.IUserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    @Test
    public void test_func(){
        String modelTypes = "glm-3-turbo,glm-4,glm-4v,cogview-3";
        String newModel = "glm-3.5-turbo";
//        String[] models = modelTypes.split(",");
//        List<String> list = new ArrayList<>(Arrays.asList(models));
//        if(!list.contains(newModel)){
//            list.add(newModel);
//        }
//        String join = String.join(",", list);
//        System.out.println(join);

        if(!modelTypes.contains(newModel)){
            if(modelTypes.isEmpty()){
                modelTypes = newModel;
            }else{
                modelTypes += ","+newModel;
            }
        }
        System.out.println(modelTypes);

    }
}
