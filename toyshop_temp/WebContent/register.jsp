<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="用户注册" scope="request" />
<jsp:include page="/common/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-6 col-lg-5">
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="card-title text-center mb-4">创建新账户</h2>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${errorMessage}" />
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.user}">
                     <div class="alert alert-warning" role="alert">
                        您已登录为 <strong><c:out value="${sessionScope.user.username}" /></strong>.
                        请 <a href="${pageContext.request.contextPath}/logout" class="alert-link">退出</a> 后再注册新账户，
                        或 <a href="${pageContext.request.contextPath}/index" class="alert-link">返回首页</a>。
                    </div>
                </c:if>

                <c:if test="${empty sessionScope.user}">
                    <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                        <div class="mb-3">
                            <label for="username" class="form-label">用户名:</label>
                            <input type="text" class="form-control" id="username" name="username" value="<c:out value="${param.username}"/>" required>
                            <div class="form-text">用户名长度建议在3-20个字符之间。</div>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密码:</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                            <div class="form-text">密码长度建议至少6个字符。</div>
                        </div>
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">确认密码:</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">注册</button>
                        </div>
                    </form>
                    <div class="mt-3 text-center">
                        <p>已有账户? <a href="${pageContext.request.contextPath}/login.jsp">立即登录</a></p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<%-- 可以添加一些简单的客户端JavaScript校验 --%>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('registerForm');
        if (form) {
            form.addEventListener('submit', function (event) {
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                const username = document.getElementById('username').value;

                if (username.length < 3 || username.length > 20) {
                    alert('用户名长度应在3到20个字符之间！');
                    event.preventDefault(); // 阻止表单提交
                    return;
                }

                if (password.length < 6) {
                    alert('密码长度至少需要6个字符！');
                    event.preventDefault(); // 阻止表单提交
                    return;
                }

                if (password !== confirmPassword) {
                    alert('两次输入的密码不一致！');
                    event.preventDefault(); // 阻止表单提交
                    return;
                }
            });
        }
    });
</script>

<jsp:include page="/common/footer.jsp" />
