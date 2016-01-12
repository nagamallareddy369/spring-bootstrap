package com.boostrap.sample;

import com.riversoft.weixin.qy.base.CorpSetting;
import com.riversoft.weixin.qy.oauth2.QyOAuth2s;
import com.riversoft.weixin.qy.oauth2.bean.QyUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AUTHOR: 819521
 * DATE  : 2016/1/12
 * TIME  : 10:41
 */
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CorpSetting corpSetting = new CorpSetting("wxf4774c756a351ff8", "FrP-Z44ewUnc766wAjWDEHIHHWzT7o_eGCsLKFQFToKTTXVRqwyVrBy4pupVpgVP");
        QyOAuth2s oAuth2s = QyOAuth2s.with(corpSetting);

        String code = request.getParameter("code");
        String state = request.getParameter("state");

        QyUser qyUser = oAuth2s.userInfo(code);
//        qyUser.
        System.out.println("code : " + code);
        response.getWriter().write("sid : " + request.getSession().getId() + "<br/>");
        response.getWriter().write("code : " + code + "<br/>");
        response.getWriter().write("state : " + state + "<br/>");
        response.getWriter().write("userId : " + qyUser.getUserId() + "<br/>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        super.doPost(request, response);
    }
}
