package com.boostrap.sample;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Author : yi.zeng
 * Date   : 2016/1/7
 * Time   : 22:44
 */
public class ToncentServlet extends HttpServlet {
    //企业号的基本信息，配置时填写
    String sToken = "8jRvcjG";
    String sCorpID = "wxf4774c756a351ff8";
    String sEncodingAESKey = "N1XM5YHIRS8Tcwsd8ctObavzqiruvxfT4WwotHhXGfQ";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        System.out.println("request = " + request);

        response.setContentType("text/html;charset=utf-8");
        //response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        Map parameterMap = request.getParameterMap();
        Set keySet = parameterMap.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
            String paramName = (String) iterator.next();
            String paramValue = request.getParameter(paramName);

            System.out.println(paramName + " = " + paramValue);
        }

        System.out.println("=========================doGet1");


        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);

            System.out.println("=========================doGet2");

            // 解析出url上的参数值如下：
            //URLDecoder.decode(request.getParameter("echostr"),"utf-8");
            String sVerifyMsgSig = URLDecoder.decode(request.getParameter("msg_signature"), "utf-8");
            String sVerifyTimeStamp = URLDecoder.decode(request.getParameter("timestamp"), "utf-8");
            String sVerifyNonce = URLDecoder.decode(request.getParameter("nonce"), "utf-8");
            String sVerifyEchoStr = URLDecoder.decode(request.getParameter("echostr"), "utf-8");

            System.out.println("sVerifyMsgSig = " + sVerifyMsgSig);
            System.out.println("sVerifyTimeStamp = " + sVerifyTimeStamp);
            System.out.println("sVerifyNonce = " + sVerifyNonce);
            System.out.println("sVerifyEchoStr = " + sVerifyEchoStr);

            PrintWriter out = response.getWriter();
            String sEchoStr; //需要返回的明文
            try {
                sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
                System.out.println("verifyurl echostr: " + sEchoStr);
                // 验证URL成功，将sEchoStr返回
                out.print(sEchoStr);
                out.close();
            } catch (Exception e) {
                //验证URL失败，错误原因请查看异常
                e.printStackTrace();
            }

        } catch (AesException e1) {
            e1.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("#############################1");

        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        //从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();
        String postData = IOUtils.toString(inputStream, "UTF-8");

        String msg = "";
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
            //解密消息
            msg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);
        } catch (AesException e) {
            e.printStackTrace();
        }
        System.out.println("msg=" + msg);

        // 调用核心业务类接收消息、处理消息
//        String respMessage = CoreService.processRequest(msg);
//        System.out.println("respMessage=" + respMessage);
        String xml = "<xml>\n" +
                "   <ToUserName><![CDATA[ZengYi]]></ToUserName>\n" +
                "   <FromUserName><![CDATA[wxf4774c756a351ff8221]]></FromUserName> \n" +
                "   <CreateTime>1348831860</CreateTime>\n" +
                "   <MsgType><![CDATA[text]]></MsgType>\n" +
                "   <Content><![CDATA[this is a test<br><a href='www.toncentsoft.com'>详情</a>]]></Content>\n" +
                "</xml>";
        System.out.println("xml=" + xml);

        String encryptMsg = "";
        try {
            //加密回复消息
            encryptMsg = wxcpt.EncryptMsg(xml, timestamp, nonce);
        } catch (AesException e) {
            e.printStackTrace();
        }

        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(encryptMsg);
        out.close();

    }


}
