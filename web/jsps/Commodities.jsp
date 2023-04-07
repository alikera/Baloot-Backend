<%@ page import="org.Baloot.Baloot" %>
<%@ page import="org.Baloot.Entities.Commodity" %>
<%@ page import="org.Baloot.Entities.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Baloot baloot = Baloot.getBaloot();
    User loggedInUser = baloot.userManager.getLoggedInUser();
    List<Commodity> commodities;
    if(request.getAttribute("commodities") != null) {
        commodities = (List<Commodity>) request.getAttribute("commodities");
    } else{
        commodities = baloot.getCommodities();
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Commodities</title>
    <style>
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
    <a href="/">Home</a>
    <p id="username">username: <%=loggedInUser.getUsername()%></p>
    <br><br>
    <form action="" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search_by_category">Search By Category</button>
        <button type="submit" name="action" value="search_by_name">Search By Name</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_rate">Rate</button>
    </form>
    <br><br>
    <table>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Provider Name</th>
            <th>Price</th>
            <th>Categories</th>
            <th>Rating</th>
            <th>In Stock</th>
            <th>Links</th>
        </tr>
        <% for(Commodity commodity : commodities) { %>
        <tr>
            <td><%=commodity.getId()%></td>
            <td><%=commodity.getName()%></td>
            <td><%=commodity.getProviderId()%></td>
            <td><%=commodity.getPrice()%></td>
            <td><%=String.join(", ", commodity.getCategories())%></>
            <td><%=commodity.getRating()%></td>
            <td><%=commodity.getInStock()%></td>
            <td><a href="/commodities/<%=commodity.getId()%>">Link</a></td>
        </tr>
        <% } %>

    </table>
</body>
</html>
