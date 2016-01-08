<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%
        Map parameterMap = request.getParameterMap();
        Set set = parameterMap.entrySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " = " + value);
        }

%>