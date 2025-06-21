<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>企业详情 - ${ company.companyName}</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/company.css" type="text/css" rel="stylesheet" />
<style>
	.tn-button-disabled {
		background: #ccc !important;
		border-color: #bbb !important;
		color: #888 !important;
		cursor: not-allowed;
	}
</style>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<div class="tn-grid">
		<div class="bottomban">
			<div class="bottombanbox">
				<img src="${pageContext.request.contextPath}/recruit/images/${company.companyPic}" />
			</div>
		</div>

	</div>
	<div class="tn-grid">
		<div class="tn-widget-content">
			<div class="tn-box-content">
				<div class="tn-helper-clearfix">
					<div class="it-main2">
						<div class="it-ctn-heading">
							<div class="it-title-line">
								<span> <em> ${company.companyViewnum } </em> 浏览
								</span>
								<h3>企业简介</h3>
							</div>
						</div>
						<div class="it-com-textnote">
							<span class="kuai"> 性质：${company.companyType } </span> <span
								class="kuai"> 所在地：${company.companyArea } </span> <span
								class="kuai"> 规模：${company.companySize } </span>
						</div>
						<div class="it-company-text">
							<p class="p1" style="padding-left: 30px;">
								<strong> </strong> <span
									style="line-height: 1.5; font-size: 14px;">
									${company.companyBrief } </span>
							</p>
							<br />
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="it-content-seqbox">
			<div id="morejob">
				<div class="it-ctn-heading">
					<div class="it-title-line">
						<h3>职位</h3>
					</div>
				</div>
				<!--职位列表-->
				<c:forEach items="${joblist}" var="job">
					<div class="it-post-box tn-border-dashed">
						<div class="it-post-name">
							<div class="tn-helper-right it-post-btn">
								<a class="it-font-underline" href="${pageContext.request.contextPath}/JobServlet?type=select&jobid=${job.jobId}" target=_blank><span
									class="tn-icon-view"></span><span class=tn-action-text>查看详细</span>
								</a>
								<c:set var="hasApplied" value="false" />
								<c:if test="${not empty appliedJobIds}">
									<c:forEach items="${appliedJobIds}" var="appliedId">
										<c:if test="${job.jobId == appliedId}">
											<c:set var="hasApplied" value="true" />
										</c:if>
									</c:forEach>
								</c:if>
								<c:choose>
									<c:when test="${hasApplied}">
										<a class="tn-button-small tn-button-disabled" href="javascript:void(0);">
											<span class="tn-button-text">已投递</span>
										</a>
									</c:when>
									<c:otherwise>
										<a class="tn-button-small" href="${pageContext.request.contextPath}/JobApplyServlet?type=apply&jobId=${job.jobId }">
											<span class="tn-button-text">申请</span>
										</a>
									</c:otherwise>
								</c:choose>
							</div>
							<h3>
								<a href="${pageContext.request.contextPath}/JobServlet?type=select&jobid=${job.jobId}" target="_blank">${job.jobName}</a>
							</h3>
						</div>
						<ul class="it-post">
							<li style="width: 300px;">薪资： <span class="it-font-size">${job.jobSalary }</span></li>
							<li style="width: 250px;"><span class=tn-text-note>工作地区：</span>
								<LaBEL for="">${job.jobArea }</LaBEL></li>
							<li><span class="tn-text-note">招聘人数：</span> ${job.jobHiringnum}</li>
							<li><span class="tn-text-note">结束日期：</span> <fmt:formatDate value="${ job.jobEnddate}"  type="date" /></li>
						</ul>
					</div>
				</c:forEach>

				<!--职位列表-->
			</div>
		</div>
	</div>
	<!-- 网站公共尾部 -->
	<iframe src="${pageContext.request.contextPath}/foot.jsp" width="100%" height="150" scrolling="no"
		frameborder="0"></iframe>
</body>
</html>