<%
    String sid = request.getSession().getId();
    System.out.println("sid = " + request.getSession().getId());
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf4774c756a351ff8&redirect_uri=www.toncentsoft.cn%2Fuser&response_type=code&scope=snsapi_base&state="+ sid +"#wechat_redirect";
    response.sendRedirect(url);
%>



