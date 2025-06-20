<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>我的简历 - 锐聘网</title>
    <link href="css/base.css" type="text/css" rel="stylesheet" />
    <link href="css/resume.css" type="text/css" rel="stylesheet" />
    <style>
        .info-fields input, .info-fields select {
            width: 70%;
            padding: 6px 10px;
            border: 1px solid #e5e5e5;
            border-radius: 4px;
            margin-left: 10px;
            font-size: 14px;
        }
        .info-row {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }
        .info-row button[type=submit] {
            background: #1faebc;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 8px 24px;
            font-size: 15px;
            cursor: pointer;
            margin-left: 10px;
            transition: background 0.2s;
        }
        .info-row button[type=submit]:hover {
            background: #19a8b6;
        }
        /* ====== 美化教育经历和项目经历表格 ====== */
        .data-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
            margin-bottom: 30px;
            overflow: hidden;
        }
        .data-table th {
            background: #1faebc;
            color: #fff;
            font-weight: bold;
            padding: 12px 8px;
            border-bottom: 2px solid #e5e5e5;
            text-align: left;
        }
        .data-table td {
            padding: 10px 8px;
            border-bottom: 1px solid #f0f0f0;
            font-size: 15px;
            background: #fff;
        }
        .data-table tr:nth-child(even) td {
            background: #f8fafd;
        }
        .data-table tr:hover td {
            background: #e6f7fa;
            transition: background 0.2s;
        }
        .data-table input[type='text'],
        .data-table input[type='date'] {
            width: 90%;
            padding: 5px 8px;
            border: 1px solid #d0d7de;
            border-radius: 4px;
            font-size: 14px;
        }
        .data-table button[type='submit'] {
            background: #1faebc;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 5px 16px;
            font-size: 14px;
            cursor: pointer;
            transition: background 0.2s;
        }
        .data-table button[type='submit']:hover {
            background: #19a8b6;
        }
    </style>
