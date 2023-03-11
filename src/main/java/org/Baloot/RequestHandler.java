package org.Baloot;
import io.javalin.Javalin;

public class RequestHandler {
    private static final String SERVICE_API = "http://5.253.25.110:5000";
    private static final String USERS_API = SERVICE_API + "/api/users";
    private static final String COMMODITY_API = SERVICE_API + "/api/commodities";
    private static final String PROVIDERS_API = SERVICE_API + "/api/providers";
    private static final String COMMENTS_API = SERVICE_API + "/api/comments";

    private static void getRequest() {
        Javalin app = Javalin.create().start(8081);
        app.get("/", ctx -> ctx.result("Welcome to IEMDB!"));

        app.get("/movies", context -> {
            Document template = getMovies();
            context.html(template.html());
        });

        app.get("/movies/{movie_id}", context -> {
            Document template = getMovie(context.pathParam("movie_id"));
            context.html(template.html());
        });

        app.get("/actors/{actor_id}", context -> {
            Document template = getActor(context.pathParam("actor_id"));
            context.html(template.html());
        });

        app.get("/watchList/{user_id}", context -> {
            Document template = getWatchList(context.pathParam("user_id"));
            context.html(template.html());
        });

        app.get("/watchList/{user_id}/{movie_id}", context -> {
            Document template = addToWatchList(context.pathParam("user_id"),context.pathParam("movie_id"));
            context.html(template.html());
        });

        app.post("/add_to_watch/{movie_id}/{user_id}", context -> {
            Document template = addToWatchList(context.pathParam("user_id"),context.pathParam("movie_id"));
            context.html(template.html());
            context.redirect("/watchList/" + context.pathParam("user_id"));
        });

        app.post("/removeFromWatchList/{user_id}/{movie_id}", context -> {
            Document template = removeFromWatchList(context.pathParam("user_id"),context.pathParam("movie_id"));
            context.html(template.html());
            context.redirect("/watchList/" + context.pathParam("user_id"));
        });

        app.get("/rateMovie/{user_id}/{movie_id}/{rate}", context -> {
            Document template = rateMovie(context.pathParam("user_id"),context.pathParam("movie_id"),context.pathParam("rate"));
            context.html(template.html());
        });

        app.post("/rate/{movie_id}/{user_id}", context -> {
            Document template = rateMovie(context.pathParam("user_id"),context.pathParam("movie_id"),context.formParam("quantity"));
            context.html(template.html());
            context.redirect("/movielogin/" + context.pathParam("movie_id") + "/" + context.pathParam("user_id"));
        });

        app.post("/movie_user/{movie_id}", context -> {
            context.redirect("/movielogin/" + context.pathParam("movie_id") + "/" + context.formParam("user_id"));
        });

        app.get("/movielogin/{movie_id}/{user_id}", context -> {
            Document template = getMovieUser(context.pathParam("movie_id"), context.pathParam("user_id"));
            context.html(template.html());
        });

        app.get("/voteComment/{user_id}/{comment_id}/{vote}", context -> {
            Document template = voteComment(context.pathParam("user_id"),context.pathParam("comment_id"),context.pathParam("vote"));
            context.html(template.html());
        });

        app.post("/vote/{movie_id}/{comment_id}/{user_id}/{vote}", context -> {
            Document template = voteComment(context.pathParam("user_id"),context.pathParam("comment_id"),context.pathParam("vote"));
            context.html(template.html());
            context.redirect("/movielogin/" + context.pathParam("movie_id") + "/" + context.pathParam("user_id"));
        });

        app.get("/movies/search/{genre}", context -> {
            Document template = getMovieByGenre(context.pathParam("genre"));
            context.html(template.html());
        });

        app.get("/movies/search/{start_year}/{end_year}", context -> {
            Document template = getMovieByYear(context.pathParam("start_year"), context.pathParam("end_year"));
            context.html(template.html());
        });
    }
}
