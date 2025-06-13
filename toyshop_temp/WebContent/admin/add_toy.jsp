<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 2}">
    <c:redirect url="${pageContext.request.contextPath}/login.jsp?error=adminRequired" />
</c:if>

<c:set var="pageTitle" value="添加新玩具" scope="request" />
<jsp:include page="/common/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-8 col-lg-7">
        <div class="card shadow-sm">
            <div class="card-header">
                <h3>添加新玩具</h3>
            </div>
            <div class="card-body">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${errorMessage}" />
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/addToy" method="post" enctype="multipart/form-data" id="addToyForm">
                    <div class="mb-3">
                        <label for="toyName" class="form-label">玩具名称 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="toyName" name="toyName" value="<c:out value="${param.toyName}"/>" required>
                    </div>
                    <div class="mb-3">
                        <label for="brand" class="form-label">品牌 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="brand" name="brand" value="<c:out value="${param.brand}"/>" required>
                    </div>
                    <div class="mb-3">
                        <label for="category" class="form-label">类别 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="category" name="category" value="<c:out value="${param.category}"/>" required>
                        <div class="form-text">例如：益智、积木、娃娃、遥控车等</div>
                    </div>
                    <div class="mb-3">
                        <label for="price" class="form-label">价格 (元) <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="price" name="price" step="0.01" min="0.01" value="<c:out value="${param.price}"/>" required>
                    </div>
                    <div class="mb-3">
                        <label for="imageFile" class="form-label">玩具图片 <span class="text-danger">*</span></label>
                        <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/jpeg, image/png, image/gif" required>
                        <div class="form-text">支持 JPG, PNG, GIF 格式的图片。</div>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">玩法介绍</label>
                        <textarea class="form-control" id="description" name="description" rows="5"><c:out value="${param.description}"/></textarea>
                    </div>

                    <hr>
                    <div class="d-flex justify-content-end">
                        <a href="${pageContext.request.contextPath}/admin/manage" class="btn btn-outline-secondary me-2">取消并返回列表</a>
                        <button type="submit" class="btn btn-primary">确认添加</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
// 简单的客户端文件大小和类型检查 (可选, 服务端验证是主要的)
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('addToyForm');
    const imageFile = document.getElementById('imageFile');
    if (form && imageFile) {
        form.addEventListener('submit', function(event) {
            const file = imageFile.files[0];
            if (file) {
                // 检查文件大小 (例如: 最大 5MB)
                const maxSize = 5 * 1024 * 1024; // 5MB
                if (file.size > maxSize) {
                    alert('图片文件过大，请上传小于 5MB 的图片。');
                    event.preventDefault();
                    return;
                }
                // 检查文件类型 (简单检查后缀名)
                const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
                if (!allowedTypes.includes(file.type)) {
                     alert('不支持的图片格式。请上传 JPG, PNG, 或 GIF 格式的图片。');
                     event.preventDefault();
                     return;
                }
            } else if (imageFile.required) { // 如果文件是必填的，但没有选择文件
                 alert('请选择玩具图片进行上传。');
                 event.preventDefault();
                 return;
            }
        });
    }
});
</script>

<jsp:include page="/common/footer.jsp" />
