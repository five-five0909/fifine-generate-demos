<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="用户登录" scope="request" />
<jsp:include page="/common/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-6 col-lg-4">
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="card-title text-center mb-4">用户登录</h2>

                <c:if test="${not empty param.registrationSuccess}">
                    <div class="alert alert-success" role="alert">
                        注册成功！现在您可以登录了。
                    </div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${errorMessage}" />
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.user}">
                     <div class="alert alert-warning" role="alert">
                        您已登录为 <strong><c:out value="${sessionScope.user.username}" /></strong>.
                        <a href="${pageContext.request.contextPath}/logout" class="alert-link">点此退出</a> 后重新登录。
                        或者 <a href="${pageContext.request.contextPath}/index" class="alert-link">返回首页</a>。
                    </div>
                </c:if>

                <c:if test="${empty sessionScope.user}">
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">用户名:</label>
                            <input type="text" class="form-control" id="username" name="username" value="<c:out value="${param.username}"/>" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密码:</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">登录</button>
                        </div>
                    </form>
                    <div class="mt-3 text-center">
                        <p>还没有账户? <a href="${pageContext.request.contextPath}/register.jsp">立即注册</a></p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
