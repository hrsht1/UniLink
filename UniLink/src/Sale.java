import java.text.DecimalFormat;
import java.util.*;

public class Sale extends Post {

    private double askingPrice;
    private double minimumRaise;
    private double highestOffer;
    private DecimalFormat df = new DecimalFormat("#.##");

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    public double getMinimumRaise() {
        return minimumRaise;
    }

    public void setMinimumRaise(double minimumRaise) {
        this.minimumRaise = minimumRaise;
    }

    public double getHighestOffer() {
        return highestOffer;
    }

    public void setHighestOffer(double highestOffer) {
        this.highestOffer = highestOffer;
    }

    public Sale() {

    }

    public Sale(String id, String title, String description, String creatorID, String status, List<Reply> replies, double askingPrice, double minimumRaise, double highestOffer) {
        super(id, title, description, creatorID, status, replies);
        this.askingPrice = askingPrice;
        this.minimumRaise = minimumRaise;
        this.highestOffer = highestOffer;
    }

    @Override
    public String getPostDetails(boolean isUserCreator) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getPostDetails(isUserCreator));
        stringBuilder.append("Minimum Raise \t: " + String.format("%.2f", this.getMinimumRaise()) + "\n"
                + "Highest Offer \t: " + String.format("%.2f", this.getHighestOffer()));

        if (isUserCreator) {
            stringBuilder.append(this.getReplyDetails(this.getId()));
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean handleReply(String username) {
        Scanner scanner = new Scanner(System.in);

        if (!this.getStatus().equalsIgnoreCase("OPEN")) {
            System.out.println("Sale is CLOSED!");
            return false;
        }

        if (this.getCreatorId().equalsIgnoreCase(username)) {
            System.out.println("Replying to your own post is invalid!");
            return false;
        }

        System.out.println("Name \t\t\t: " + this.getTitle());
        System.out.println("Highest offer \t: " + (this.getHighestOffer() == 0 ? "NONE" : "$" + String.format("%.2f", this.getHighestOffer())));
        System.out.println("Minimum Raise \t: $" + String.format("%.2f", this.getMinimumRaise()));

        boolean flag = false;
        String input = "";
        do {
            System.out.println("Enter your offer or 'Q' to quit: ");
            input = scanner.nextLine();

            double value = Double.parseDouble(String.format("%.2f", Double.valueOf(input)));
            if (!input.equalsIgnoreCase("Q") && (Double.parseDouble(input) > this.getAskingPrice())) {
                System.out.println("Congratulations! " + this.getTitle() + " has been sold to you");
                System.out.println("Please contact he owner " + this.getCreatorId() + " for more details.");
                this.setHighestOffer(Double.parseDouble(input));
                this.setStatus("CLOSED");
                String eventId = this.getId();
                this.replies.add(new Reply(this.getId(), value, username));
                flag = true;
                return flag;
            } else if (!input.equalsIgnoreCase("Q") && (Double.parseDouble(input) - this.getHighestOffer() >= this.getMinimumRaise())) {
                this.setHighestOffer(Double.parseDouble(input));
                System.out.println("Your offer has been submitted!");
                if (this.getAskingPrice() > Double.parseDouble(input)) {
                    System.out.println("However, your offer is below the asking price.");
                }
                System.out.println("The item is still on sale.");
                String eventId = this.getId();
                this.replies.add(new Reply(this.getId(), value, username));
                flag = true;
                return flag;
            } else if (!input.equalsIgnoreCase("Q") && (Double.parseDouble(input) - this.getHighestOffer() < this.getMinimumRaise())) {
                System.out.println("Offer not accepted!");
                return flag;
            }
        } while (!flag);
        return false;
    }

    @Override
    public String getReplyDetails(String postId) {

        List<Reply> replies = this.getReplies();
        StringBuilder sb = new StringBuilder();
        sb.append("\nAsking Price \t: " + String.format("%.2f", this.getAskingPrice()));
        sb.append("\n-- Offer History --\n");
        if (replies.size() == 0) {
            sb.append("EMPTY");
        } else {
            replies.sort(Comparator.comparing(Reply::getValue).reversed());
            for (int i = 0; i < replies.size(); i++) {
                sb.append(replies.get(i).getResponderID()).append(": ").append("$").append(String.format("%.2f", replies.get(i).getValue())).append("\n");
            }
        }
        return sb.toString();
    }
}
