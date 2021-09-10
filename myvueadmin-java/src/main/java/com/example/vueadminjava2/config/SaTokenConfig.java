package com.example.vueadminjava2.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-08-29 16:03
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer{
    @Autowired
    private HttpServletRequest request;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
//        为方便接口测试，暂时关闭，等最后都完成了再开启路由鉴权
//        registry.addInterceptor(new SaRouteInterceptor()).addPathPatterns("/**").excludePathPatterns("/captcha","/login");
    }

//     注册 [Sa-Token全局过滤器]，如果未登录返回401，前端根据401code跳转到登录页面
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/*")
                .addExclude("/login", "/captcha")
                .setAuth(r -> {
                    //axios对复杂跨域请求有一个预检请求，请求方法为options，且不会携带token，下面判断要对options方法放行
                    if(!request.getMethod().equalsIgnoreCase("options") && StpUtil.isLogin() == false) {
                        // 与前端约定好，code=401时代表会话未登录
                        System.out.println("not login");
                        SaRouter.back(SaResult.ok().setCode(401));
                    }
                });
    }

}
