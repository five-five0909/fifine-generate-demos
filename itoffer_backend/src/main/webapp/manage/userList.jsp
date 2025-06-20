<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,com.qst.itoffer.bean.User" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户查询列表</title>
<link href="../css/manageadmin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery-2.1.3.min.js"></script>
<script type="text/javascript"> 
function confirmDel(param)
{
if(window.confirm("您确定要删除该条数据吗？")){	
document.location="../UserServlet?type=deleteSelect&userId="+param;
}else{
	return false;
}
}  
$(function(){
    // 全选/反选
    $("#checkAll").on("change", function(){
        $(".rowCheck").prop("checked", this.checked);
    });
    $(document).on("change", ".rowCheck", function(){
        var all = $(".rowCheck").length;
        var checked = $(".rowCheck:checked").length;
        $("#checkAll").prop("checked", all === checked);
    });
});
</script>
</head>

<body>
<div class="place"> <span>位置：</span>
  <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">用户列表</a></li>
  </ul>
</div>
<div class="rightinfo">
  <div class="tools">
    <ul class="toolbar">
      <li class="click"><span>
        <a href="userAdd.html"><img src="../images/t01.png" /></span>添加</a></li>
      <!-- <li class="click"><span><img src="../images/t02.png" /></span>
        <a href="#">修改</a></li>
      <li><span><img src="../images/t03.png" /></span>
        <a href="userDelete.html">删除</a></li> -->
    </ul>
<!-- <iframe src="userSearch.html" scrolling="no" frameborder="0" width="400" 
        height="42"></iframe> -->
  </div>
  
  <jsp:useBean id="pagination" 
        class="com.qst.itoffer.bean.UserPageBean" scope="request">
      <jsp:setProperty name="pagination" property="pageSize" value="10" />
      <jsp:setProperty name="pagination" property="pageNo" value="1"/>
  </jsp:useBean>
  <jsp:setProperty name="pagination" property="pageNo" param="pageNo"/>
  
  <table class="imgtable">
    <thead>
      <tr>
        <th ><input id="checkAll" type="checkbox" /></th>
        <th>用户登录名</th>
        <th>用户真实姓名</th>
        <th>用户Email</th>
        <th>用户角色</th>
        <th>用户状态</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
    <% 
        List<User> list = pagination.getPageData();
        if(list != null)
            for(User user : list){
    %>
      <tr height="50px">
        <td ><input class="rowCheck" type="checkbox" value="<%=user.getUserId()%>" /></td>
        <td><%=user.getUserLogname() %></td>
        <td><%=user.getUserRealname() %></td>
        <td><%=user.getUserEmail() %></td>
        <td><%if(user.getUserRole() == 1) {%> 普通用户<%} %>
        <%if(user.getUserRole() == 2) {%> 企业管理员<%} %>
        <%if(user.getUserRole() == 3) {%> 系统管理员<%} %></td>
        <td><%if(user.getUserState() == 1) {%> 启用<%} %>
        <%if(user.getUserRole() == 0) {%> 禁用<%} %></td>
        <td ><a href="../UserServlet?type=updateSelect&userId=<%=user.getUserId() %>" class="tablelink">修改</a> &nbsp;&nbsp;
        <a href="javascript:confirmDel(<%=user.getUserId() %>);">删除</a></td>
      </tr>
      <%} %>
    </tbody>
  </table>
  <div class="pagin">
<div class="message">共<i class="blue">
<jsp:getProperty property="totalPages" name="pagination"/></i>页，
当前显示第&nbsp;<i class="blue">
<jsp:getProperty property="pageNo" name="pagination"/></i>页</div>
    <ul class="paginList">
      <li class="paginItem"><a href="userList.jsp?pageNo=1">首页</a></li>
      <%if(pagination.isHasPreviousPage()){ %>
      <li class="paginItem">
        <a href="userList.jsp?pageNo=<%=pagination.getPageNo()-1%>">
        上一页<span class="pagepre"></span></a></li>
      <%} %>
       <%if(pagination.isHasNextPage()){ %>
      <li class="paginItem">
        <a href="userList.jsp?pageNo=<%=pagination.getPageNo()+1 %>">
        下一页<span class="pagenxt"></span></a></li>
       <%} %>
       <li class="paginItem">
        <a href="userList.jsp?pageNo=<%=pagination.getTotalPages()%>">
            尾页</a></li>
    </ul>
  </div>
</div>
</body>
</html>