public class IDGenerator {
    private static int eventID = 0;
    private static int saleID = 0;
    private static int jobID = 0;

    public static String generateId(String postType) {
        if (postType.equalsIgnoreCase("Event")) {
            eventID += 1;
            return postIdDecorator(idDecorator(eventID), postType);
        } else if (postType.equalsIgnoreCase("Sale")) {
            saleID += 1;
            return postIdDecorator(idDecorator(saleID), postType);
        } else if (postType.equalsIgnoreCase("Job")) {
            jobID += 1;
            return postIdDecorator(idDecorator(jobID), postType);
        }
        return "Can't generate a valid id";
    }

    private static String idDecorator(int id) {

        if (id < 10) {
            return "00" + String.valueOf(id);
        } else if (id < 100) {
            return "0" + String.valueOf(id);
        } else if (id < 1000) {
            return String.valueOf(id);
        }
        return "0000";
    }

    private static String postIdDecorator(String id, String postType) {
        if (postType.equalsIgnoreCase("Event")) {
            return "EVE" + id;
        } else if (postType.equalsIgnoreCase("Sale")) {
            return "SAL" + id;
        } else if (postType.equalsIgnoreCase("Job")) {
            return "JOB" + id;
        }
        return "Not a valid id";
    }
}
