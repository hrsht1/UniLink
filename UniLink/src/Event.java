import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class Event extends Post {

    private String venue;
    private String date;
    private int capacity;
    private int attendeeCount;
    private DecimalFormat df = new DecimalFormat("#.##");

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public Event() {

    }

    public Event(String id, String title, String description, String creatorID, String status, List<Reply> replies, String venue, String date, int capacity, int attendeeCount) {
        super(id, title, description, creatorID, status, replies);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = attendeeCount;
    }

    @Override
    public boolean handleReply(String username) {

        Scanner scanner = new Scanner(System.in);

        if (!this.getStatus().equalsIgnoreCase("OPEN")) {
            System.out.println("Event is CLOSED!");
            return false;
        }

        if (this.getCreatorId().equalsIgnoreCase(username)) {
            System.out.println("Replying to your own post is invalid!");
            return false;
        }

        System.out.println("Name	: " + this.getTitle());
        System.out.println("Venue 	: " + this.getVenue());
        System.out.println("Date  	: " + this.getDate());
        System.out.println("Status	: " + this.getStatus());

        String input = "";
        boolean flag = false;
        do {
            System.out.println("Enter '1' to join event or 'Q' o quit: ");
            input = scanner.nextLine();
            if (!input.equalsIgnoreCase("Q") && input.equals("1")) {
                if (this.getCapacity() > this.getAttendeeCount()) {
                    this.setAttendeeCount(this.getAttendeeCount() + 1);
                    if (this.getCapacity() == this.getAttendeeCount()) {
                        this.setStatus("CLOSED");
                    }
                    String eventId = this.getId();
                    this.replies.add(new Reply(this.getId(), Double.valueOf(input), username));
                    flag = true;
                    return flag;
                }
            } else {
                System.out.println("Invalid input!");
            }
        } while (!flag);
        return flag;
    }

    @Override
    public String getPostDetails(boolean isUserCreator) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getPostDetails(isUserCreator));
        stringBuilder.append("Venue \t\t\t: " + this.getVenue() + "\n"
                + "Date \t\t\t: " + this.getDate() + "\n"
                + "Capacity \t\t: " + this.getCapacity() + "\n"
                + "Attendees \t\t: " + this.getAttendeeCount());

        if (isUserCreator) {
            stringBuilder.append(this.getReplyDetails(this.getId()));
        }
        return stringBuilder.toString();
    }

    @Override
    public String getReplyDetails(String postId) {

        List<Reply> replies = this.getReplies();
        StringBuilder sb = new StringBuilder();
        sb.append("\nAttendee List \t: ");
        if (replies.size() == 0) {
            sb.append("EMPTY");
        } else {
            for (int i = 0; i < replies.size(); i++) {
                if (i != replies.size() - 1) {
                    sb.append(replies.get(i).getResponderID()).append(", ");
                } else {
                    sb.append(replies.get(i).getResponderID());
                }
            }
        }
        return sb.toString();
    }
}