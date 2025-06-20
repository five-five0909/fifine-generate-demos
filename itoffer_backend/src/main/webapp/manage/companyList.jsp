<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,com.qst.itoffer.bean.Company" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<%
List<Company> list  = (List<Company>)request.getAttribute("list");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>企业列表</title>

<link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-2.1.3.min.js"></script>
<script type="text/javascript">
function deleteCompany(companyId, hasJob) {
    if(hasJob) {
        alert("该企业下有职位，不能删除！");
        return;
    }
    if(confirm("确定要删除该企业吗？")) {
        $.post("CompanyServlet", {type: "delete", companyId: companyId}, function(res) {
            if(res === "success") {
                alert("删除成功");
                location.reload();
            } else if(res === "hasJob") {
                alert("该企业下有职位，不能删除！");
            } else {
                alert("删除失败");
            }
        });
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
    <li><a href="#">企业列表</a></li>
  </ul>
</div>
<div class="rightinfo">
  <div class="tools">
    <ul class="toolbar">
      <li class="click"><span><img src="images/t01.png" /></span><a href="manage/companyAdd.html">添加</a></li>
      <li class="click"><span><img src="images/t02.png" /></span><a href="#">修改</a></li>
      <li><span><img src="images/t03.png" /></span><a href="#">删除</a></li>
    </ul>
  </div>
  <table class="imgtable">
    <thead>
      <tr>
        <th ><input id="checkAll" type="checkbox" /></th>
        <th>企业名称</th>
        <th>企业所在地</th>
        <th>企业规模</th>
        <th>企业性质</th>
        <th>招聘状态</th>
        <th>显示排序</th>
        <th>浏览数</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
    <%
    if(list != null) {
    	for(Company c : list){
        // 查询该企业下是否有职位
        boolean hasJob = false;
        com.qst.itoffer.dao.CompanyDAO companyDAO = new com.qst.itoffer.dao.CompanyDAO();
        hasJob = companyDAO.hasJob(c.getCompanyId());
    %>
      <tr height="50px">
        <td ><input class="rowCheck" type="checkbox" value="<%=c.getCompanyId()%>" /></td>
        <td><%=c.getCompanyName() %></td>
        <td><%=c.getCompanyArea() %></td>
        <td><%=c.getCompanySize() %></td>
        <td><%=c.getCompanyType() %></td>
        <td><%if(c.getCompanyState()==1){ %>招聘中
        <%}else if(c.getCompanyState()==2){ %>已暂停
        <%}else if(c.getCompanyState()==3){ %>已结束<%} %></td>
        <td><%=c.getCompanySort() %></td>
        <td><%=c.getCompanyViewnum() %></td>
        <td ><a href="CompanyServlet?type=updateSelect&companyId=<%=c.getCompanyId()%>" 
    class="tablelink">修改</a>
         &nbsp;&nbsp;
         <% if(!hasJob) { %>
             <a href="javascript:void(0);" class="tablelink" onclick="deleteCompany('<%=c.getCompanyId()%>', false)">删除</a>
         <% } else { %>
             <span style="color:gray;cursor:not-allowed;">删除</span>
         <% } %>
         </td>
      </tr>
    <%
    	} 
    }
    %>
    </tbody>
  </table>
</div>
</body>
</html>