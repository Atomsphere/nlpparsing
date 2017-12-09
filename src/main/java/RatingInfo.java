public class RatingInfo {

    public String isbn;
    public String rating;
    public String count;

    public RatingInfo(String input){
        String temp[];
        temp = input.split(" ");
        isbn = temp[0];
        rating = temp[1];
        count = temp[2];
    }

}
