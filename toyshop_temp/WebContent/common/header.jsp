<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- 引入 JSTL fmt 库，如果需要格式化日期或数字 --%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> --%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? '儿童玩具售卖系统' : pageTitle}</title>
    <!-- Bootstrap 5 CDN CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <!-- 自定义 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <script>
        // 全局的 contextPath，方便 JavaScript 中使用
        const contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index">儿童玩具城</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link <c:if test="${fn:endsWith(pageContext.request.requestURI, '/index') || fn:endsWith(pageContext.request.requestURI, '/toyList')}">active</c:if>" aria-current="page" href="${pageContext.request.contextPath}/index">首页</a>
                </li>
                <c:if test="${not empty sessionScope.user}">
                    <%-- 用户已登录 --%>
                    <c:if test="${sessionScope.user.role == 1}"> <%-- 普通用户 --%>
                        <%-- 普通用户特定的导航项，例如“我的订单”等，目前没有 --%>
                    </c:if>
                    <c:if test="${sessionScope.user.role == 2}"> <%-- 管理员 --%>
                        <li class="nav-item">
                            <a class="nav-link <c:if test="${fn:startsWith(pageContext.request.requestURI, pageContext.request.contextPath  + '/admin')}">active</c:if>" href="${pageContext.request.contextPath}/admin/manage">后台管理</a>
                        </li>
                    </c:if>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <%-- 用户已登录 --%>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownUser" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                欢迎, ${sessionScope.user.username}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownUser">
                                <%-- <li><a class="dropdown-item" href="#">个人资料</a></li> --%>
                                <%-- <li><hr class="dropdown-divider"></li> --%>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">退出登录</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <%-- 用户未登录 --%>
                        <li class="nav-item">
                            <a class="nav-link <c:if test="${fn:endsWith(pageContext.request.requestURI, '/login.jsp')}">active</c:if>" href="${pageContext.request.contextPath}/login.jsp">登录</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link <c:if test="${fn:endsWith(pageContext.request.requestURI, '/register.jsp')}">active</c:if>" href="${pageContext.request.contextPath}/register.jsp">注册</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <%-- 主体内容开始 --%>
