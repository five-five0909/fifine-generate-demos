<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
	import="com.huawei.itoffer.dao.CompanyDAO,com.huawei.itoffer.bean.Company,
      com.huawei.itoffer.bean.Job"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RTO服务_锐聘官网-大学生求职,IT行业招聘，IT企业快速入职 - 锐聘网</title>
<link href="css/base.css" type="text/css" rel="stylesheet" />
<link href="css/index.css" type="text/css" rel="stylesheet" />
</head>
<body class="tn-page-bg">
	<jsp:include page="top.jsp"></jsp:include>
	<div id="tn-content">
		<!-- 招聘企业展示 -->
		<c:forEach items="${companyList}" var="company">
			<div class="tn-grid">
				<div
					class="tn-box tn-widget tn-widget-content tn-corner-all it-home-box">
					<div class="tn-box-content tn-widget-content tn-corner-all">
						<!-- 企业图片展示 -->
						<div
							class="it-company-keyimg tn-border-bottom tn-border-gray it-home-box">
							<a href="CompanyServlet?type=select&id=${company.companyId}">
								<img src="recruit/images/${company.companyPic}" width="990">
							</a>
						</div>
						<!-- 招聘职位展示 -->
						<c:forEach items="${company.jobs}" var="job">
							<div class="it-home-present">
								<div class="it-present-btn">
									<a class=" tn-button tn-button-home-apply"
										href="JobApplyServlet?type=apply&jobId=${job.jobId }"> <span
										class="tn-button-text">我要申请</span></a>
								</div>
								<div class="it-present-text" style="padding-left: 185px;">
									<div class="it-line01 it-text-bom">
										<p class="it-text-tit">职位</p>
										<p class="it-line01 it-text-explain">
											<span class="tn-helper-right tn-action"> <a
												href="CompanyServlet?type=select&id=${company.companyId}"
												class="tn-button tn-corner-all tn-button-text-only  tn-button-semidlong">
													<span class="tn-button-text">更多职位</span>
											</a>
											</span> <b >${job.jobName}</b>
										</p>
									</div>
									<div class="it-line01 it-text-top">
										<p class="it-text-tit">薪资</p>
										<p class="it-line01 it-text-explain">
											<b>${job.jobSalary}</b>
										</p>

									</div>
								</div>
								<div class="it-present-text">
									<div class="it-line01 it-text-bom">
										<p class="it-text-tit">到期时间</p>
										<p class="it-line01 it-text-explain">
											<b>
											<fmt:formatDate value="${ job.jobEnddate}"  type="both" />
											</b>
										</p>
									</div>
									<div class="it-line01 it-text-top">
										<p class="it-text-tit">工作地区</p>
										<p class="it-line01 it-text-explain">
											<b>${job.jobArea}</b>
										</p>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<!-- 网站公共尾部 -->
	<iframe src="foot.jsp" width="100%" height="150" scrolling="no"
		frameborder="0"></iframe>
</body>
</html>