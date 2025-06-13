<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- 用于格式化价格 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <%-- 确保fn库被引入 --%>


<%-- pageTitle 在 Servlet 中设置 --%>
<jsp:include page="/common/header.jsp" />

<div class="row">
    <div class="col-12">
        <h1 class="mb-4">
            <c:choose>
                <c:when test="${not empty isSearchResult}">
                    搜索结果 <c:if test="${not empty searchKeyword}">针对 “<c:out value="${searchKeyword}"/>”</c:if>
                </c:when>
                <c:otherwise>
                    玩具浏览
                </c:otherwise>
            </c:choose>
        </h1>
    </div>
</div>

<!-- 搜索表单 -->
<div class="row mb-4">
    <div class="col-12">
        <form action="${pageContext.request.contextPath}/search" method="get" class="card p-3 shadow-sm">
            <div class="row g-3 align-items-end">
                <div class="col-md-5">
                    <label for="keyword" class="form-label">关键字 (玩具名或品牌):</label>
                    <input type="text" class="form-control" id="keyword" name="keyword" value="<c:out value="${searchKeyword}"/>">
                </div>
                <div class="col-md-3">
                    <label for="minPrice" class="form-label">最低价格:</label>
                    <input type="number" class="form-control" id="minPrice" name="minPrice" step="0.01" min="0" value="<c:out value="${searchMinPrice}"/>">
                </div>
                <div class="col-md-3">
                    <label for="maxPrice" class="form-label">最高价格:</label>
                    <input type="number" class="form-control" id="maxPrice" name="maxPrice" step="0.01" min="0" value="<c:out value="${searchMaxPrice}"/>">
                </div>
                <div class="col-md-1 d-grid">
                    <button type="submit" class="btn btn-success">搜索</button>
                </div>
            </div>
            <c:if test="${not empty requestScope.warningMessage}"> <%-- 来自SearchServlet的价格范围警告 --%>
                 <div class="form-text text-warning mt-2"><c:out value="${warningMessage}"/></div>
            </c:if>
        </form>
    </div>
</div>

<!-- 玩具列表 -->
<div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
    <c:choose>
        <c:when test="${not empty toyList}">
            <c:forEach var="toy" items="${toyList}">
                <div class="col">
                    <div class="card h-100 shadow-hover">
                        <a href="${pageContext.request.contextPath}/toyDetail?id=${toy.toyId}" class="text-decoration-none text-dark stretched-link">
                            <c:choose>
                                <c:when test="${not empty toy.imageUrl}">
                                    <img src="${pageContext.request.contextPath}/${fn:escapeXml(toy.imageUrl)}" class="card-img-top" alt="<c:out value="${toy.toyName}"/>" style="height: 200px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/images/placeholder.png" class="card-img-top" alt="无图片" style="height: 200px; object-fit: cover;">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body">
                                <h5 class="card-title"><c:out value="${toy.toyName}"/></h5>
                                <p class="card-text text-muted small">品牌: <c:out value="${toy.brand}"/></p>
                                <p class="card-text fw-bold text-danger fs-5">
                                    <fmt:formatNumber value="${toy.price}" type="currency" currencySymbol="¥"/>
                                </p>
                            </div>
                        </a>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="col-12">
                <div class="alert alert-info" role="alert">
                    <c:choose>
                        <c:when test="${not empty noResultsMessage}"> <%-- 来自SearchServlet --%>
                            <c:out value="${noResultsMessage}"/>
                        </c:when>
                        <c:otherwise>
                            当前没有可供展示的玩具。
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- 分页导航 (仅在非搜索结果或搜索结果也需要分页时显示) -->
<c:if test="${(empty isSearchResult || requestScope.totalPages > 1) && not empty toyList && requestScope.totalPages > 1}">
    <div class="row mt-5">
        <div class="col-12">
            <nav aria-label="玩具分页">
                <ul class="pagination justify-content-center">
                    <%-- 上一页 --%>
                    <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                        <a class="page-link" href="<c:url value="/index"><c:param name="pageNum" value="${currentPage - 1}"/></c:url>" aria-label="上一页">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <%-- 页码 --%>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item <c:if test="${currentPage == i}">active</c:if>">
                            <a class="page-link" href="<c:url value="/index"><c:param name="pageNum" value="${i}"/></c:url>"><c:out value="${i}"/></a>
                        </li>
                    </c:forEach>

                    <%-- 下一页 --%>
                    <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                        <a class="page-link" href="<c:url value="/index"><c:param name="pageNum" value="${currentPage + 1}"/></c:url>" aria-label="下一页">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
         <div class="col-12 text-center mt-2">
            <p class="text-muted small">共 ${totalToys} 件玩具，第 ${currentPage} / ${totalPages} 页</p>
        </div>
    </div>
</c:if>
<style>
    .shadow-hover:hover {
        transition: box-shadow .3s;
        box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
    }
</style>

<jsp:include page="/common/footer.jsp" />
