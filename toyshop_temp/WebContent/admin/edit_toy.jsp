<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- For price formatting --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%--
  编辑玩具页面
  这个页面在加载时，通常需要一个Servlet来根据id查询玩具信息，
  然后将Toy对象放入request作用域，再转发到此JSP。
  或者，此JSP页面可以通过AJAX/Scriptlet获取数据（不推荐）。

  当前假设:
  1. 管理员从 toy_manage.jsp 点击编辑按钮，链接为 edit_toy.jsp?id=${toy.toyId}
  2. 我们需要在这个JSP页面加载时，通过JSP脚本或一个辅助Servlet（更佳实践）获取玩具数据。
     为了简化，这里将使用JSP脚本通过DAO直接获取。
     在更完善的MVC中，应该有一个 EditToyLoaderServlet。
--%>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 2}">
    <c:redirect url="${pageContext.request.contextPath}/login.jsp?error=adminRequired" />
</c:if>

<%-- 获取玩具ID --%>
<c:set var="toyIdParam" value="${param.id}" />

<%-- 只有当toy对象不存在于request中，并且toyIdParam有效时，才尝试加载 --%>
<c:if test="${empty requestScope.toy && not empty toyIdParam}">
    <%-- Java代码块来获取玩具数据 --%>
    <%
        com.toyshop.dao.ToyDao toyDaoForPage = new com.toyshop.dao.ToyDao();
        int currentToyId = 0;
        try {
            currentToyId = Integer.parseInt(request.getParameter("id"));
            if (currentToyId > 0) {
                com.toyshop.bean.Toy toyToEdit = toyDaoForPage.findById(currentToyId);
                if (toyToEdit != null) {
                    request.setAttribute("toy", toyToEdit);
                } else {
                    // 如果找不到玩具，设置错误信息，稍后会显示
                    request.setAttribute("loadErrorMessage", "无法找到ID为 " + currentToyId + " 的玩具进行编辑。");
                }
            }
        } catch (NumberFormatException nfe) {
            request.setAttribute("loadErrorMessage", "无效的玩具ID格式。");
        } catch (Exception e) {
            request.setAttribute("loadErrorMessage", "加载玩具数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    %>
</c:if>

<%-- 如果toy对象是从Servlet（如更新失败后转发）传递过来的，它会优先使用requestScope.toy --%>
<c:set var="editableToy" value="${not empty requestScope.toy ? requestScope.toy : toy}" />

<c:set var="pageTitle" value="编辑玩具 - ${not empty editableToy ? editableToy.toyName : '未知玩具'}" scope="request" />
<jsp:include page="/common/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-8 col-lg-7">
        <div class="card shadow-sm">
            <div class="card-header">
                <h3>编辑玩具信息</h3>
            </div>
            <div class="card-body">
                <c:if test="${not empty errorMessage || not empty loadErrorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <c:if test="${not empty errorMessage}"><c:out value="${errorMessage}" /><br/></c:if>
                        <c:if test="${not empty loadErrorMessage}"><c:out value="${loadErrorMessage}" /></c:if>
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${not empty editableToy}">
                        <form action="${pageContext.request.contextPath}/admin/updateToy" method="post">
                            <%-- 隐藏的 toyId 字段 --%>
                            <input type="hidden" name="toyId" value="<c:out value="${editableToy.toyId}"/>">

                            <div class="mb-3">
                                <label for="toyName" class="form-label">玩具名称 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="toyName" name="toyName" value="<c:out value="${editableToy.toyName}"/>" required>
                            </div>
                            <div class="mb-3">
                                <label for="brand" class="form-label">品牌 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="brand" name="brand" value="<c:out value="${editableToy.brand}"/>" required>
                            </div>
                            <div class="mb-3">
                                <label for="category" class="form-label">类别 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="category" name="category" value="<c:out value="${editableToy.category}"/>" required>
                            </div>
                            <div class="mb-3">
                                <label for="price" class="form-label">价格 (元) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="price" name="price" step="0.01" min="0.01" value="<fmt:formatNumber value="${editableToy.price}" type="number" groupingUsed="false" maxFractionDigits="2"/>" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">当前图片:</label><br>
                                <c:choose>
                                    <c:when test="${not empty editableToy.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/${fn:escapeXml(editableToy.imageUrl)}" alt="当前图片" style="max-width: 150px; max-height: 150px; object-fit: contain;" class="img-thumbnail mb-2">
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">暂无图片</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="mb-3">
                                <label for="imageUrl" class="form-label">图片相对路径 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="imageUrl" name="imageUrl" value="<c:out value="${editableToy.imageUrl}"/>" required>
                                <div class="form-text">
                                    例如: assets/images/your_image.jpg.
                                    如需更换图片，请先上传新图片到服务器的对应位置，然后更新此路径。
                                    (当前版本不直接处理图片文件替换上传)
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">玩法介绍</label>
                                <textarea class="form-control" id="description" name="description" rows="5"><c:out value="${editableToy.description}"/></textarea>
                            </div>

                            <hr>
                            <div class="d-flex justify-content-end">
                                <a href="${pageContext.request.contextPath}/admin/manage" class="btn btn-outline-secondary me-2">取消并返回列表</a>
                                <button type="submit" class="btn btn-primary">确认更新</button>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">
                            <c:if test="${empty loadErrorMessage}">无法加载玩具数据进行编辑。</c:if>
                            <a href="${pageContext.request.contextPath}/admin/manage" class="btn btn-primary mt-2">返回玩具管理列表</a>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
