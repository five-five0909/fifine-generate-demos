<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职位列表</title>

<link href="css/manageadmin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-2.1.3.min.js"></script>
<script type="text/javascript">
function deleteJob(jobId) {
    if(confirm("确定要删除该职位吗？")) {
        $.post("../JobServlet", {type: "delete", jobId: jobId}, function(res) {
            if(res === "success") {
                alert("删除成功");
                location.reload();
            } else if(res === "hasApply") {
                alert("该职位已有投递，不能删除！");
            } else {
                alert("删除失败");
            }
        });
    }
}
function updateJobDialog(jobId, jobHiringnum, jobEndtime, jobState, companyId) {
    var html = '<div id="updateJobDiv" style="background:#fff;padding:20px;border:1px solid #ccc;position:fixed;top:20%;left:40%;z-index:9999;">'
        + '<h3>修改职位</h3>'
        + '招聘数：<input id="editJobHiringnum" type="number" value="'+jobHiringnum+'"/><br/>'
        + '结束日期：<input id="editJobEndtime" value="'+jobEndtime+'"/><br/>'
        + '状态：<select id="editJobState">'
        + '<option value="1"'+(jobState==1?' selected':'')+'>招聘中</option>'
        + '<option value="2"'+(jobState==2?' selected':'')+'>已暂停</option>'
        + '<option value="3"'+(jobState==3?' selected':'')+'>已结束</option>'
        + '</select><br/>'
        + '<input type="hidden" id="editCompanyId" value="'+companyId+'"/>'
        + '<button onclick="submitUpdateJob('+jobId+')">提交</button>'
        + '<button onclick="$(\'#updateJobDiv\').remove()">取消</button>'
        + '</div>';
    $("body").append(html);
}
function submitUpdateJob(jobId) {
    $.post("../JobServlet", {
        type: "update",
        jobId: jobId,
        jobHiringnum: $("#editJobHiringnum").val(),
        jobEndtime: $("#editJobEndtime").val(),
        jobState: $("#editJobState").val(),
        companyId: $("#editCompanyId").val()
    }, function(res) {
        if(res === "success") {
            alert("修改成功");
            location.reload();
        } else {
            alert("修改失败");
        }
        $("#updateJobDiv").remove();
    });
}
</script>
</head>
<body>
<div class="place"> <span>位置：</span>
  <ul class="placeul">
    <li><a href="manage/index.html">首页</a></li>
    <li>职位列表</li>
  </ul>
</div>
<div class="rightinfo">
  <div class="tools">
    <ul class="toolbar">
      <li class="click"><span><img src="images/t01.png" /></span>
      <!-- <a href="manage/jobAdd.html"></a>添加</a> -->
    </li>
    </ul>
   <jsp:include page="jobSearch.jsp"></jsp:include>
  </div>
  <table class="imgtable">
    <thead>
      <tr>
        <th>职位名称</th>
        <th>所属企业</th>
        <th>招聘数</th>
        <th>申请数</th>
        <th>结束日期</th>
        <th>招聘状态</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.joblist}" var="job">
      <tr height="50px">
        <td>${job.jobName }</td>
        <td>${job.company.companyName }</td>
        <td>${job.jobHiringnum }</td>
        <td>${job.applyNum }</td>
        <td>${job.jobEndtime }</td>
        <td><c:if test="${job.jobState == 1}">招聘中  </c:if>
        <c:if test="${job.jobState == 2}">已暂停</c:if>
        <c:if test="${job.jobState == 3}">已结束</c:if></td>
        <td >
            <a href="javascript:void(0);" class="tablelink" onclick="updateJobDialog('${job.jobId}','${job.jobHiringnum}','${job.jobEndtime}','${job.jobState}','${job.company.companyId}')">修改</a> &nbsp;&nbsp;
            <c:choose>
                <c:when test="${job.applyNum == 0}">
                    <a href="javascript:void(0);" class="tablelink" onclick="deleteJob('${job.jobId}')"> 删除</a>
                </c:when>
                <c:otherwise>
                    <span style="color:gray;cursor:not-allowed;">删除</span>
                </c:otherwise>
            </c:choose>
        </td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
