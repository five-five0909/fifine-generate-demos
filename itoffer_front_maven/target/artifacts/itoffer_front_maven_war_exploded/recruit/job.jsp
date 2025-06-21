<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>职位详情展示 - ${job.jobName}</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/job.css" type="text/css" rel="stylesheet">
<style>
	.tn-button-disabled {
		background: #ccc !important;
		border-color: #bbb !important;
		color: #888 !important;
		cursor: not-allowed;
	}
	.it-smallbutton-disabled-hover {
		background-position: -243px -184px !important;
		cursor: not-allowed;
	}
</style>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<div class="tn-grid" align="center">
		<div class="bottomban">
			<div class="bottombanbox">
				<a
					href="${pageContext.request.contextPath}/CompanyServlet?type=select&id=${requestScope.company.companyId }">
					<img src="${pageContext.request.contextPath}/recruit/images/${requestScope.company.companyPic }">
				</a>
			</div>
		</div>
	</div>
	<div class="tn-grid">
		<div class="tn-box-content">
			<div class="it-main">
				<div class="it-ctn-heading">
					<div class="it-title-line">
						<h3>${job.jobName }</h3>
					</div>
				</div>
				<div class="job">
					<table class="it-table">
						<tbody>
							<tr>
								<td class="it-table-title">招聘人数：</td>
								<td class="tn-border-rb">${job.jobHiringnum }人</td>
								<td class="it-table-title">薪资：</td>
								<td class="tn-border-rb">${job.jobSalary }</td>
							</tr>
							<tr>

								<td class="it-table-title">工作地区：</td>
								<td class="tn-border-rb">${job.jobArea }</td>
								<td class="it-table-title">截止日期：</td>
								<td class="tn-border-rb"><fmt:formatDate
										value="${ job.jobEnddate}" type="date" /></td>
							</tr>
						</tbody>
					</table>
					<div class="it-post-count">
						<div class="it-com-apply">
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
									<a href="javascript:void(0);"
									   class="tn-button it-smallbutton-disabled-hover"></a>
								</c:when>
								<c:otherwise>
									<a href="${pageContext.request.contextPath}/JobApplyServlet?type=apply&jobId=${job.jobId }"
									   class="tn-button it-smallbutton-apply-hover"></a>
								</c:otherwise>
							</c:choose>
						</div>
						<ul class="tn-text-note it-text-part">
							<!-- <li class="jobli"><span class="tn-explain-icon"><span
									class="tn-icon it-icon-time"></span><span class="tn-icon-text"
									id="leftTimeShowSpan"> <label>已过期</label>
								</span></span></li> -->
							<li class="jobli"><span class="tn-explain-icon"><span
									class="tn-icon it-icon-people"></span><span
									class="tn-icon-text">招聘人数 <span class="it-font-cor">${job.jobHiringnum}</span>
										人
								</span></span></li>
							<li class="jobli"><span class="tn-explain-icon"><span
									class="tn-icon it-icon-people"></span><span
									class="tn-icon-text">申请人数 <span class="it-font-cor">44</span>
										人
								</span></span></li>
						</ul>
					</div>
					<div class="clear"></div>
					<div class="it-requirements-title">
						<h3 class="it-font-size">岗位要求</h3>
					</div>
					<div class="it-post-text">
						<p>${job.jobDesc }</p>

					</div>
					<div class="btn_bot">
						<a class="tn-button-secondary" href="${pageContext.request.contextPath}/CompanyServlet?type=select&id=${job.company.companyId}" target="_blank"> <span
							style="color: #1faebc" class="tn-button-text">查看公司信息</span>
						</a>
						<c:choose>
							<c:when test="${hasApplied}">
								<a class="tn-button-secondary tn-button-disabled" href="javascript:void(0);">
									<span style="color: #888" class="tn-button-text">已投递</span>
								</a>
							</c:when>
							<c:otherwise>
								<a class="tn-button-secondary"
								   href="${pageContext.request.contextPath}/JobApplyServlet?type=apply&jobId=${job.jobId }">
									<span style="color: #1faebc" class="tn-button-text">立即申请</span>
								</a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<div class="job_right">
				<div class="it-listbox">
					<div class="it-listbox-header">
						<div class="tn-option">
							<a href="#" target="_blank">更多</a>
						</div>
						<h3 class="tn-helper-reset">职位分类</h3>
					</div>
					<div class="it-listbox-content">
						<ul>
							<li><span class="tn-icon"></span><a title="软件开发工程师" href="#"
								target="_blank">软件开发工程师</a></li>
							<li><span class="tn-icon"></span><a title=".Net软件开发"
								href="#" target="_blank">.Net软件开发</a></li>
							<li><span class="tn-icon"></span><a title="移动客户端开发" href="#"
								target="_blank">移动客户端开发</a></li>
							<li><span class="tn-icon"></span><a title="Java软件开发"
								href="#" target="_blank">Java软件开发</a></li>
							<li><span class="tn-icon"></span><a title="其他" href="#"
								target="_blank">其他</a></li>
							<li><span class="tn-icon"></span><a title="IT运维工程师" href="#"
								target="_blank">IT运维工程师</a></li>
						</ul>
					</div>
				</div>
				<div class="it-listbox">
					<div class="it-listbox-header">
						<div class="tn-option">
							<a href="http://www.itoffer.cn" target="_blank">更多</a>
						</div>
						<h3 class="tn-helper-reset">热招企业</h3>
					</div>
					<div class="it-listbox-content">
						<ul>
							<li><span class="tn-icon"></span><a title="“锐聘之星”软件设计大赛"
								href=# " target="_blank">“锐聘之星”软件设计大赛</a></li>
							<li><span class="tn-icon"></span><a title="凌志软件股份有限公司"
								href="#" target="_blank">凌志软件股份有限公司</a></li>
							<li><span class="tn-icon"></span><a title="北京日立华胜信息系统有限公司"
								href="#" target="_blank">北京日立华胜信息系统有限公司</a></li>
							<li><span class="tn-icon"></span><a title="宏智科技（苏州）有限公司"
								href="#" target="_blank">宏智科技（苏州）有限公司</a></li>
							<li><span class="tn-icon"></span><a title="江苏仕德伟网络科技股份有限公司"
								href="#" target="_blank">江苏仕德伟网络科技股份有限公司</a></li>
							<li><span class="tn-icon"></span><a title="青岛百灵信息科技有限公司"
								href="#" target="_blank">青岛百灵信息科技有限公司</a></li>
							<li><span class="tn-icon"></span><a title="无锡晟奥软件有限公司"
								href="#" target="_blank">无锡晟奥软件有限公司</a></li>
							<li><span class="tn-icon"></span><a title="苏州大宇宙信息创造有限公司"
								href="#" target="_blank">苏州大宇宙信息创造有限公司</a></li>
							<li><span class="tn-icon"></span><a title="青岛拓宇网络科技有限公司"
								href="#" target="_blank">青岛拓宇网络科技有限公司</a></li>
							<li><span class="tn-icon"></span><a title="无锡NTT DATA有限公司"
								href="#" target="_blank">无锡NTT DATA有限公司</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<iframe src="${pageContext.request.contextPath}/foot.jsp" width="100%" height="150" scrolling="no"
		frameborder="0"> </iframe>
</body>
</html>