<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link href="${pageContext.request.contextPath}/css/base.css" rel="stylesheet" type="text/css">
</head>

<body><div class="head">
	<div class="head_area">
		<div class="head_nav">
			<ul>
				<li><img src="${pageContext.request.contextPath}/images/nav_inc1.png" /><a href="${pageContext.request.contextPath}/toIndex" target="_parent">首页</a></li>
				<li><img src="${pageContext.request.contextPath}/images/nav_inc2.png" /><a href="#">成功案例</a></li>
				<li><img src="${pageContext.request.contextPath}/images/nav_inc3.png" /><a href="#">关于锐聘</a></li>
			</ul>
		</div>
		<div class="head_logo"><img src="${pageContext.request.contextPath}/images/head_logo.png" /></div>
		<div class="head_user">
		<c:if test="${sessionScope.applicantID==null }">
		
			<a href="${pageContext.request.contextPath}/login.jsp" target="_parent"><span class="type1">登录</span></a>
			<a href="${pageContext.request.contextPath}/register.jsp" target="_parent"><span class="type2">注册</span></a>
		</c:if>
		<c:if test="${sessionScope.applicantID!=null }">
			<a href="${pageContext.request.contextPath}/mycenter.jsp" target="_parent"><span class="type1">个人中心</span></a>
			<a href="${pageContext.request.contextPath}/ApplicantLogoutServlet"><span class="type2">退出登录</span></a>
		</c:if>
		</div>
		<div class="clear"></div>
	</div>
</div>

<div class="top_main">
	<div class="top_logo"><img src="${pageContext.request.contextPath}/images/main_logo.png" /></div>
	<div class="top_instr">提供岗前培训的IT职位</div>
	<div class="top_tel"><img src="${pageContext.request.contextPath}/images/it-phone.png" /></div>
	<div class="clear"></div>
</div>

<div class="clear"></div>

</body>
</html>
