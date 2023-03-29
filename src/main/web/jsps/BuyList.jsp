<%@ page import="org.Baloot.Baloot" %>
<%@ page import="org.Baloot.Entities.Commodity" %>
<%@ page import="org.Baloot.Entities.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<html lang="en"><head>
  <meta charset="UTF-8">
  <title>User</title>
  <style>
    li {
      padding: 5px
    }
    table{
      width: 100%;
      text-align: center;
    }
  </style>
</head>
<body>
<ul>
  <%
    Baloot baloot = Baloot.getBaloot();
    User loggedInUser = baloot.userManager.getLoggedInUser();
  %>
  <li id="username">Username: <%=loggedInUser.getUsername()%></li>
  <li id="email">Email: <%=loggedInUser.getEmail()%></li>
  <li id="birthDate">Birth Date: <%=loggedInUser.getDate().getAsString()%></li>
  <li id="address"><%=loggedInUser.getAddress()%></li>
  <li id="credit">Credit: <%=loggedInUser.getCredit()%></li>
  <%
    List<Commodity> commodities = new ArrayList<>();
    double currentBuyListPrice = 0;
    for (int commodityId : loggedInUser.getBuyList()) {
      Commodity commodity = baloot.getCommodityById(commodityId);
      currentBuyListPrice += commodity.getPrice();
      commodities.add(commodity);
    }
  %>
  <li>Current Buy List Price: <%=currentBuyListPrice%></li>
  <li>
    <a href="${pageContext.request.contextPath}/credit">Add Credit</a>
  </li>
  <li>
    <form action="" method="POST">
      <label>Submit & Pay</label>
      <input id="form_payment" type="hidden" name="userId" value=<%=loggedInUser.getUsername()%>>
      <button type="submit">Payment</button>
    </form>
  </li>
</ul>
<table>
  <caption>
    <h2>Buy List</h2>
  </caption>
  <tbody><tr>
    <th>Id</th>
    <th>Name</th>
    <th>Provider Name</th>
    <th>Price</th>
    <th>Categories</th>
    <th>Rating</th>
    <th>In Stock</th>
    <th></th>
    <th></th>
  </tr>
  <% for (Commodity commodity : commodities) { %>
  <tr>
    <td><%=commodity.getId()%></td>
    <td><%=commodity.getName()%></td>
    <td><%=baloot.getProviderById(commodity.getProviderId()).getName()%></td>
    <td><%=commodity.getPrice()%></td>
    <td><%=String.join(", ", commodity.getCategories())%></td>
    <td><%=commodity.getRating()%></td>
    <td><%=commodity.getInStock()%></td>
    <td><a href="/commodities/<%=commodity.getId()%>">Link</a></td>
    <td>
      <form action="" method="POST">
        <input id="form_commodity_id" type="hidden" name="commodityId" value="<%=commodity.getId()%>">
        <button type="submit">Remove</button>
      </form>
    </td>
  </tr>

  <% } %>
  </tbody></table>
</body></html>