<%
    String tab = request.getParameter("tab");
    if (tab == null) tab = "resume";
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
    .resume-nav {
        background: #fff;
        border-bottom: 2px solid #1faebc;
        margin-bottom: 0;
    }

    .resume-nav ul {
        list-style: none;
        margin: 0;
        padding: 0;
        display: flex;
    }

    .resume-nav li {
        margin: 0 10px 0 0;
    }

    .resume-nav li a {
        display: block;
        padding: 10px 30px;
        font-size: 16px;
        font-weight: bold;
        color: #1faebc;
        border-radius: 6px 6px 0 0;
        background: #f7f7f7;
        transition: background 0.2s, color 0.2s;
        text-decoration: none;
    }

    .resume-nav li.active a, .resume-nav li a:focus {
        background: #1faebc;
        color: #fff;
    }

    .resume-nav li a:hover {
        background: #e0f7fa;
        color: #1faebc;
    }
</style>
<div class="resume-nav">
    <ul>
        <li <%= "resume".equals(tab) ? "class=\"active\"" : "" %> >
            <a href="?tab=resume" onclick="showTab('resume');return false;">myResume</a>
        </li>
        <li <%= "apply".equals(tab) ? "class=\"active\"" : "" %> >
            <a href="?tab=apply" onclick="showTab('apply');return false;">myApplication</a>
        </li>
    </ul>
</div>
<script>
    function showTab(tab) {
        var iframe = document.getElementById('resume-iframe');
        if (tab === 'resume') {
            iframe.src = '${pageContext.request.contextPath}/ResumeServlet?iframe=resume';
        } else {
            iframe.src = '${pageContext.request.contextPath}/JobApplyServlet?type=myapply&iframe=apply';
        }
        // 动态高亮tab
        var lis = document.querySelectorAll('.resume-nav li');
        lis.forEach(function (li) {
            li.classList.remove('active');
        });
        document.querySelector('.resume-nav li' + (tab === 'resume' ? ':first-child' : ':last-child')).classList.add('active');
    }
</script>