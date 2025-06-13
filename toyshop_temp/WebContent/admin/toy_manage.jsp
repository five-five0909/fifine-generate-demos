<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%-- pageTitle 在 AdminToyListServlet 中设置 --%>
<c:if test="${empty sessionScope.user || sessionScope.user.role != 2}">
    <%-- 权限检查：如果不是管理员或未登录，重定向到登录页 --%>
    <c:redirect url="${pageContext.request.contextPath}/login.jsp?error=adminRequired" />
</c:if>

<jsp:include page="/common/header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1>玩具后台管理</h1>
    <a href="${pageContext.request.contextPath}/admin/add_toy.jsp" class="btn btn-success">
        <i class="bi bi-plus-circle-fill"></i> 添加新玩具
    </a>
</div>

<%-- 操作结果提示信息 --%>
<c:if test="${not empty param.addSuccess}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        玩具添加成功！
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty param.updateSuccess}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        玩具信息更新成功！
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty param.deleteSuccess}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        玩具删除成功！
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty param.deleteError}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        删除玩具失败。ID: <c:out value="${param.toyId}"/>。请检查相关依赖或联系技术支持。
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        操作失败：<c:out value="${param.error}"/>.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>


<div class="card shadow-sm">
    <div class="card-header">
        玩具列表
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty toyList}">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th scope="col">ID</th>
                                <th scope="col">图片</th>
                                <th scope="col">玩具名称</th>
                                <th scope="col">品牌</th>
                                <th scope="col">类别</th>
                                <th scope="col">价格</th>
                                <th scope="col" style="min-width: 120px;">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="toy" items="${toyList}">
                                <tr>
                                    <th scope="row"><c:out value="${toy.toyId}"/></th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty toy.imageUrl}">
                                                <img src="${pageContext.request.contextPath}/${fn:escapeXml(toy.imageUrl)}" alt="<c:out value="${toy.toyName}"/>" style="width: 50px; height: 50px; object-fit: cover;" class="img-thumbnail">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted small">无图</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${toy.toyName}"/></td>
                                    <td><c:out value="${toy.brand}"/></td>
                                    <td><c:out value="${toy.category}"/></td>
                                    <td><fmt:formatNumber value="${toy.price}" type="currency" currencySymbol="¥"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/edit_toy.jsp?id=${toy.toyId}" class="btn btn-sm btn-primary me-1" title="编辑">
                                            <i class="bi bi-pencil-square"></i> 编辑
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/deleteToy?id=${toy.toyId}" class="btn btn-sm btn-danger" title="删除" onclick="return confirm('确定要删除玩具 “<c:out value="${fn:escapeXml(toy.toyName)}"/>” 吗？此操作不可恢复。');">
                                            <i class="bi bi-trash-fill"></i> 删除
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">当前没有玩具信息。 <a href="${pageContext.request.contextPath}/admin/add_toy.jsp">添加一个新玩具</a></div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- 分页导航 (如果AdminToyListServlet中启用了分页) -->
<c:if test="${not empty toyList && requestScope.totalPages > 1}">
    <div class="row mt-4">
        <div class="col-12">
            <nav aria-label="后台玩具分页">
                <ul class="pagination justify-content-center">
                    <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                        <a class="page-link" href="<c:url value="/admin/manage"><c:param name="pageNum" value="${currentPage - 1}"/></c:url>">&laquo;</a>
                    </li>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item <c:if test="${currentPage == i}">active</c:if>">
                            <a class="page-link" href="<c:url value="/admin/manage"><c:param name="pageNum" value="${i}"/></c:url>"><c:out value="${i}"/></a>
                        </li>
                    </c:forEach>
                    <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                        <a class="page-link" href="<c:url value="/admin/manage"><c:param name="pageNum" value="${currentPage + 1}"/></c:url>">&raquo;</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</c:if>

<%-- 引入Bootstrap Icons的CDN，如果需要使用上面的i标签图标 --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<jsp:include page="/common/footer.jsp" />
