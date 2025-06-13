<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%-- pageTitle 在 Servlet (ToyDetailServlet) 中设置 --%>
<jsp:include page="/common/header.jsp" />

<c:choose>
    <c:when test="${not empty toy}">
        <div class="row">
            <!-- 图片展示 -->
            <div class="col-md-6 mb-4">
                <c:choose>
                    <c:when test="${not empty toy.imageUrl}">
                        <img src="${pageContext.request.contextPath}/${fn:escapeXml(toy.imageUrl)}" class="img-fluid rounded shadow" alt="<c:out value="${toy.toyName}"/>" style="max-height: 500px; width: 100%; object-fit: contain;">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/images/placeholder.png" class="img-fluid rounded shadow" alt="无有效图片" style="max-height: 500px; width: 100%; object-fit: contain;">
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 玩具信息 -->
            <div class="col-md-6">
                <h2><c:out value="${toy.toyName}"/></h2>
                <p class="text-muted">品牌: <c:out value="${toy.brand}"/></p>
                <p class="text-muted">类别: <c:out value="${toy.category}"/></p>

                <h3 class="text-danger my-3">
                    价格: <fmt:formatNumber value="${toy.price}" type="currency" currencySymbol="¥"/>
                </h3>

                <%-- 购买按钮等交互元素可以放在这里 --%>
                <%-- <button type="button" class="btn btn-success btn-lg mb-3">加入购物车</button> --%>

                <h4>玩法介绍:</h4>
                <div class="toy-description bg-light p-3 rounded">
                    <c:choose>
                        <c:when test="${not empty toy.description}">
                            <%-- 为了安全，默认使用 c:out。如果description确定是安全的HTML，可以考虑不转义，但通常不推荐 --%>
                            <%-- 若要显示换行，一种方式是在存储时用
，然后JSP中替换为 <br> --%>
                            <c:set var="formattedDescription" value="${fn:replace(fn:escapeXml(toy.description), '\n', '<br/>')}" />
                            <p>${formattedDescription}</p> <%-- 这里因为替换了
为<br/>，所以不能再用c:out，但原始数据已escapeXml --%>
                        </c:when>
                        <c:otherwise>
                            <p>暂无详细玩法介绍。</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-12">
                 <a href="${pageContext.request.contextPath}/index" class="btn btn-outline-secondary">&laquo; 返回列表</a>
            </div>
        </div>

    </c:when>
    <c:otherwise>
        <%-- ToyDetailServlet 中处理了 toy 为 null 的情况，会设置 errorMessage 并转发到 toy_list.jsp --%>
        <%-- 但如果由于某种原因直接访问此页面且toy不存在，这里可以加一个提示 --%>
        <div class="alert alert-warning" role="alert">
            无法加载玩具详情。请 <a href="${pageContext.request.contextPath}/index" class="alert-link">返回玩具列表</a>。
            <c:if test="${not empty errorMessage}">
                <hr>
                <p class="mb-0">错误信息: <c:out value="${errorMessage}"/></p>
            </c:if>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/common/footer.jsp" />
