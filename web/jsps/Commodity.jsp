<%@ page import="org.Baloot.Entities.Commodity" %>
<%@ page import="org.Baloot.Database.Database" %>
<%@ page import="org.Baloot.Baloot" %>
<%@ page import="org.Baloot.Entities.User" %>
<%@ page import="org.Baloot.Entities.Comment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Baloot baloot = Baloot.getBaloot();
    User loggedInUser = baloot.userManager.getLoggedInUser();
    Integer commodityId = Integer.valueOf(request.getParameter("commodityId"));
    Commodity commodityFound = Database.findByCommodityId(commodityId);
%>
<head>
    <meta charset="UTF-8" />
    <title>Commodity</title>
    <style>
        li {
            padding: 5px;
        }
        table {
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
    <a href="/">Home</a>
    <span>username:  <%=loggedInUser.getUsername()%> </span>
    <br>
    <ul>
        <li id="id">Id: <%=commodityFound.getId()%></li>
        <li id="name">Name: <%=commodityFound.getName()%></li>
        <li id="providerName">Provider Name: <%=commodityFound.getProviderId()%></li>
        <li id="price">Price: <%=commodityFound.getPrice()%></li>
        <li id="categories">Categories: <%=String.join(", ", commodityFound.getCategories())%></li>
        <li id="rating">Rating: <%=commodityFound.getRating()%></li>
        <li id="inStock">In Stock: <%=commodityFound.getInStock()%></li>
    </ul>

    <form action="" method="post">
        <label>Add Your Comment:</label>
        <input type="text" name="comment_text" value="" />
        <button type="submit" name="action" value="comment">submit</button>
    </form>
    <br>
    <form action="" method="POST">
        <label>Rate(between 1 and 10):</label>
        <input type="number" id="quantity" name="quantity" min="1" max="10">
        <button type="submit" name="action" value="rate">Rate</button>
    </form>
    <br>
    <form action="" method="POST">
        <button type="submit" name="action" value="add">Add to BuyList</button>
    </form>
    <br/>
    <table>
        <caption><h2>Comments</h2></caption>
        <tr>
            <th>username</th>
            <th>comment</th>
            <th>date</th>
            <th>likes</th>
            <th>dislikes</th>
        </tr>
        <% List<Comment> comments = baloot.commentManager.getCommentsByCommodityId(commodityId);%>
        <% for (Comment comment : comments) {
        %>
        <tr>
            <td><%=comment.getUserEmail()%></td>
            <td><%=comment.getText()%></td>
            <td><%=comment.getDate()%></td>
            <td>
                <form action="" method="POST">
                    <label><%=comment.getLikes()%></label>
                    <input type="hidden" name="comment_reaction" value="1"/>
                    <input type="hidden" name="comment_id" value=<%=comment.getId()%>>
                    <button type="submit" name="action" value="react">like</button>
                </form>
            </td>
            <td>
                <form action="" method="POST">
                    <label><%=comment.getDislikes()%></label>
                    <input type="hidden" name="comment_reaction" value="-1"/>
                    <input type="hidden" name="comment_id" value=<%=comment.getId()%>>
                    <button type="submit" name="action" value="react">dislike</button>
                </form>
            </td>
        </tr>
        <%  } %>
    </table>
    <br><br>
    <table>
        <caption><h2>Suggested Commodities</h2></caption>
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
        <% List<Commodity> commodities = baloot.commodityManager.getSuggestedCommodities(commodityFound, commodityFound.getCategories());%>
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
