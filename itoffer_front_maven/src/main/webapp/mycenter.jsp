<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>我的中心 - 锐聘网</title>
    <link href="css/base.css" type="text/css" rel="stylesheet"/>
    <link href="css/resume.css" type="text/css" rel="stylesheet"/>
    <link href="css/myapplys.css" type="text/css" rel="stylesheet"/>
    <style>
        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }
    </style>
</head>
<body>
<jsp:include page="top.jsp"></jsp:include>
<%@ include file="common/resume_tabs.jsp" %>
<div style="width:100%;height:900px;">
    <iframe id="resume-iframe" src="ResumeServlet?iframe=resume" width="100%" height="100%" frameborder="0">
    </iframe>
</div>
<script>
    function showTab(tab) {
        var iframe = document.getElementById('resume-iframe');
        if (tab === 'resume') {
            iframe.src = 'ResumeServlet?iframe=resume';
            iframe.title = '我的简历';
        } else {
            iframe.src = 'JobApplyServlet?type=myapply&iframe=apply';
            iframe.title = '我的申请';
        }
        // 动态高亮tab
        var lis = document.querySelectorAll('.resume-nav li');
        lis.forEach(function (li) {
            li.classList.remove('active');
        });
        document.querySelector('.resume-nav li' + (tab === 'resume' ? ':first-child' : ':last-child')).classList.add('active');
    }

    // 根据url参数自动切换tab和iframe
    (function () {
        var tab = (new URLSearchParams(window.location.search)).get('tab');
        if (tab === 'apply') {
            showTab('apply');
        } else {
            showTab('resume');
        }
    })();
</script>
<iframe src="foot.jsp" width="100%" height="150" scrolling="no" frameborder="0"></iframe>
</body>
</html> 