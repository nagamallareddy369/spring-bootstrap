<%
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
%>