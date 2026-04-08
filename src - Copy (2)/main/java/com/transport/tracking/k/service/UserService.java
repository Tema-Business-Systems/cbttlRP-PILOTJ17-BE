package com.transport.tracking.k.service;

import com.sun.istack.NotNull;
import com.transport.tracking.model.User;
import com.transport.tracking.repository.UserRepository;
import com.transport.tracking.response.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${transport.path}")
    private String contextPath;

    public UserVO login(@NotNull UserVO userVO, HttpServletResponse response) {
        //User user = this.userRepository.findByXusrnameAndXpswd(userVO.getXusrname(), userVO.getXpswd());

//        String xlogin = userVO.getXlogin().toLowerCase();
//        //User user = this.userRepository.findByXusrnameAndXpswd(userVO.getXusrname(), userVO.getXpswd());
//        User user = this.userRepository.findByXloginAndXpswdAndXact(xlogin, userVO.getXpswd(),2);
//
//
//        if(Objects.isNull(user)) {
//
//            return null;
//        }
//        userVO = new UserVO();
//        userVO.setXusrname(user.getXusrname());
//        userVO.setXlogin(user.getXlogin());
//        //Set<Role> roles = user.getRoles();
//
//        List<String> permissions = new ArrayList();
//        permissions.add("Admin");
//
//        //System.out.println(user.getRoles());
//
//        userVO.setAccessToken(this.tokenService.generateAccessToken(permissions, user.getXusrname()));
//      //  userVO.setSite(user.getSite());
//
//        try {
//            response.addCookie(this.generateCookie("token", userVO.getAccessToken()));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userVO;




        User user = this.userRepository.findByXloginAndXpswd(userVO.getXlogin(), userVO.getXpswd());
//        User user = this.userRepository.findByXloginAndXpswdAndXact(userVO.getXlogin(), userVO.getXpswd(),2);
        if(Objects.isNull(user)) {

            User tempuser = userRepository.findByXlogin(userVO.getXlogin());

            if(Objects.isNull(tempuser)) {
                throw new RuntimeException("User doesn't exist");
            }
            else {

                throw new RuntimeException("Username and password are incorrect");
            }
        }
        if(user.getXact()!=2) {
            throw new RuntimeException("The User is currently inactive. Unable to login");
        }
        userVO = new UserVO();
        userVO.setXusrname(user.getXusrname());
        userVO.setXlogin(user.getXlogin());

        userVO.setXact(user.getXact());
        //Set<Role> roles = user.getRoles();

        List<String> permissions = new ArrayList();
        permissions.add("Admin");

        //System.out.println(user.getRoles());

        userVO.setAccessToken(this.tokenService.generateAccessToken(permissions, user.getXusrname()));
        //  userVO.setSite(user.getSite());

        try {
            response.addCookie(this.generateCookie("token", userVO.getAccessToken()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return userVO;

    }

    private Cookie generateCookie(final String key, final String value) {
        final Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
        cookie.setPath(contextPath);
        return cookie;
    }

    public void logOut(HttpServletResponse response) {
        final Cookie cookie = new Cookie("token", "value");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath(contextPath);
        try {
            response.addCookie(cookie);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
