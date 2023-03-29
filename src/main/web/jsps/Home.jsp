<%@ page import="org.Baloot.Baloot" %>
<%@ page import="org.Baloot.Entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.Baloot.Managers.UserManager"%>
<html>
<head>
    <title>Home</title>
</head>
<body>
<ul>
    <%
        Baloot baloot = Baloot.getBaloot();
        User loggedInUser = baloot.userManager.getLoggedInUser();
    %>
    <li id="username">Username: <%=loggedInUser.getUsername()%></li>
    <li id="credit">Credit: <%=loggedInUser.getCredit()%></li>
    <li>
        <a href="/commodities">commodities</a>
    </li>
    <li>
        <a href="/buyList">Buy List</a>
    </li>
    <li>
        <a href="/credit">Add Credit</a>
    </li>
    <li>
        <a href="/logout">Log Out</a>
    </li>
</ul>
</body>
</html>
