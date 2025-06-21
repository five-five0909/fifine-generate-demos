<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <%-- 这个meta标签是正确的，它告诉浏览器使用UTF-8解码 --%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/base.css" rel="stylesheet" type="text/css" />
    <%-- 注意：因为这个文件被 mycenter.jsp 引入，所以CSS路径应该是相对于mycenter.jsp的 --%>
    <link href="${pageContext.request.contextPath}/css/resume.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/myapplys.css" rel="stylesheet" type="text/css" />
    <title>我的申请</title>
</head>
<body>
<div class="tn-grid resume-container">
    <div class="resume-body" style="border: none; padding: 0;">
        <div id="panel-apply" class="tab-content active">
            <div class="it-content-box it-person">
                <div class="tn-box-content">
                    <div class="tn-tabs-panel tn-widget-content">
                        <c:if test="${empty jobList }">
                            <div style="text-align: center; margin: auto; padding: 100px 0;">
                                <h1>抱歉，您暂未申请任何职位!</h1>
                            </div>
                        </c:if>
                        <c:if test="${!empty jobList }">
                            <form action="#" method="post" id="Form-Apply">
                                <table class="tn-table-grid">
                                    <tbody>
                                    <tr class="tn-table-grid-header">
                                        <th class="tn-width-check"><input type="checkbox" class="tn-checkbox" id="selectAll" name="selectall"></th>
                                        <th>企业名称</th>
                                        <th class="it-text-ctnter">职位名称</th>
                                        <th class="it-text-ctnter">申请状态</th>
                                        <th>操作</th>
                                    </tr>
                                    <c:forEach items="${jobList}" var="jobApply">
                                        <tr class="tn-table-grid-row">
                                            <td class="tn-width-check"><input type="checkbox" class="tn-checkbox tnui-apply" value="${jobApply.applyId }" name="applyIdList"></td>
                                            <td class="tn-width-auto"><a title="${jobApply.job.company.companyName }" href="${pageContext.request.contextPath}/CompanyServlet?type=select&id=${jobApply.job.company.companyId }" target="_blank"> ${jobApply.job.company.companyName }</a></td>
                                            <th class="tn-width-pic-mini"><a title="${jobApply.job.jobName }" href="${pageContext.request.contextPath}/JobServlet?type=select&jobid=${jobApply.job.jobId}" target="_blank"> ${jobApply.job.jobName }</a></th>
                                            <td class="tn-width-category"><div class="tn-instructions">
                                                <div class="it-instructions-tit">
                                                    <span style="width: 26px">申请</span><span style="width: 132px">审核</span><span style="width: 132px">通知</span>
                                                </div>
                                                <div class="tn-progress-bar tn-widget-content tn-corner-all">
                                                    <div style="width: 50%;" class="tn-progress-bar-value tn-widget-header tn-corner-left tn-border-tbl"></div>
                                                </div>
                                                <ul class="tn-helper-clearfix tn-step-scale"></ul>
                                            </div></td>
                                            <td class="tn-width-action2"><span class="tn-action">
                                                    <a class="tn-action tn-action-text-icon tnui-apply-delete" href="${pageContext.request.contextPath}/JobServlet?type=delete&applyId=${jobApply.applyId }"> <span class="tn-icon it-icon-delete"></span><span class="tn-action-text">删除</span></a>
                                                </span></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
</body>
</html>