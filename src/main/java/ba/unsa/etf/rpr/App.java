package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.AdminManager;
import ba.unsa.etf.rpr.business.MovieManager;
import ba.unsa.etf.rpr.business.UserManager;
import ba.unsa.etf.rpr.business.WatchlistManager;
import ba.unsa.etf.rpr.controllers.AdminController;
import ba.unsa.etf.rpr.domain.*;
import ba.unsa.etf.rpr.exceptions.MovieException;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * In the main method CLI (Command Line Interface) implementation of the application is provided.
 * Picking a specific option can give you more choices for interacting with TMDB.
 *
 * @author Anida Nezovic
 */
public class App {
    public static void main(String[] args) throws MovieException {
        System.out.println("               WELCOME TO TMDB!\n");
        System.out.println("You can choose the option you want from the provided list: ");

        MovieManager movieManager = new MovieManager();
        UserManager userManager = new UserManager();
        AdminManager adminManager = new AdminManager();
        WatchlistManager watchlistManager = new WatchlistManager();


        while(true) {
            System.out.println("1 - List of all movies in our database");
            System.out.println("2 - Login");
            System.out.println("3 - Sign up");
            System.out.println("4 - Login as Administrator");
            System.out.println("5 - Search for a movie");
            System.out.println("6 - Close the application");

            Scanner input = new Scanner(System.in);
            System.out.println("\n\nEnter option: ");

            int option = input.nextInt();
            if(option == 1) {
                System.out.println("\nThis is our full list of movies.\n");
                List<Movie> allMovies = FXCollections.observableList(movieManager.getAll());

                for(int i = 0; i < allMovies.size(); i++) {
                    System.out.println(allMovies.get(i).getId() + ". " + allMovies.get(i).getName() + "                    ");
                    System.out.println("   " + allMovies.get(i).getGenre() + "               ");
                }
            }
            else if(option == 2) {
                System.out.println("\n\nPlease enter your username and password to log in.\n");
                String username = "";
                String password = "";

                Scanner input1 = new Scanner(System.in);
                System.out.print("Enter your username: ");
                username = input1.nextLine();
                System.out.print("Enter your password: ");
                password = input1.nextLine();

                List<User> allUsers = FXCollections.observableList(userManager.getAll());

                boolean valid = false;
                String name = "";
                int userId = 0;

                for(int i = 0; i < allUsers.size(); i++) {
                    if(allUsers.get(i).getUsername().equals(username) && allUsers.get(i).getPassword().equals(password)) {
                        name = allUsers.get(i).getName() + " " + allUsers.get(i).getLastName();
                        userId = allUsers.get(i).getId();
                        valid = true;
                        break;
                    }
                }

                if(valid) {
                    System.out.println("\n\n    Welcome back, " + name + "!\n\n");

                    while(true) {
                        System.out.println("You can choose the option you want.\n");

                        System.out.println("show watchlist - Show all of m watchlists");
                        System.out.println("create watchlist - Create new watchlist");
                        System.out.println("logout - Logout");

                        Scanner input2 = new Scanner(System.in);
                        System.out.print("\nEnter option: ");
                        String userOption = input2.nextLine();

                        if(userOption.equals("show watchlist")) {
                            List<Watchlist> allWatchlists = FXCollections.observableList(watchlistManager.getAll());
                            List<String> namesOfAllWatchlists = new ArrayList<>();
                            List<String> moviesOfWatchlist = new ArrayList<>();

                            for(int i = 0; i < allWatchlists.size(); i++) {
                                if(allWatchlists.get(i).getUserId() == userId) {
                                    namesOfAllWatchlists.add(allWatchlists.get(i).getName()); //   free time, my favourite
                                    moviesOfWatchlist.add(allWatchlists.get(i).getMovies());  //  "1,4,7", "1,4"
                                }
                            }


                            Map<String, StringBuilder> movies = new HashMap<>();
                            List<Movie> allMovies = FXCollections.observableList(movieManager.getAll());

                            for(int i = 0; i < moviesOfWatchlist.size(); i++) {
                                StringBuilder namesOfAllMovies = new StringBuilder("");
                                String[] splittedMovies = moviesOfWatchlist.get(i).split(",");

                                for(int j = 0; j < splittedMovies.length; j++) {
                                    for(int k = 0; k < allMovies.size(); k++) {
                                        if(allMovies.get(k).getId() == Integer.parseInt(splittedMovies[j])) {
                                            namesOfAllMovies.append("- " + allMovies.get(k).getName() + "\n");
                                        }
                                    }
                                }

                                movies.put(namesOfAllWatchlists.get(i), namesOfAllMovies);
                            }

                            for (Map.Entry<String, StringBuilder> entry : movies.entrySet()) {
                                System.out.println(entry.getKey() + ":\n" + entry.getValue());
                            }
                        }
                        else if(userOption.equals("create watchlist")) {
                            System.out.println("\nPlease enter the required information.\n");
                            String watchlistName = "";
                            String listOfMoviesId = "";

                            Scanner input3 = new Scanner(System.in);
                            System.out.print("Enter name: ");
                            watchlistName = input3.nextLine();
                            System.out.print("Enter movie id (\",\" between them): ");
                            listOfMoviesId = input3.nextLine();

                            Watchlist watchlist = new Watchlist();

                            watchlist.setName(watchlistName);
                            watchlist.setUserId(userId);
                            watchlist.setMovies(listOfMoviesId);
                            watchlistManager.add(watchlist);

                            System.out.println("You successfully created your watchlist!");
                        }
                        else if(userOption.equals("logout")) {
                            break;
                        }
                    }
                }
                else {
                    System.out.println("Sorry, but you are not logged in. Please sign up.");
                }
            }
            else if(option == 3) {
                System.out.println("\n\nJoin the Community!\n");
                System.out.println("\nPlease enter the required information.\n");
                String name = "";
                String lastName = "";
                String email = "";
                String username = "";
                String password = "";

                Scanner input1 = new Scanner(System.in);
                System.out.print("Enter your name: ");
                name = input1.nextLine();
                System.out.print("Enter your last name: ");
                lastName = input1.nextLine();
                System.out.print("Enter your email: ");
                email = input1.nextLine();
                System.out.print("Enter your username: ");
                username = input1.nextLine();
                System.out.print("Enter your password: ");
                password = input1.nextLine();

                boolean valid = true;

                if(name.contains(" ") || username.contains(" ") || lastName.contains(" ")) {
                    valid = false;
                }

                for(int i = 0; i < name.length(); i++) {
                    if(name.charAt(i) < 'A' || (name.charAt(i) > 'Z' && name.charAt(i) < 'a') || name.charAt(i) > 'z') {
                        valid = false;
                        break;
                    }
                }
                for(int i = 0; i < lastName.length(); i++) {
                    if(lastName.charAt(i) < 'A' || (lastName.charAt(i) > 'Z' && lastName.charAt(i) < 'a') || lastName.charAt(i) > 'z') {
                        valid = false;
                        break;
                    }
                }

                String validPassword = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(validPassword);
                Matcher matcher = pattern.matcher(email);

                if(!matcher.matches()) {
                    valid = false;
                }

                if(!valid) {
                    System.out.println("Sorry, but you entered invalid information. Please check again.");
                }
                else {
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setLastName(lastName);
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    userManager.add(newUser);

                    System.out.println("You successfully signed up! Please, try to log in.");
                }
            }
            else if(option == 4) {
                System.out.println("\n\nPlease enter your username and password to log in.\n");
                String username = "";
                String password = "";

                Scanner input1 = new Scanner(System.in);
                System.out.print("Enter your username: ");
                username = input1.nextLine();
                System.out.print("Enter your password: ");
                password = input1.nextLine();

                List<Administrator> allAdmins = FXCollections.observableList(adminManager.getAll());
                boolean valid = false;

                for(int i = 0; i < allAdmins.size(); i++) {
                    if(allAdmins.get(i).getUsername().equals(username) && allAdmins.get(i).getPassword().equals(password)) {
                        valid = true;
                        break;
                    }
                }

                if(valid) {
                    System.out.println("Welcome back administrator, " + username + "!");
                    System.out.println("                   :)                   ");

                    while(true) {
                        System.out.println("\n-u - List of all users");
                        System.out.println("-m - List of all movies\n");
                        System.out.println("-d user - Delete a user");
                        System.out.println("-d movie - Delete a movie");
                        System.out.println("-a movie - Add a movie");
                        System.out.println("-q - Logout");

                        Scanner input2 = new Scanner(System.in);
                        System.out.println("Enter option: ");

                        String adminOption = input2.nextLine();

                        if(adminOption.equals("-u")) {
                            System.out.println("Users: ");
                            List<User> allUsers = FXCollections.observableList(userManager.getAll());

                            for(int i = 0; i < allUsers.size(); i++) {
                                System.out.println(allUsers.get(i).getId() + ". " + allUsers.get(i).getName() + " " + allUsers.get(i).getLastName());
                            }
                        }
                        else if(adminOption.equals("-m")) {
                            System.out.println("Movies: ");
                            List<Movie> allMovies = FXCollections.observableList(movieManager.getAll());

                            for(int i = 0; i < allMovies.size(); i++) {
                                System.out.println(allMovies.get(i).getId() + ". " + allMovies.get(i).getName());
                            }
                        }
                        else if(adminOption.equals("-d user")) {
                            Scanner input3 = new Scanner(System.in);
                            System.out.print("Enter the id of the user you want to delete: ");
                            int userId = input3.nextInt();

                            List<User> allUsers = FXCollections.observableList(userManager.getAll());

                            if(userId < 0 || userId > allUsers.size()) {
                                System.out.println("User is not deleted!");
                            }
                            else {
                                List<Watchlist> allWatchlists = FXCollections.observableList(watchlistManager.getAll());
                                List<Integer> idWatchlists = new ArrayList<>();

                                for(int i = 0; i < allWatchlists.size(); i++) {
                                    if(allWatchlists.get(i).getUserId() == userId) {
                                        idWatchlists.add(allWatchlists.get(i).getId());
                                    }
                                }

                                for(int i = 0; i < idWatchlists.size(); i++) {
                                    watchlistManager.delete(idWatchlists.get(i));
                                }

                                userManager.delete(userId);
                                System.out.println("You successfully deleted the user!");
                            }

                        }
                        else if(adminOption.equals("-d movie")) {
                            Scanner input3 = new Scanner(System.in);
                            System.out.print("Enter the id of the movie you want to delete: ");
                            int movieId = input3.nextInt();

                            List<Movie> allMovies = FXCollections.observableList(movieManager.getAll());

                            if(movieId < 0 || movieId > allMovies.size()) {
                                System.out.println("Movie is not deleted!");
                            }
                            else {
                                List<Watchlist> allWatchlists = FXCollections.observableList(watchlistManager.getAll());
                                List<Integer> idWatchlists = new ArrayList<>();

                                for(int i = 0; i < allWatchlists.size(); i++) {
                                    String[] idMovies = allWatchlists.get(i).getMovies().split(",");

                                    for(int j = 0; j < idMovies.length; j++) {
                                        if(movieId == Integer.parseInt(idMovies[j])) {
                                            idWatchlists.add(allWatchlists.get(i).getId());
                                        }
                                    }
                                }

                                for(int i = 0; i < idWatchlists.size(); i++) {
                                    watchlistManager.delete(idWatchlists.get(i));
                                }

                                movieManager.delete(movieId);
                                System.out.println("You successfully deleted the movie!");
                            }
                        }
                        else if(adminOption.equals("-a movie")) {
                            System.out.println("\nAdd a new movie to our database.\n");
                            String name = "";
                            String genre = "";
                            String duration = "";
                            String rating = "";

                            Scanner input3 = new Scanner(System.in);
                            System.out.print("Enter name: ");
                            name = input3.nextLine();
                            System.out.print("Enter genre: ");
                            genre = input3.nextLine();
                            System.out.print("Enter duration: ");
                            duration = input3.nextLine();
                            System.out.print("Enter rating: ");
                            rating = input3.nextLine();

                            Movie newMovie = new Movie();
                            newMovie.setName(name);
                            newMovie.setGenre(genre);
                            newMovie.setDuration(duration);
                            newMovie.setRating(rating);
                            movieManager.add(newMovie);

                            System.out.println("You successfully added the movie!");

                        }
                        else if(adminOption.equals("-q")) {
                            System.out.println("Goodbye!");
                            break;
                        }
                    }
                }
                else {
                    System.out.println("You don't have administrator access.");
                }
            }
            else if(option == 5) {
                System.out.println("   Search for a movie in our database!   ");
                String text = "";

                Scanner input1 = new Scanner(System.in);
                System.out.print("Enter the text to search for the movie you want: ");
                text = input1.nextLine();

                System.out.println("     Results:\n");
                List<Movie> movies = FXCollections.observableList(movieManager.searchMovies(text));
                for(int i = 0; i < movies.size(); i++) {
                    System.out.println(i + 1 + ". " + movies.get(i).getName());
                }
            }
            else if (option == 6) {
                System.out.println(":(");
                return;
            }
        }
    }
}
