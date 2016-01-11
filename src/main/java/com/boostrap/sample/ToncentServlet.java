package com.boostrap.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.riversoft.weixin.common.decrypt.MessageDecryption;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.common.message.xml.TextXmlMessage;
import com.riversoft.weixin.common.util.XmlObjectMapper;
import com.riversoft.weixin.qy.base.AgentSetting;
import com.riversoft.weixin.qy.base.DefaultSettings;
import com.riversoft.weixin.qy.message.QyXmlMessages;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
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
    private static Logger logger = LoggerFactory.getLogger(ToncentServlet.class);


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

    private String wapperMessage(XmlMessageHeader xmlRequest) throws JsonProcessingException {

        logger.info(xmlRequest.getFromUser());
        logger.info(xmlRequest.getToUser());
        logger.info(xmlRequest.getMsgType().name());
        logger.info(xmlRequest.getCreateTime() + "");
        TextXmlMessage textXmlMessage = new TextXmlMessage();
        textXmlMessage.setFromUser(xmlRequest.getToUser());
        textXmlMessage.setToUser(xmlRequest.getFromUser());
        textXmlMessage.setCreateTime(new Date());
        textXmlMessage.setContent("有趣的测试");

        return XmlObjectMapper.defaultMapper().toXml(textXmlMessage);
//        xmlRequest.

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = URLDecoder.decode(request.getParameter("msg_signature"), "utf-8");
        String timestamp = URLDecoder.decode(request.getParameter("timestamp"), "utf-8");
        String nonce = URLDecoder.decode(request.getParameter("nonce"), "utf-8");
        String echostr = URLDecoder.decode(request.getParameter("echostr"), "utf-8");

        logger.info("msg_signature={}, nonce={}, timestamp={}, echostr={}", signature, nonce, timestamp, echostr);

        AgentSetting agentSetting = DefaultSettings.defaultSettings().getAgentSetting();
        String corpId = DefaultSettings.defaultSettings().getCorpSetting().getCorpId();
        String result = echostr;

        try {
            MessageDecryption messageDecryption = new MessageDecryption(agentSetting.getToken(), agentSetting.getAesKey(), corpId);
            if (!StringUtils.isEmpty(echostr)) {
                String echo = messageDecryption.decryptEcho(signature, timestamp, nonce, echostr);
                logger.info("消息签名验证成功.");
                result =  echo;
            } else {
                //从请求中读取整个post数据
                InputStream inputStream = request.getInputStream();
                String postData = IOUtils.toString(inputStream, "UTF-8");

                XmlMessageHeader xmlRequest = QyXmlMessages.fromXml(messageDecryption.decrypt(signature, timestamp, nonce, postData));
                result = wapperMessage(xmlRequest);

            }
        } catch (Exception e) {
            logger.error("callback failed.", e);
        }

        response.getWriter().write(result);
        response.getWriter().close();

    }


}
