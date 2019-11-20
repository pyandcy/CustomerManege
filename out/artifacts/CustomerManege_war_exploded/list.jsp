<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>客户列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <body>
<h3 align="center">客户列表</h3>
<table border="1" width="70%" align="center">
	<tr>
		<th>客户姓名</th>
		<th>性别</th>
		<th>生日</th>
		<th>手机</th>
		<th>邮箱</th>
		<th>描述</th>
		<th>操作</th>
	</tr>
	<c:forEach items="${pb.beanList}" var="cstm">
		<tr>
			<td>${cstm.cname}</td>
			<td>${cstm.gender}</td>
			<td>${cstm.birthday}</td>
			<td>${cstm.cellphone}</td>
			<td>${cstm.email}</td>
			<td>${cstm.description}</td>
			<td>
				<%--<a href="<c:url value='/CustomerServlet?method=preEdit&cid=${cstm.cid}'/>">编辑</a>--%>
				<a href="<c:url value='/CustomerServlet?method=preEdit&cid=${cstm.cid}'/> ">编辑</a>
				<a onclick="return confirm('确定删除吗？')" href="<c:url value='/CustomerServlet?method=delet&cid=${cstm.cid}'/>">删除</a>
			</td>
		</tr>
	</c:forEach>
</table>
  <br>
  <center>
第${pb.pc}页/共${pb.tp}页
	  <a href="${pb.url}&pc=1">首页</a>
	<c:if test="${pb.pc>1}">
		<a href="${pb.url}&pc=${pb.pc-1}">上一页</a>
	</c:if>

	  <%------------------------------------ --%>
	  <%-- 页码列表的长度自己定，10个长 --%>
	  <%--<c:choose>--%>
		  <%--&lt;%&ndash; 第一条：如果总页数<=10，那么页码列表为1 ~ tp &ndash;%&gt;--%>
		  <%--<c:when test="${pb.tp <= 10 }">--%>
			  <%--<c:set var="begin" value="1"/>--%>
			  <%--<c:set var="end" value="${pb.tp }"/>--%>
		  <%--</c:when>--%>
		  <%--<c:otherwise>--%>
			  <%--&lt;%&ndash; 第二条：按公式计算，让列表的头为当前页-4；列表的尾为当前页+5 &ndash;%&gt;--%>
			  <%--<c:set var="begin" value="${pb.pc-4 }"/>--%>
			  <%--<c:set var="end" value="${pb.pc+5 }"/>--%>

			  <%--&lt;%&ndash; 第三条：第二条只适合在中间，而两端会出问题。这里处理begin出界！ &ndash;%&gt;--%>
			  <%--&lt;%&ndash; 如果begin<1，那么让begin=1，相应end=10 &ndash;%&gt;--%>
			  <%--<c:if test="${begin<1 }">--%>
				  <%--<c:set var="begin" value="1"/>--%>
				  <%--<c:set var="end" value="10"/>--%>
			  <%--</c:if>--%>
			  <%--&lt;%&ndash; 第四条：处理end出界。如果end>tp，那么让end=tp，相应begin=tp-9 &ndash;%&gt;--%>
			  <%--<c:if test="${end>pb.tp }">--%>
				  <%--<c:set var="begin" value="${pb.tp-9 }"/>--%>
				  <%--<c:set var="end" value="${pb.tp }"/>--%>
			  <%--</c:if>--%>
		  <%--</c:otherwise>--%>
	  <%--</c:choose>--%>
	  <c:choose>
		  <c:when test="${pb.tp<=10}" >
			  <c:set var="begin" value="1"></c:set>
			  <c:set var="end" value="${pb.tp}"></c:set>
		  </c:when>
		  <c:otherwise>
			  <c:set var="begin" value="${pb.pc-4}"></c:set>
			  <c:set var="end" value="${pb.pc+5}"></c:set>
			  <c:if test="${begin<1}" >
				  <c:set var="begin" value="1"></c:set>
				  <c:set var="end" value="10"></c:set>
			  </c:if>
			  <c:if test="${end>pb.tp}" >
				  <c:set var="begin" value="${pb.tp-9}"></c:set>
				  <c:set var="end" value="${pb.tp}"></c:set>
			  </c:if>
		  </c:otherwise>
	  </c:choose>

	  <%-- 循环显示页码列表 --%>
	  <%--<c:forEach begin="${begin }" end="${end }" var="i">--%>
				  <%--<a href="${pb.url}&pc=${i}">[${i }]</a>--%>
	  <%--</c:forEach>--%>
	  <c:forEach begin="${begin}" end="${end}" var="i">
		  <a href="${pb.url}&pc=${i}">[${i} ]</a>
	  </c:forEach>
	<c:if test="${pb.pc<pb.tp}">
		<a href="${pb.url}&pc=${pb.pc+1}">下一页</a>
	</c:if>
	  <a href="${pb.url}&pc=${pb.tp}">尾页</a>
  </center>
  </body>
</html>