</head>
<body>
<%--<jsp:include page="top.jsp"></jsp:include>--%>
<div class="tn-grid resume-container">
    <div class="resume-body">
        <!-- 左侧简历内容 -->
        <div class="resume-left">
            <!-- 基本信息 -->
            <div class="resume-section">
                <div class="section-header">
                    <h3>基本信息</h3>
                </div>
                <div class="section-content clearfix">
                    <div class="profile-pic">
                        <div class="pic-box">
                            <img id="headShotImg" src="<c:choose><c:when test='${not empty basicInfo.headShot}'>images/profile/${basicInfo.headShot}</c:when><c:otherwise>images/profile_placeholder.png</c:otherwise></c:choose>" width="120" height="120"/>
                        </div>
                        <form action="ResumeServlet" method="post" enctype="multipart/form-data" style="margin-top:10px;">
                            <input type="hidden" name="action" value="uploadHeadShot"/>
                            <input type="file" name="headShotFile" accept="image/*" style="margin-bottom:5px;" required/>
                            <button type="submit" style="width:100%;">上传头像</button>
                        </form>
                    </div>
                    <div class="info-fields">
                        <form action="ResumeServlet" method="post">
                            <input type="hidden" name="action" value="saveBasicInfo"/>
                            <div class="info-row"><span>姓名：</span><input name="realname" value="${basicInfo.realname}"/></div>
                            <div class="info-row"><span>性别：</span>
                                <select name="gender">
                                    <option value="男" ${basicInfo.gender=="男"?"selected":''}>男</option>
                                    <option value="女" ${basicInfo.gender=="女"?"selected":''}>女</option>
                                </select>
                            </div>
                            <div class="info-row"><span>出生日期：</span><input name="birthday" type="date" value="<fmt:formatDate value='${basicInfo.birthday}' pattern='yyyy-MM-dd'/>"/></div>
                            <div class="info-row"><span>当前所在地：</span><input name="currentLoc" value="${basicInfo.currentLoc}"/></div>
                            <div class="info-row"><span>户口所在地：</span><input name="residentLoc" value="${basicInfo.residentLoc}"/></div>
                            <div class="info-row"><span>手机：</span><input name="telephone" value="${basicInfo.telephone}"/></div>
                            <div class="info-row"><span>邮件：</span><input name="email" value="${basicInfo.email}"/></div>
                            <div class="info-row"><span>求职意向：</span><input name="jobIntension" value="${basicInfo.jobIntension}"/></div>
                            <div class="info-row"><span>工作经验：</span><input name="jobExperience" value="${basicInfo.jobExperience}"/></div>
                            <div class="info-row"><span>头像文件名：</span><input name="headShot" value="${basicInfo.headShot}"/></div>
                            <div class="info-row" style="justify-content: flex-end;">
                                <button type="submit">保存</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- 教育经历 -->
            <div class="resume-section">
                <div class="section-header">
                    <h3>教育经历</h3>
                </div>
                <div class="section-content">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th style="width: 40%;">毕业院校</th>
                            <th style="width: 25%;">就读时间</th>
                            <th style="width: 15%;">学历</th>
                            <th style="width: 20%;">专业</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${educationList}" var="edu">
                            <tr>
                                <td>${edu.school}</td>
                                <td><fmt:formatDate value='${edu.startDate}' pattern='yyyy-MM-dd'/> ~ <fmt:formatDate value='${edu.endDate}' pattern='yyyy-MM-dd'/></td>
                                <td>${edu.degree}</td>
                                <td>${edu.major}</td>
                                <td>
                                    <form action="ResumeServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="deleteEducation"/>
                                        <input type="hidden" name="educationId" value="${edu.educationId}"/>
                                        <button type="submit">删除</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <form action="ResumeServlet" method="post">
                                <input type="hidden" name="action" value="addEducation"/>
                                <td><input name="school" required/></td>
                                <td><input name="startDate" type="date" required/> ~ <input name="endDate" type="date" required/></td>
                                <td><input name="degree" required/></td>
                                <td><input name="major" required/></td>
                                <td><button type="submit">添加</button></td>
                            </form>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- 项目经验 -->
            <div class="resume-section">
                <div class="section-header">
                    <h3>项目经验</h3>
                </div>
                <div class="section-content">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th style="width: 40%;">项目名称</th>
                            <th style="width: 30%;">参与时间</th>
                            <th style="width: 30%;">担任职位</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${projectList}" var="p">
                            <tr>
                                <td>${p.projectName}</td>
                                <td><fmt:formatDate value='${p.startDate}' pattern='yyyy-MM-dd'/> ~ <fmt:formatDate value='${p.endDate}' pattern='yyyy-MM-dd'/></td>
                                <td>${p.position}</td>
                                <td>
                                    <form action="ResumeServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="deleteProject"/>
                                        <input type="hidden" name="projectId" value="${p.projectId}"/>
                                        <button type="submit">删除</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <form action="ResumeServlet" method="post">
                                <input type="hidden" name="action" value="addProject"/>
                                <td><input name="projectName" required/></td>
                                <td><input name="startDate" type="date" required/> ~ <input name="endDate" type="date" required/></td>
                                <td><input name="position" required/></td>
                                <td><button type="submit">添加</button></td>
                            </form>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- 简历附件 -->
            <div class="resume-section">
                <div class="section-header">
                    <h3>简历附件</h3>
                </div>
                <div class="section-content attachment-placeholder">
                    <c:if test="${empty attachmentList}">暂无附件!</c:if>
                    <c:forEach items="${attachmentList}" var="att">
                        <div>
                            <a href="${att.filePath}" target="_blank">${att.fileName}</a>
                            <form action="ResumeServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteAttachment"/>
                                <input type="hidden" name="attachmentId" value="${att.attachmentId}"/>
                                <button type="submit">删除</button>
                            </form>
                        </div>
                    </c:forEach>
                    <form action="ResumeServlet" method="post" style="margin-top:10px;">
                        <input type="hidden" name="action" value="addAttachment"/>
                        <input name="fileName" placeholder="文件名" required/>
                        <input name="filePath" placeholder="文件路径" required/>
                        <button type="submit">添加</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- 右侧操作栏 -->
        <div class="resume-right">
            <div class="action-box">
                <a href="#" class="action-btn btn-preview">简历预览</a>
                <a href="#" class="action-btn btn-save">保存简历</a>
                <div class="completeness">
                    <p>简历完整度0%</p>
                    <div class="progress-bar-bg">
                        <div class="progress-bar-fg" style="width: 15%;"></div> <!-- 15% 对应已填写的 "基本信息"-->
                    </div>
                </div>
                <ul class="checklist">
                    <li class="completed"><span class="icon"></span>基本信息</li>
                    <li class="incomplete"><span class="icon"></span>教育经历</li>
                    <li class="incomplete"><span class="icon"></span>项目经验</li>
                    <li class="incomplete"><span class="icon"></span>简历照片</li>
                    <li class="incomplete"><span class="icon"></span>简历附件</li>
                </ul>
                <div class="legend">
                    <span class="legend-completed">✓</span>已填写
                    <span class="legend-incomplete">△</span>暂未填写
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<%--<iframe src="foot.jsp" width="100%" height="150" scrolling="no"
        frameborder="0"></iframe>--%>
</body>
</html>