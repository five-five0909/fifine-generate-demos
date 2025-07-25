<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户在线列表</title>
<link href="../css/manageadmin.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="place"> <span>位置：</span>
  <ul class="placeul">
<li><a href="index.html">首页</a></li><li>用户在线列表</li>
  </ul>
</div>
<div class="rightinfo">
  <table class="imgtable">
    <thead>
      <tr>
        <th ><input name="" type="checkbox" value="" checked="checked"/></th>
        <th>用户登录名</th>
        <th>用户真实姓名</th>
        <th>用户角色</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${applicationScope.ONLINE_USER}" var="usermap">
      <tr height="50px">
        <td>${usermap.key}</td>
        <td>${usermap.value.userRealname}</td>
        <td><c:if test="${usermap.value.userRole==1}">普通用户</c:if>
        <c:if test="${usermap.value.userRole==2}"> 企业管理员</c:if>
        <c:if test="${usermap.value.userRole==3}">系统管理员</c:if></td>
        <td><a 
        href="../UserServlet?type=forceLogout&userId=${usermap.value.userId }" 
        class="tablelink">强制下线</a></td>
      </tr>
     </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>