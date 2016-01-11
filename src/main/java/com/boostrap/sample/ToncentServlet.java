package com.boostrap.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.riversoft.weixin.common.decrypt.MessageDecryption;
import com.riversoft.weixin.common.event.ClickEvent;
import com.riversoft.weixin.common.message.MsgType;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.common.message.xml.TextXmlMessage;
import com.riversoft.weixin.common.util.XmlObjectMapper;
import com.riversoft.weixin.qy.event.QyClickEvent;
import com.riversoft.weixin.qy.message.QyXmlMessages;
import com.riversoft.weixin.qy.message.json.TextMessage;
import com.riversoft.weixin.qy.request.QyTextRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
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
//    private static Logger logger = LoggerFactory.getLogger(ToncentServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        //response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        System.out.println("=========================doGet");
        System.out.println("request.getSession().getId() = " + request.getSession().getId());
        Map parameterMap = request.getParameterMap();
        Set keySet = parameterMap.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
            String paramName = (String) iterator.next();
            String paramValue = request.getParameter(paramName);

            System.out.println(paramName + " = " + paramValue);
        }


        //从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();
        String postData = IOUtils.toString(inputStream);

        System.out.println("postData = " + postData);


        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);


            // 解析出url上的参数值如下：
            //request.getParameter("echostr"),"utf-8");
            String sVerifyMsgSig = request.getParameter("msg_signature");
            String sVerifyTimeStamp = request.getParameter("timestamp");
            String sVerifyNonce = request.getParameter("nonce");
            String sVerifyEchoStr = request.getParameter("echostr");

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

    private XmlMessageHeader wapperMessage(XmlMessageHeader xmlRequest) throws JsonProcessingException {

        System.out.println(xmlRequest.getFromUser());
        System.out.println(xmlRequest.getToUser());
        System.out.println(xmlRequest.getMsgType().name());
        System.out.println(xmlRequest.getCreateTime() + "");

        if (MsgType.event.equals(xmlRequest.getMsgType()) && xmlRequest instanceof ClickEvent) {
            ClickEvent clickEvent = (ClickEvent)xmlRequest;
            String eventKey = clickEvent.getEventKey();
            if ("task".equals(eventKey)) {

            }
        }
        TextXmlMessage textXmlMessage = new TextXmlMessage();
        textXmlMessage.setFromUser(xmlRequest.getToUser());
        textXmlMessage.setToUser(xmlRequest.getFromUser());
        textXmlMessage.setCreateTime(new Date());
        textXmlMessage.setContent("有趣的测试");

        return textXmlMessage;
//        xmlRequest.

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("request.getSession().getId() = " + request.getSession().getId());


        try {
            String signature = request.getParameter("msg_signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");


            String result = "";


            MessageDecryption messageDecryption = new MessageDecryption(sToken, sEncodingAESKey, sCorpID);
            if (!StringUtils.isEmpty(echostr)) {
                result = messageDecryption.decryptEcho(signature, timestamp, nonce, echostr);
                System.out.println("消息签名验证成功.");
            } else {
                //从请求中读取整个post数据
                InputStream inputStream = request.getInputStream();
                String postData = IOUtils.toString(inputStream);

                XmlMessageHeader xmlRequest = QyXmlMessages.fromXml(messageDecryption.decrypt(signature, timestamp, nonce, postData));
                XmlMessageHeader textXmlMessage = wapperMessage(xmlRequest);
                String xml = XmlObjectMapper.defaultMapper().toXml(textXmlMessage);

                result = messageDecryption.encrypt(xml, String.valueOf(textXmlMessage.getCreateTime().getTime() / 1000), nonce);

            }

            response.getWriter().write(result);
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
