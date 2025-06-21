<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录 - 锐聘网</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
	function validate() {
		var email = document.getElementById("email");
		var password = document.getElementById("password");
		if (email.value == "") {
			alert("邮箱不能为空！");
			email.focus();
			return false;
		}
		if (password.value == "") {
			alert("密码不能为空！");
			password.focus();
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<jsp:include page="top.jsp"></jsp:include>

	<div class="content">
		<div class="page_name">登录</div>
		<div class="login_content">
			<form action="${pageContext.request.contextPath}/ApplicantLoginServlet" method="post"
				onsubmit="return validate();">
				<!-- 从拦截器中获取被拦截前的请求地址 -->
				<input type="hidden" name="requestPath"
					value="${requestScope.requestPath }">
				<div class="login_l">
					<div class="span1">
						<label class="tn-form-label">邮箱：</label> <input class="tn-textbox"
							type="text" name="email" id="email">
					</div>
					<div class="span1">
						<label class="tn-form-label">密码：</label> <input class="tn-textbox"
							type="password" name="password" id="password">
					</div>
					<div class="tn-form-row-button">
						<div class="span1">
							<input name="submit" type="submit" class="tn-button-text"
								value="登录">
						</div>
					</div>
					<div class="clear"></div>
				</div>
			</form>

			<div class="clear"></div>
		</div>
	</div>
	<iframe src="${pageContext.request.contextPath}/foot.jsp" width="100%" height="150" scrolling="no"
		frameborder="0"></iframe>
</body>
</html>