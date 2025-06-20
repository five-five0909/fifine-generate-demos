<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职位申请查询列表</title>

<link href="../css/manageadmin.css" rel="stylesheet" type="text/css" />
<!-- 日期控件js -->
<script src="../js/Calendar6.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/jquery-2.1.3.min.js"></script>
<script type="text/javascript">
var xhr = false;
function createXHR() {
	try {
		xhr = new XMLHttpRequest();
	} catch (e) {
		try {
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e1) {
			xhr = false;
		}
	}
	if (!xhr)
		alert("初始化XMLHttpRequest对象失败！");
}
function loadCompanyData(){
	createXHR();
	var url = "../JobApplyServlet";
	var content = "type=companyNameList";
	xhr.open("POST", url, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var list = eval("("+ xhr.responseText +")");
			for(var i=0;i<list.length;i++){
				var item = new Option(list[i].companyName,list[i].companyId); 
				document.getElementById("companyId").options.add(item);
			}
		}
	};
	xhr.setRequestHeader("Content-Length",content.length);
	xhr.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
	xhr.send(content);
}
function queryJob(companyElem){
	var companyId = companyElem.value;
	$.getJSON("../JobApplyServlet",{"type":"jobNameList","companyId":companyId},function(json){
	var opts = document.getElementById("jobId").options;
	for(var i=1;i<opts.length;i++)
		opts.remove(i);	
	for(var i=0;i<json.length;i++){
		var item = new Option(json[i].jobName,json[i].jobId); 
		opts.add(item);
	}
	});
}
function auditApply(applyId, state) {
	$.post("../JobApplyServlet", {type: "audit", applyId: applyId, state: state}, function(res) {
		if(res === "success") {
			alert("审核操作成功");
			query();
		} else {
			alert("审核操作失败");
		}
	});
}
function interviewApply(applyId) {
	$.post("../JobApplyServlet", {type: "interview", applyId: applyId}, function(res) {
		if(res === "success") {
			alert("面试通知已发送");
			query();
		} else {
			alert("面试通知失败");
		}
	});
}
function query(){
	var companyId = document.getElementById("companyId").value;
	var jobId = document.getElementById("jobId").value;
	var startDate =  document.getElementById("startDate").value;
	var endDate =  document.getElementById("endDate").value
	$.getJSON("../JobApplyServlet",{"type":"query","companyId":companyId,"jobId":jobId,"startDate":startDate,"endDate":endDate},function(json){
		var str = "<tr height='50px'>";
		if(json.length == 0)
			str += "<td colspan=7 height='50px'>没有匹配查询的数据</td>";
		for(var i = 0 ; i < json.length ; i++){
			str += "<td height='50px'>" + json[i].applicant.resume.realName + "</td>";
			str += "<td height='50px'>" + json[i].job.jobName + "</td>";
			if(json[i].applyState == 1)
				str += "<td height='50px'>申请</td>";
			if(json[i].applyState == 2)
				str += "<td height='50px'>审核</td>";
			if(json[i].applyState == 3)
				str += "<td height='50px'>通知</td>";
			str += "<td height='50px'>" + (new Date(json[i].applyDate)).toLocaleString() + "</td>";
			str += "<td height='50px'>";
			// 只有申请状态为1时，审核按钮可用
			if(json[i].applyState == 1) {
				str += "<a href='javascript:void(0);' class='tablelink' onclick='auditApply("+json[i].applyId+",2)'>申请审核</a> &nbsp;&nbsp;";
			} else {
				str += "<span style=\"color:gray;cursor:not-allowed;\">申请审核</span> &nbsp;&nbsp;";
			}
			// 只有申请状态为2时，面试通知按钮可用
			if(json[i].applyState == 2) {
				str += "<a href='javascript:void(0);' class='tablelink' onclick='interviewApply("+json[i].applyId+")'>面试通知</a>";
			} else {
				str += "<span style=\"color:gray;cursor:not-allowed;\">面试通知</span>";
			}
			str += "</td></tr>";
		}
		$("#viewData").html(str);
		/*//服务器端响应回的JSON示例 
		[{"applyId":1,"jobId":0,
			"applicant":{"applicantId":2,"applicantEmail":null,"applicantPwd":null,"applicantRegistDate":null,
				"resume":{"basicinfoId":21,"realName":"张三","gender":null,"birthday":null,"currentLoc":null,"residentLoc":null,"telephone":null,"email":null,"jobIntension":null,"jobExperience":null,"headShot":null}
			},
			"applyDate":1425484800000,"applyState":1,
			"job":{"jobId":1,"company":null,"jobName":"对日软件开发工程师（提供岗前培训）","jobHiringnum":0,"jobSalary":null,"jobArea":null,"jobDesc":null,"jobEnddate":null,"jobState":0,"applyNum":0}}]
 		*/
	});
}
</script>
</head>
<body onload="loadCompanyData()">
<div class="place"> <span>位置：</span>
  <ul class="placeul">
    <li><a href="${pageContext.request.contextPath }/index.html">首页</a></li>
    <li>职位申请查询列表</li>
  </ul>
</div>
<div class="rightinfo">
  <div class="tools">
  <ul class="seachform">
	  <li>
	    <div class="vocation">
	      所属企业：<select class="select3" name="companyId" id="companyId" onchange="queryJob(this)">
	        <option value="0">全部企业</option>
	      </select>
	    </div>
	  </li>
	  <li>
	    <div class="vocation">
	      所属职位：<select class="select3" name="jobId" id="jobId">
	        <option value="0">全部职位</option>
	      </select>
	    </div>
	  </li>
	  <li>
	    申请日期：<input type="text" id="startDate" name="startDate" onclick="SelectDate(this)" readonly="readonly" class="scinput"/>--
	    <input type="text" name="endDate" id="endDate" onclick="SelectDate(this)" readonly="readonly" class="scinput"/>
	  </li>
	  <li><input type="button" class="scbtn" value="查询" onclick="query()"/></li>
	</ul>
  </div>
  <table class="imgtable">
    <thead>
      <tr>
        <th>姓名</th>
        <th>申请职位</th>
        <th>申请状态</th>
        <th>申请日期</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody id="viewData"></tbody>
  </table>
</div>
</body>
</html>