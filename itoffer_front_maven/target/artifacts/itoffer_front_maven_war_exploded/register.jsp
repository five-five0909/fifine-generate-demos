<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册 - 锐聘网</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/register.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
    function validate() {
        var email = document.getElementById("email");
        var password = document.getElementById("password");
        var agree = document.getElementById("agree");
        var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;

        if (email.value == "") {
            alert("邮箱不能为空！");
            email.focus();
            return false;
        } else if (!pattern.test(email.value)) {
            alert("请输入正确的邮箱格式！");
            email.focus();
            return false;
        }
        if (password.value == "") {
            alert("密码不能为空！");
            password.focus();
            return false;
        }  
        if (password.value.length<6 || password.value.length>12) {
            alert("密码长度不符合要求，请输入6-12位密码!");
            password.focus();
            return false;
        }
        if (!agree.checked) {
            alert("请先同意本站服务条款！");
            return false;
        }
        return true;
    }
</script>
</head>

<body>
<jsp:include page="top.jsp"></jsp:include>
<div class="content">
  <div class="page_name">注册</div>
  <div class="login_content">
  <form action="${pageContext.request.contextPath}/ApplicantRegisterServlet"  method="post" onsubmit="return validate();">
    <div class="login_l">
      <div class="span1">
        <label class="tn-form-label">邮箱：</label>
        <input  class="tn-textbox" type="text" name="email" id="email">
      </div>
      <div class="span1">
        <label class="tn-form-label">密码：</label>
        <input class="tn-textbox"  type="password" name="password" id="password">
      </div>
     <div class="tn-form-row-button">
        <div class="span1">
         <input name="submit" type="submit" class="tn-button-text" value="立即注册">
          <p class="it-register-text">
            <input  class="tn-checkbox"  type="checkbox" id="agree">
            <label > 同意本站服务条款</label>
            <a href="javascript:;">查看</a> </p>
        </div>
      </div>
      <div class="clear"></div>
    </div>
    </form>
    <div class="register_r">
     <p align="center"><br><br>
                    <b>已有帐号？</b><a href="${pageContext.request.contextPath}/login.jsp">登录</a></p>
    
      <div><img src="${pageContext.request.contextPath}/images/reg_pic.jpg"></div>
    
      <div class="clear"></div>
    </div>
    <div class="clear"></div>
  </div>
</div>
<iframe src="${pageContext.request.contextPath}/foot.jsp" width="100%" height="150"  scrolling="no" frameborder="0" ></iframe>
</body>
</html>
