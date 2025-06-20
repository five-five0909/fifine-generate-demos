<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Q_ITOffer锐聘网后台管理系统</title>
<link href="../css/left.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="../js/jquery.js"></script>
<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('ul').slideUp();
		if($ul.is(':visible')){
			$(this).next('ul').slideUp();
		}else{
			$(this).next('ul').slideDown();
		}
	});
})	
</script>
</head>
<body style="background:#f0f9fd;">
<div class="lefttop"><span></span>功能菜单</div>
<dl class="leftmenu">
  <%--仅当用户角色为管理员或企业用户时可查看 --%>
  <c:if test="${sessionScope.SESSION_USER.userRole == 3 ||  sessionScope.SESSION_USER.userRole == 2}">
  <dd>
    <div class="title"> <span><img src="../images/leftico01.png" /></span>企业职位管理</div>
    <ul class="menuson">
      <li><cite></cite><a href="jobApplyList.jsp" target="rightFrame">职位申请查看</a><i></i></li>
      <li><cite></cite><a href="../JobServlet?type=list" target="rightFrame">职位管理</a><i></i></li>
      <li><cite></cite><a href="../CompanyServlet?type=list" target="rightFrame">企业管理</a><i><i></i></li>
    </ul>
  </dd>
  </c:if>
  <%--所有登录用户角色均可查看 --%>
  <c:if test="${sessionScope.SESSION_USER.userRole == 1 ||  sessionScope.SESSION_USER.userRole == 2 ||  sessionScope.SESSION_USER.userRole == 3}">
  <dd>
    <div class="title"> <span><img src="../images/leftico02.png" /></span>简历管理</div>
    <ul class="menuson">
      <li><cite></cite><a href="../ResumeServlet?type=list" target="rightFrame">简历查询</a><i></i></li>
    </ul>
  </dd>
  </c:if>
<!--   仅系统管理员角色可查看 -->
  <c:if test="${sessionScope.SESSION_USER.userRole == 3 }">
  <dd>
    <div class="title"><span><img src="../images/leftico03.png"/></span>用户管理</div>
  	<ul class="menuson">
      <li><cite></cite><a href="userList.jsp" target="rightFrame">用户管理</a><i></i></li>
    </ul>
  </dd>
  <dd>
    <div class="title"><span><img src="../images/leftico04.png" /></span>系统管理</div>
    <ul class="menuson">
      <li><cite></cite><a href="userOnline.jsp" target="rightFrame">在线用户</a><i></i></li>
    </ul>
  </dd>
  </c:if>
  <%--所有用户角色均可查看 --%>
  <dd>
    <!-- <div class="title"><span><img src="../images/leftico04.png" /></span> -->
    <!-- <a href="#" target="rightFrame">密码修改</a></div> -->
  </dd>
</dl>
</body>
</html>
