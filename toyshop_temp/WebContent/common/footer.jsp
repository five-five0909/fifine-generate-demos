<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

    <%-- 主体内容结束 --%>
</div> <%-- class="container" --%>

<footer class="bg-light text-center text-lg-start mt-auto py-3">
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.05);">
        © <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> 儿童玩具售卖系统. 版权所有.
        <%-- <br>
        <a class="text-dark" href="#">隐私政策</a> | <a class="text-dark" href="#">服务条款</a> --%>
    </div>
</footer>

<!-- Bootstrap 5 CDN JS (Bundle includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
<%-- 如果有自定义的全局JS文件，可以在这里引入 --%>
<%-- <script src="${pageContext.request.contextPath}/assets/js/main.js"></script> --%>
</body>
</html>
