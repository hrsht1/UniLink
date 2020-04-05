import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Job extends Post {

    private double proposedPrice;
    private double lowestOffer;
    private DecimalFormat df = new DecimalFormat("#.##");

    public double getLowestOffer() {
        return lowestOffer;
    }

    public void setLowestOffer(double lowestOffer) {
        this.lowestOffer = lowestOffer;
    }

    public double getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(double proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public Job() {

    }

    public Job(String id, String title, String description, String creatorID, String status, List<Reply> replies, double proposedPrice, double lowestOffer) {
        super(id, title, description, creatorID, status, replies);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = lowestOffer;
    }

    @Override
    public boolean handleReply(String username) {
        Scanner scanner = new Scanner(System.in);

        if (!this.getStatus().equalsIgnoreCase("OPEN")) {
            System.out.println("Job is CLOSED!");
            return false;
        }

        if (this.getCreatorId().equalsIgnoreCase(username)) {
            System.out.println("Replying to your own post is invalid!");
            return false;
        }

        System.out.println("Name \t\t: " + this.getTitle());
        System.out.println("Proposed Price \t: $" + String.format("%.2f", this.getProposedPrice()));
        System.out.println("Lowest Offer \t: " + (this.getLowestOffer() == 0 ? "NONE" : "$" + String.format("%.2f", this.getLowestOffer())));

        boolean flag = false;
        String input = "";

        do {
            System.out.println("Enter your offer or 'Q' o quit: ");
            input = scanner.nextLine();
            if (!input.equalsIgnoreCase("Q") && (Double.parseDouble(input) < this.getProposedPrice())) {
                this.setLowestOffer(Double.parseDouble(input));
                System.out.println("Offer accepted!");
                String eventId = this.getId();
                this.replies.add(new Reply(this.getId(), Double.parseDouble(String.format("%.2f", Double.valueOf(input))), username));
                flag = true;
                return flag;
            } else if (!input.equalsIgnoreCase("Q") && Double.parseDouble(input) > this.getProposedPrice()) {
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
        sb.append("\n-- Offer History --\n");
        if (replies.size() == 0) {
            sb.append("EMPTY");
        } else {
            replies.sort(Comparator.comparing(Reply::getValue));
            for (int i = 0; i < replies.size(); i++) {
                sb.append(replies.get(i).getResponderID()).append(": ").append("$").append(String.format("%.2f", replies.get(i).getValue())).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String getPostDetails(boolean isUserCreator) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getPostDetails(isUserCreator));
        stringBuilder.append("Proposed Price \t: " + String.format("%.2f", this.getProposedPrice()) + "\n"
                + "Lowest Offer \t: " + String.format("%.2f", this.getLowestOffer()));

        if (isUserCreator) {
            stringBuilder.append(this.getReplyDetails(this.getId()));
        }
        return stringBuilder.toString();
    }
}
