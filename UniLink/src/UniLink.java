import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UniLink {

    private static Scanner sc = null;

    private static List<String> usernames = new ArrayList<>();
    private static List<Event> events = new ArrayList<>();
    private static List<Sale> sales = new ArrayList<>();
    private static List<Job> jobs = new ArrayList<>();
    private static List<Reply> repliesForEvents = new ArrayList<>();
    private static List<Reply> repliesForSales = new ArrayList<>();
    private static List<Reply> repliesForJobs = new ArrayList<>();

    public static void main(String args[]) throws ParseException, InputMismatchException {

        sc = new Scanner(System.in);

        //Initialise Demo Data fo demonstration
        initialiseUsers();
        initialiseReplies();
        initialisePosts();

        String username = "";
        boolean quit = false;
        while (!quit) {
            int choice = showLoginMenu();
            switch (choice) {
                case 2:
                    System.out.println("You have successfully quit the system.");
                    quit = true;
                    break;
                case 1: {
                    Scanner scan = new Scanner(System.in);
                    System.out.println("Enter your username: ");
                    username = scan.nextLine();
                    boolean logout = false;
                    while (!logout) {
                        int subChoice = showStudentMenu(username);
                        switch (subChoice) {
                            case 1:
                                events.add(createNewEvent(username));
                                System.out.println("Success! Your event has been created with id " + events.get(events.size() - 1).getId());
                                break;
                            case 2:
                                sales.add(createNewSale(username));
                                System.out.println("Success! Your new sale has been created with id " + sales.get(sales.size() - 1).getId());
                                break;
                            case 3:
                                jobs.add(createNewJob(username));
                                System.out.println("Success! Your new job has been created with id " + jobs.get(jobs.size() - 1).getId());
                                break;
                            case 4:
                                replyToPost(username);
                                break;
                            case 5:
                                displayMyPosts(username);
                                break;
                            case 6:
                                displayAllPosts(username);
                                break;
                            case 7:
                                closePost(username);
                                break;
                            case 8:
                                deletePost(username);
                                break;
                            case 9:
                                logout = true;
                                System.out.println("You have successfully logged out!");
                                break;
                            default:
                                System.out.println("Please Enter Choice From Menu!");
                                break;
                        }
                    }
                }
            }
        }
        sc.close();
    }

    private static int showLoginMenu() {
        System.out.println("** UniLink System **\r\n" +
                "1. Log in\r\n" +
                "2. Quit");
        int input = getChoice(1, 2);
        return input;
    }

    private static int showStudentMenu(String username) {
        System.out.println("Welcome " + username);
        System.out.println("** Student Menu **");
        System.out.println("1. New Event Post");
        System.out.println("2. New Sale Post");
        System.out.println("3. New Job Post");
        System.out.println("4. Reply To Post");
        System.out.println("5. Display My Post");
        System.out.println("6. Display All Post");
        System.out.println("7. Close Post");
        System.out.println("8. Delete Post");
        System.out.println("9. Log Out");
        int choice = getChoice(1, 9);
        return choice;
    }

    private static int getChoice(int a, int b) {
        Scanner scan = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Your choice [" + a + " - " + b + "]: ");
            choice = Integer.parseInt((scan.next()));
        } while ((choice < a) || (choice > b));
        return choice;
    }

    private static Event createNewEvent(String username) throws ParseException {
        System.out.println("Enter details of the Event below: ");
        String title;
        String description;
        String venue;
        String date;
        int capacity;

        do {
            System.out.print("Item name \t\t: ");
            title = sc.nextLine();
        } while (!checkString(title));
        do {
            System.out.print("Description \t: ");
            description = sc.nextLine();
        } while (!checkString(description));
        do {
            System.out.print("Venue \t\t\t: ");
            venue = sc.nextLine();
        } while (!checkString(venue));

        try {
            date = getDate();
        } catch (ParseException p) {
            System.out.println(("Date is not valid, please enter date in this format - dd/mm/yyyy"));
            date = getDate();
        }
        do {
            System.out.print("Capacity \t\t: ");
            capacity = sc.nextInt();
        } while (!checkValue(capacity));
        sc.nextLine();
        return new Event(IDGenerator.generateId("Event"), title, description, username, "OPEN", new ArrayList<Reply>(), venue, date, capacity, 0);
    }

    private static Sale createNewSale(String username) {

        String title;
        String description;
        double askingPrice;
        double minimumRaise;

        System.out.println("Enter details of item to sale below: ");

        do {
            System.out.print("Item name \t\t: ");
            title = sc.nextLine();
        } while (!checkString(title));
        do {
            System.out.print("Description \t: ");
            description = sc.nextLine();
        } while (!checkString(description));
        do {
            System.out.print("Asking Price \t: ");
            askingPrice = sc.nextDouble();
        } while (!checkValue(askingPrice));
        do {
            System.out.print("Minimum Raise \t: ");
            minimumRaise = sc.nextDouble();
        } while (!checkValue(minimumRaise));

        return new Sale(IDGenerator.generateId("Sale"), title, description, username, "OPEN", new ArrayList<Reply>(), Double.valueOf(String.format("%.2f", askingPrice)), Double.valueOf(String.format("%.2f", askingPrice)), 0);
    }

    private static Job createNewJob(String username) {

        String title;
        String description;
        double proposedPrice;

        System.out.println("Enter details of new job below: ");
        do {
            System.out.print("Item name \t\t: ");
            title = sc.nextLine();
        } while (!checkString(title));
        do {
            System.out.print("Description \t: ");
            description = sc.nextLine();
        } while (!checkString(description));
        do {
            System.out.print("Proposed Price \t: ");
            proposedPrice = sc.nextDouble();
        } while (!checkValue(proposedPrice));

        return new Job(IDGenerator.generateId("Job"), title, description, username, "OPEN", new ArrayList<Reply>(), Double.valueOf(String.format("%.2f", proposedPrice)), 0);
    }

    private static void replyToPost(String username) {

        String postId;
        do {
            System.out.print("Enter post id or 'Q' to quit: ");
            postId = sc.nextLine();
            if (!postId.equalsIgnoreCase("Q")) {
                if (postId.startsWith("EVE") && searchPostId(postId, new Event())) {
                    int index = 0;
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Event event = events.get(index);
                    if (event.handleReply(username)) {
                        events.remove(index);
                        events.add(event);
                        System.out.println("Event Registration Accepted!");
                        return;
                    } else {
                        break;
                    }
                } else if (postId.startsWith("SAL") && searchPostId(postId, new Sale())) {
                    int index = 0;
                    for (int i = 0; i < sales.size(); i++) {
                        if (sales.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Sale sale = sales.get(index);
                    if (sale.handleReply(username)) {
                        sales.remove(index);
                        sales.add(sale);
                        return;
                    }
                    return;
                } else if (postId.startsWith("JOB") && searchPostId(postId, new Job())) {
                    int index = 0;
                    for (int i = 0; i < jobs.size(); i++) {
                        if (jobs.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Job job = jobs.get(index);
                    if (job.handleReply(username)) {
                        jobs.remove(index);
                        jobs.add(job);
                        return;
                    }
                    return;
                } else {
                    System.out.println("Invalid Post id! Post not found");
                    replyToPost(username);
                }
            } else {
                System.out.println("You have successfully Quit the application!");
                return;
            }
        } while (!postId.equalsIgnoreCase("Q"));
        return;
    }

    private static void displayMyPosts(String username) {

        boolean isUserCreator = true;
        System.out.println("** MY POSTS **");

        for (Event event : events
        ) {
            if (event.getCreatorId().equalsIgnoreCase(username)) {
                System.out.println(event.getPostDetails(isUserCreator));
                System.out.println("--------------------------------");
            }
        }
        for (Sale sale : sales
        ) {
            if (sale.getCreatorId().equalsIgnoreCase(username)) {
                System.out.println(sale.getPostDetails(isUserCreator));
                System.out.println("--------------------------------");
            }
        }
        for (Job job : jobs
        ) {
            if (job.getCreatorId().equalsIgnoreCase(username)) {
                System.out.println(job.getPostDetails(isUserCreator));
                System.out.println("--------------------------------");
            }
        }
    }

    private static void displayAllPosts(String username) {

        boolean isUserCreator = false;
        System.out.println("** ALL POSTS **");

        for (Event event : events
        ) {
            if (event.getCreatorId().equalsIgnoreCase(username)) {
                isUserCreator = true;
            } else {
                isUserCreator = false;
            }
            System.out.println(event.getPostDetails(isUserCreator));
            System.out.println("--------------------------------");
        }
        for (Sale sale : sales
        ) {
            if (sale.getCreatorId().equalsIgnoreCase(username)) {
                isUserCreator = true;
            } else {
                isUserCreator = false;
            }
            System.out.println(sale.getPostDetails(isUserCreator));
            System.out.println("--------------------------------");
        }
        for (Job job : jobs
        ) {
            if (job.getCreatorId().equalsIgnoreCase(username)) {
                isUserCreator = true;
            } else {
                isUserCreator = false;
            }
            System.out.println(job.getPostDetails(isUserCreator));
            System.out.println("--------------------------------");
        }
    }

    private static void closePost(String username) {

        boolean flag = false;
        String postId;

        do {
            System.out.print("Enter post id or 'Q' to quit: ");
            postId = sc.nextLine();
            if (!postId.equalsIgnoreCase("Q")) {
                if (postId.startsWith("EVE") && searchPostId(postId, new Event())) {
                    int index = 0;
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Event event = events.get(index);
                    if (event.getCreatorId().equalsIgnoreCase(username) && event.getStatus().equalsIgnoreCase("OPEN")) {
                        event.setStatus("CLOSED");
                        System.out.println("Succeeded, Post Closed!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                } else if (postId.startsWith("SAL")) {
                    int index = 0;
                    for (int i = 0; i < sales.size(); i++) {
                        if (sales.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Sale sale = sales.get(index);
                    if (sale.getCreatorId().equalsIgnoreCase(username) && sale.getStatus().equalsIgnoreCase("OPEN")) {
                        sale.setStatus("CLOSED");
                        System.out.println("Succeeded, Post Closed!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                } else if (postId.startsWith("JOB")) {
                    int index = 0;
                    for (int i = 0; i < jobs.size(); i++) {
                        if (jobs.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Job job = jobs.get(index);
                    if (job.getCreatorId().equalsIgnoreCase(username) && job.getStatus().equalsIgnoreCase("OPEN")) {
                        job.setStatus("CLOSED");
                        System.out.println("Succeeded, Post Closed!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                }
            } else {
                System.out.println("Invalid Post id! Post not found");
                closePost(username);
            }
        } while (!postId.equalsIgnoreCase("Q") && !flag);
    }

    private static void deletePost(String username) {

        boolean flag = false;
        String postId;

        do {
            System.out.print("Enter post id or 'Q' to quit: ");
            postId = sc.nextLine();
            if (!postId.equalsIgnoreCase("Q")) {
                if (postId.startsWith("EVE") && searchPostId(postId, new Event())) {
                    int index = 0;
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Event event = events.get(index);
                    if (event.getCreatorId().equalsIgnoreCase(username)) {
                        events.remove(event);
                        System.out.println("Succeeded, Post Deleted!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                } else if (postId.startsWith("SAL")) {
                    int index = 0;
                    for (int i = 0; i < sales.size(); i++) {
                        if (sales.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Sale sale = sales.get(index);
                    if (sale.getCreatorId().equalsIgnoreCase(username)) {
                        sales.remove(sale);
                        System.out.println("Succeeded, Post Deleted!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                } else if (postId.startsWith("JOB")) {
                    int index = 0;
                    for (int i = 0; i < jobs.size(); i++) {
                        if (jobs.get(i).getId().equalsIgnoreCase(postId)) {
                            index = i;
                            break;
                        }
                    }
                    Job job = jobs.get(index);
                    if (job.getCreatorId().equalsIgnoreCase(username)) {
                        jobs.remove(job);
                        System.out.println("Succeeded, Post Deleted!");
                        flag = true;
                    } else {
                        System.out.println("Request Denied! You are not the owner of this post.");
                        return;
                    }
                }
            } else {
                System.out.println("Invalid Post id! Post not found");
                deletePost(username);
            }
        } while (!postId.equalsIgnoreCase("Q") && !flag);
        return;
    }

    private static boolean searchPostId(String postId, Post post) {

        if (post instanceof Event) {
            for (Post event : events) {
                ((Event) event).getId().equalsIgnoreCase(postId);
                return true;
            }
        } else if (post instanceof Sale) {
            for (Post sale : sales) {
                ((Sale) sale).getId().equalsIgnoreCase(postId);
                return true;
            }
        } else if (post instanceof Job) {
            for (Post job : jobs) {
                ((Job) job).getId().equalsIgnoreCase(postId);
                return true;
            }
        }
        return false;
    }

    private static String getDate() throws ParseException {

        String date;
        System.out.print("Date(dd/mm/yyyy): ");
        date = sc.nextLine();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        Date fDate = formatDate.parse(date);

        if (fDate.compareTo(new Date()) < 0) {
            System.out.println("Please enter future date");
            getDate();
        }
        return date;
    }

    private static boolean checkValue(double value) {
        if (value <= 0) {
            System.out.println("Please enter value greater than 0!");
            return false;
        }
        return true;
    }

    private static boolean checkString(String value) {
        if (value.trim().equals("")) {
            System.out.println("Please enter valid input!");
            return false;
        }
        return true;
    }

    private static void initialiseReplies() {
        Reply reply = new Reply("EVE001", 1, "s2");
        Reply reply3 = new Reply("EVE001", 1, "s3");
        Reply reply6 = new Reply("EVE001", 1, "s4");
        Reply reply9 = new Reply("EVE001", 1, "s5");
        Reply reply12 = new Reply("EVE001", 1, "s6");

        Reply reply1 = new Reply("SAL001", 500, "s2");
        Reply reply4 = new Reply("SAL001", 600, "s3");
        Reply reply7 = new Reply("SAL001", 550.25, "s4");
        Reply reply10 = new Reply("SAL001", 700, "s5");
        Reply reply13 = new Reply("SAL001", 800.55, "s6");

        Reply reply2 = new Reply("JOB001", 50, "s2");
        Reply reply5 = new Reply("JOB001", 42.55, "s3");
        Reply reply8 = new Reply("JOB001", 40, "s4");
        Reply reply11 = new Reply("JOB001", 37, "s5");
        Reply reply14 = new Reply("JOB001", 35.55, "s6");

        repliesForEvents.add(reply);
        repliesForSales.add(reply1);
        repliesForJobs.add(reply2);
        repliesForEvents.add(reply3);
        repliesForSales.add(reply4);
        repliesForJobs.add(reply5);
        repliesForEvents.add(reply6);
        repliesForSales.add(reply7);
        repliesForJobs.add(reply8);
        repliesForEvents.add(reply9);
        repliesForSales.add(reply10);
        repliesForJobs.add(reply11);
        repliesForEvents.add(reply12);
        repliesForSales.add(reply13);
        repliesForJobs.add(reply14);
    }

    private static void initialiseUsers() {
        usernames.add("s1");
        usernames.add("s2");
        usernames.add("s3");
        usernames.add("s4");
        usernames.add("s5");
        usernames.add("s6");
        usernames.add("s7");
        usernames.add("s8");
        usernames.add("s9");
        usernames.add("s10");
    }

    private static void initialisePosts() {
        Event event = new Event("EVE001", "Birthday party", "Small gathering with loved ones", "s1", "OPEN", repliesForEvents, "My home", "10/04/2020", 5, 5);
        events.add(event);
        Sale sale = new Sale("SAL001", "iPad 2019", "iPad for sale mint condition 256gb", "s2", "OPEN", repliesForSales, 1000.00, 20.00, 800.55);
        sales.add(sale);
        Job job = new Job("JOB001", "Gardening of front yard", "Gardening of 50sq mtr front year", "s3", "OPEN", repliesForJobs, 50.55, 35.55);
        jobs.add(job);
    }
}