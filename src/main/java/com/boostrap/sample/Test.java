package com.boostrap.sample;

import com.riversoft.weixin.common.util.URLEncoder;
import com.riversoft.weixin.qy.agent.Agents;
import com.riversoft.weixin.qy.agent.bean.Agent;
import com.riversoft.weixin.qy.contact.Departments;
import com.riversoft.weixin.qy.contact.Users;
import com.riversoft.weixin.qy.contact.department.Department;
import com.riversoft.weixin.qy.contact.user.ReadUser;
import com.riversoft.weixin.qy.oauth2.QyOAuth2s;

import java.util.List;

/**
 * AUTHOR: 819521
 * DATE  : 2016/1/11
 * TIME  : 11:55
 */
public class Test {
    public static void main(String[] args) {


        String url = QyOAuth2s.defaultOAuth2s().authenticationUrl("www.toncentsoft.com", "snsapi_userinfo");
        System.out.println(url);
//        QyUser qyUser = oAuth2s.userInfo("4a43605ac31817e87c7fb80be7424406");
//        System.out.println("qyUser = " + qyUser);
        ReadUser user = Users.defaultUsers().get("ZengYi");
        int[] departments = user.getDepartment();
        System.out.println("user = " + user);

        for (int i = 0; i < departments.length; i++) {
            int departmentId = departments[i];

            List<Department> departmentList = Departments.defaultDepartments().list(departmentId);
            for (int j = 0; j < departmentList.size(); j++) {
                Department department = departmentList.get(j);
                System.out.println("department = " + department);

            }
        }


//        Map<String, String> map = QyOAuth2s.with(corpSetting).toOpenId(5, "ZengYi");
//        String appId = map.get("appid");
//        String openId = map.get("openid");
//        System.out.println("appId = " + appId);
//        System.out.println("openId = " + openId);

        String encode = URLEncoder.encode("www.toncentsoft.cn/user.jsp");
        System.out.println("encode = " + encode);

        List<Agent> list = Agents.defaultAgents().list();
        System.out.println("list = " + list);
        Agent agent = Agents.defaultAgents().get(5);
        System.out.println("agent = " + agent);
    }
}
