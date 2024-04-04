package lk.jiat.reserveme;

public class EmailHolder {
    private static String userEmail;

    private static String sellerEmail;

    public static String getSellerEmail(){
        return sellerEmail;
    }

    public static void setSellerEmail(String sellerEmail) {
        EmailHolder.sellerEmail = sellerEmail;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        EmailHolder.userEmail = userEmail;
    }
}
