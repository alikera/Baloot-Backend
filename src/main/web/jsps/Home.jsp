<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.Baloot.Managers.UserManager"%>
<html>
<head>
    <title>Home</title>
</head>
<body>
<ul>
<%--    <li id="username">Username: <%=UserManager.getLoggedInUser().getUsername()%></li>--%>
    <li>
        <a href="/commodities">commodities</a>
    </li>
    <li>
        <a href="/buyList">Buy List</a>
    </li>
    <li>
        <a href="/addCredit">Add Credit</a>
    </li>
    <li>
        <a href="/logout">Log Out</a>
    </li>
</ul>
</body>
</html>
