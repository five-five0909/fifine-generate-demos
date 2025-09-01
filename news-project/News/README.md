# 新闻管理系统 - 课程设计报告

## 1. 项目概述

### 1.1. 项目简介

本项目是一个基于 `Java Web` 技术栈实现的动态新闻发布与管理系统。系统实现了新闻信息的前后台分离，为前台用户提供了新闻浏览、分类查看和发表评论的功能；为后台管理员提供了对新闻、新闻类别、评论及友情链接进行全面管理的平台。

该系统旨在模拟一个真实的新闻网站的核心功能，涵盖了从数据库设计、后端逻辑处理到前端页面展示的完整 `Web` 开发流程。

### 1.2. 功能模块

系统主要分为两大模块：前台展示模块和后台管理模块。

**前台展示模块:**

* **首页:** 动态展示最新的头条新闻、图片新闻、热点新闻以及各个分类下的新闻列表。
* **新闻列表页:** 根据新闻分类展示该分类下的所有新闻，并支持分页浏览。
* **新闻详情页:** 展示新闻的完整内容，包括标题、作者、发布时间、点击量，并提供评论功能和上一篇/下一篇的快速导航。
* **评论功能:** 用户可以在新闻详情页发表自己的评论。

**后台管理模块:**

* **管理员登录/注销:** 提供安全的后台登录入口。
* **新闻管理:** 实现新闻的增、删、改、查（CRUD）功能，支持富文本编辑器进行内容编辑和图片上传。
* **新闻类别管理:** 实现新闻类别的增、删、改、查功能。
* **评论管理:** 管理员可以查看所有新闻的评论，并对不良评论进行删除。
* **友情链接管理:** 管理员可以添加、修改或删除网站的友情链接。

### 1.3. 技术栈

* **后端核心:** `Servlet`, `JSP`
* **前端技术:** `HTML`, `CSS`, `JavaScript`, `jQuery`, `Bootstrap`
* **数据库:** `MySQL`
* **Web 服务器:** `Tomcat`
* **开发工具:** `Eclipse` / `IntelliJ IDEA`
* **JavaBean:** `POJO` (Plain Old Java Objects) 作为数据模型
* **DAO:** `Data Access Object` 设计模式处理数据库操作

---

## 2. 系统设计

### 2.1. 架构设计

本系统采用经典的 **MVC (Model-View-Controller)** 设计模式，这是一种将应用程序的逻辑、数据和界面显示分离的设计范式。

* **Model (模型):** 由 `JavaBean` (如 `News.java`, `User.java` 等) 和 `DAO` (Data Access Object, 如 `NewsDao.java`) 组成。
  
  * `JavaBean` 用于封装业务数据，作为数据传输的载体。
  * `DAO` 层负责与数据库进行直接交互，执行 SQL 语句，完成数据的增删改查操作。

* **View (视图):** 由 `JSP` (JavaServer Pages) 文件组成 (如 `index.jsp`, `newsList.jsp` 等)。
  
  * 负责渲染用户界面，将模型层提供的数据动态地展示给用户。

* **Controller (控制器):** 由 `Servlet` (如 `NewsServlet.java`, `UserServlet.java` 等) 组成。
  
  * 作为系统的核心控制器，接收来自客户端的 HTTP 请求。
  * 调用相应的模型（DAO）来处理业务逻辑和数据操作。
  * 根据处理结果，选择合适的视图（JSP）进行响应，并将处理后的数据转发给视图进行渲染。

