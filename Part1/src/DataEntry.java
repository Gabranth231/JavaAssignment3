public class DataEntry {
    private int authorID;
    private int publisherID;
    private String isbn = "";
    private String firstName = "";
    private String lastName = "";
    private String publisherName = "";
    private String title = "";
    private int editionNum;
    private String copyrightYear;
    private String imageFile = "";
    private double price;

    public DataEntry(){
    }

    public DataEntry( int id )
    {
        authorID = id;
    }

    public int getAuthorID(){return authorID;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEditionNum() {
        return editionNum;
    }

    public void setEditionNum(String editionNum) {
        this.editionNum = Integer.valueOf(editionNum);
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {this.copyrightYear = copyrightYear;   }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = Double.parseDouble(price);
    }
}
