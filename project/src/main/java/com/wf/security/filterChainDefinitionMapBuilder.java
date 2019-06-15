package com.wf.security;

import java.util.LinkedHashMap;

/**
 * @ClassName filterChainDefinitionMapBuilder
 * @Author 乔翰林
 * @Date 2019/3/1
 **/
public class filterChainDefinitionMapBuilder {

    public LinkedHashMap<String,String> builFilterChainDefinitionMap(){
        LinkedHashMap<String,String> map =new LinkedHashMap<String, String>();

        map.put("/toLogin.do","anon");
        map.put("/toRegister.do","anon");
        map.put("/login.do","anon");
        map.put("/register.do","anon");
        map.put("/login.jsp","anon");
        map.put("/register.jsp","anon");
        map.put("/logout.do","logout");
        map.put("/**","authc");

        return map;
    }
}