![MVC Architecture](https://i.imgur.com/UaZzX5X.png)

### 2.2. 目录结构

项目的目录结构清晰地反映了其 MVC 架构：

```
News/
├── src/
│   ├── com/artisan/
│   │   ├── dao/         # 数据访问对象 (DAO) - Model
│   │   ├── model/       # JavaBean 实体类 - Model
│   │   ├── util/        # 工具类 (数据库、字符串、日期等)
│   │   └── web/         # Servlet 控制器 - Controller
│   └── news.properties  # 数据库及项目配置文件
├── webapp/
│   ├── background/      # 后台管理页面 - View
│   ├── foreground/      # 前台展示页面 - View
│   ├── bootstrap/       # Bootstrap 框架
│   ├── js/              # JavaScript 库 (jQuery, CKEditor)
│   ├── style/           # 自定义 CSS 样式
│   ├── WEB-INF/
│   │   ├── lib/         # 项目依赖的 JAR 包
│   │   └── web.xml      # Web 应用部署描述文件
│   └── index.jsp        # 网站首页
└── db_news.sql          # 数据库初始化脚本
```

### 2.3. 数据库设计

系统使用 MySQL 数据库，库名为 `db_news`。包含了以下 5 张核心表：

1. **`t_user` (用户表)**
   * 存储后台管理员的登录信息。

| 字段名        | 类型            | 约束     | 描述   |
|:---------- |:------------- |:------ |:---- |
| `userId`   | `int`         | 主键, 自增 | 用户ID |
| `userName` | `varchar(20)` |        | 用户名  |
| `password` | `varchar(20)` |        | 密码   |

2. **`t_newsType` (新闻类别表)**
   * 存储新闻的分类信息。

| 字段名          | 类型            | 约束     | 描述   |
|:------------ |:------------- |:------ |:---- |
| `newsTypeId` | `int`         | 主键, 自增 | 类别ID |
| `typeName`   | `varchar(20)` |        | 类别名称 |

3. **`t_news` (新闻表)**
   * 核心表，存储所有新闻的详细信息。

| 字段名           | 类型            | 约束              | 描述              |
|:------------- |:------------- |:--------------- |:--------------- |
| `newsId`      | `int`         | 主键, 自增          | 新闻ID            |
| `title`       | `varchar(40)` |                 | 新闻标题            |
| `content`     | `text`        |                 | 新闻内容 (HTML格式)   |
| `publishDate` | `datetime`    |                 | 发布日期            |
| `author`      | `varchar(20)` |                 | 作者              |
| `typeId`      | `int`         | 外键 (t_newsType) | 所属类别ID          |
| `click`       | `int`         |                 | 点击量             |
| `isHead`      | `tinyint`     |                 | 是否头条 (1是, 0否)   |
| `isImage`     | `tinyint`     |                 | 是否图片新闻 (1是, 0否) |
| `imageName`   | `varchar(20)` |                 | 关联的图片文件名        |
| `isHot`       | `tinyint`     |                 | 是否热点 (1是, 0否)   |

4. **`t_comment` (评论表)**
   * 存储用户对新闻的评论。

| 字段名           | 类型             | 约束          | 描述       |
|:------------- |:-------------- |:----------- |:-------- |
| `commentId`   | `int`          | 主键, 自增      | 评论ID     |
| `newsId`      | `int`          | 外键 (t_news) | 评论所属新闻ID |
| `content`     | `varchar(200)` |             | 评论内容     |
| `userIP`      | `varchar(40)`  |             | 评论者IP地址  |
| `commentDate` | `datetime`     |             | 评论时间     |

5. **`t_link` (友情链接表)**
   * 存储网站的友情链接信息。

| 字段名         | 类型            | 约束     | 描述   |
|:----------- |:------------- |:------ |:---- |
| `linkId`    | `int`         | 主键, 自增 | 链接ID |
| `linkName`  | `varchar(20)` |        | 链接名称 |
| `linkUrl`   | `varchar(40)` |        | 链接地址 |
| `linkEmail` | `varchar(40)` |        | 联系邮箱 |
| `orderNum`  | `int`         |        | 排序号  |

---

## 3. 核心模块实现详解

### 3.1. 数据库连接

数据库的连接信息配置在 `src/news.properties` 文件中，通过 `DbUtil` 工具类进行读取和连接管理。这种方式将配置与代码分离，便于修改和维护。

**`DbUtil.java` 关键代码:**

```java
package com.artisan.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {

    public Connection getCon()throws Exception{
        // 1. 加载驱动
        Class.forName(PropertiesUtil.getValue("jdbcName"));
        // 2. 获取连接
        Connection con=DriverManager.getConnection(PropertiesUtil.getValue("dbUrl"), PropertiesUtil.getValue("dbUserName"), PropertiesUtil.getValue("dbPassword"));
        return con;
    }

    public void closeCon(Connection con)throws Exception{
        if(con!=null){
            con.close();
        }
    }
}
```

### 3.2. 用户登录模块

* **Controller:** `UserServlet.java`
* **DAO:** `UserDao.java`
* **View:** `background/login.jsp`

**流程:**

1. 用户在 `login.jsp` 页面输入用户名和密码，表单提交到 `UserServlet`。
2. `UserServlet` 的 `doPost` 方法接收请求，获取 `action=login` 参数。
3. 调用 `login` 方法，获取用户名和密码，并封装成一个 `User` 对象。
4. 调用 `UserDao.login(con, user)` 方法，在数据库中查询是否存在匹配的用户。
5. `UserDao` 执行 SQL 查询，如果查询到结果，返回一个包含用户完整信息的 `User` 对象；否则返回 `null`。
6. `UserServlet` 根据 `UserDao` 的返回结果进行判断：
   * 如果返回的 `currentUser` 不为 `null`，表示登录成功。将 `currentUser` 对象存入 `HttpSession`，并重定向到后台主页 `mainTemp.jsp`。
   * 如果为 `null`，表示登录失败。在 `request` 中设置错误信息，并转发回 `login.jsp` 页面进行提示。

**`UserDao.java` 的 `login` 方法:**

```java
public User login(Connection con, User user) throws Exception {
    User resultUser = null;
    String sql = "select * from t_user where userName=? and password=?";
    PreparedStatement pstmt = con.prepareStatement(sql);
    pstmt.setString(1, user.getUserName());
    pstmt.setString(2, user.getPassword());
    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
        resultUser = new User();
        resultUser.setUserId(rs.getInt("userId"));
        resultUser.setUserName(rs.getString("userName"));
        resultUser.setPassword(rs.getString("password"));
    }
    return resultUser;
}
```

### 3.3. 新闻管理模块

这是系统的核心模块，功能复杂，涉及前后台交互。

* **Controller:** `NewsServlet.java`
* **DAO:** `NewsDao.java`
* **View (后台):** `background/news/newsList.jsp`, `background/news/newsSave.jsp`
* **View (前台):** `foreground/news/newsList.jsp`, `foreground/news/newsShow.jsp`

#### 3.3.1. 后台新闻列表与查询

`NewsServlet` 中的 `newsBackList` 方法负责处理后台新闻列表的展示和带条件查询。它从请求中获取查询条件（如标题、发布日期范围）和分页参数，然后调用 `NewsDao.newsCount` 获取总记录数和 `NewsDao.newsList` 获取当前页的数据。最后将数据和分页代码存入 `request` 并转发到 `newsList.jsp`。

**`NewsDao.java` 的 `newsList` (后台分页查询) 方法:**

```java
public List<News> newsList(Connection con, News s_news, PageBean pageBean, String s_bPublishDate, String s_aPublishDate) throws Exception {
    List<News> newsList = new ArrayList<News>();
    StringBuffer sb = new StringBuffer("select * from t_news t1,t_newsType t2 where t1.typeId=t2.newsTypeId ");
    // 拼接查询条件
    if (s_news.getTypeId() != -1) {
        sb.append(" and t1.typeId=" + s_news.getTypeId());
    }
    if (StringUtil.isNotEmpty(s_news.getTitle())) {
        sb.append(" and t1.title like '%" + s_news.getTitle() + "%'");
    }
    // ... 其他日期条件拼接
    sb.append(" order by t1.publishDate desc ");
    // 拼接分页
    if (pageBean != null) {
        sb.append(" limit " + pageBean.getStart() + "," + pageBean.getPageSize());
    }
    PreparedStatement pstmt = con.prepareStatement(sb.toString());
    ResultSet rs = pstmt.executeQuery();
    // 遍历结果集并封装为 News 对象列表
    while (rs.next()) {
        News news = new News();
        // ... set 属性
        newsList.add(news);
    }
    return newsList;
}
```

#### 3.3.2. 新闻发布与修改

新闻的发布和修改共用 `newsSave.jsp` 页面和 `NewsServlet` 的 `newsSave` 方法。

* **预处理 (`preSave`):** 当点击“修改”时，`newsPreSave` 方法会根据 `newsId` 从数据库加载新闻的旧数据，并将其存入 `request`，然后转发到 `newsSave.jsp` 进行回显。
* **保存 (`save`):** `newsSave.jsp` 页面包含一个支持文件上传的表单，并集成了 `CKEditor` 富文本编辑器。表单提交后，`NewsServlet` 的 `newsSave` 方法使用 `commons-fileupload` 组件来解析请求。
  * 它会遍历表单中的每个字段 (`FileItem`)。
  * 如果是普通文本字段，则将其值设置到 `News` 对象中。
  * 如果是文件字段（图片），则将其保存到服务器的 `userImage` 目录下，并将生成的文件名设置到 `News` 对象的 `imageName` 属性中。
  * 最后，根据 `newsId` 是否存在来判断是执行 `newsDao.newsAdd` (新增) 还是 `newsDao.newsUpdate` (更新)。

**`NewsDao.java` 的 `newsAdd` 方法:**

```java
public int newsAdd(Connection con, News news) throws Exception {
    String sql = "insert into t_news values(null,?,?,now(),?,?,0,?,?,?,?)";
    PreparedStatement pstmt = con.prepareStatement(sql);
    pstmt.setString(1, news.getTitle());
    pstmt.setString(2, news.getContent());
    pstmt.setString(3, news.getAuthor());
    pstmt.setInt(4, news.getTypeId());
    pstmt.setInt(5, news.getIsHead());
    pstmt.setInt(6, news.getIsImage());
    pstmt.setString(7, news.getImageName());
    pstmt.setInt(8, news.getIsHot());
    return pstmt.executeUpdate();
}
```

### 3.4. 新闻类别管理模块

* **Controller:** `NewsTypeServlet.java`
* **DAO:** `NewsTypeDao.java`
* **View:** `background/newsType/newsTypeList.jsp`, `background/newsType/newsTypeSave.jsp`

该模块的实现逻辑与用户管理类似，提供了对新闻类别的增、删、改、查功能。一个关键点是在执行删除操作时，会先调用 `NewsDao.existNewsWithNewsTypeId` 方法检查该类别下是否存在新闻，如果存在，则禁止删除，以保证数据的完整性。

**`NewsTypeServlet.java` 的 `newsTypeDelete` 方法中的检查逻辑:**

```java
// ...
boolean exist = newsDao.existNewsWithNewsTypeId(con, newsTypeId);
if (exist) {
    result.put("errorMsg", "该类别下存在新闻，不能删除！");
} else {
    int delNums = newsTypeDao.newsTypeDelete(con, newsTypeId);
    // ...
}
// ...
```

---

## 4. 安装与部署

### 4.1. 环境准备

* **JDK:** 1.8 或更高版本
* **Tomcat:** 8.0 或更高版本
* **MySQL:** 5.7 或更高版本
* **IDE:** Eclipse for Java EE 或 IntelliJ IDEA Ultimate

### 4.2. 步骤

1. **克隆或下载项目:** 将项目源码下载到本地。
2. **数据库初始化:**
   * 启动 MySQL 服务。
   * 创建一个名为 `db_news` 的数据库，字符集设置为 `utf8`。
   * 使用 MySQL 客户端 (如 Navicat, MySQL Workbench 或命令行) 执行项目根目录下的 `db_news.sql` 文件，以创建表结构和导入初始数据。
3. **修改数据库配置:**
   * 打开 `src/news.properties` 文件。
   * 根据您的 MySQL 安装情况，修改 `dbUserName` 和 `dbPassword` 的值。
     
     ```properties
     dbUrl=jdbc:mysql://localhost:3306/db_news?useUnicode=true&characterEncoding=UTF-8
     dbUserName=root
     dbPassword=your_mysql_password
     ```
4. **导入并部署项目:**
   * 在 IDE (如 Eclipse) 中，选择 `File -> Import -> Existing Projects into Workspace`，将项目导入。
   * 在项目上右键，选择 `Run As -> Run on Server`，选择您配置好的 Tomcat 服务器来部署和运行项目。
5. **访问项目:**
   * **前台首页:** `http://localhost:8080/News/` (端口号和项目名可能因您的配置而异)
   * **后台登录:** `http://localhost:8080/News/background/login.jsp`
   * **默认管理员账号:**
     * 用户名: `admin`
     * 密码: `admin`

---

## 5. 总结

本新闻管理系统成功地运用 `Java Web` 的核心技术（Servlet、JSP、JDBC）和 MVC 设计模式，构建了一个功能完整、结构清晰的 Web 应用程序。通过这个项目，可以全面地练习和掌握 Web 开发的各个环节，包括需求分析、数据库设计、后端开发、前端实现和系统部署。

尽管系统功能已基本完善，但仍有可扩展的空间，例如：

* 引入 AJAX 技术实现页面无刷新评论和点赞。
* 增加用户注册和权限管理系统，实现多角色访问。
* 使用更现代化的前端框架 (如 Vue.js) 和后端框架 (如 Spring MVC) 进行重构。