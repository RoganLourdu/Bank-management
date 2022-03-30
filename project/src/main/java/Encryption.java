
public class Encryption {
	public static String encryption(String password) {
        //in this method I encrypt the string by ascii values
        String encryptedcode = "";
        int asciivalue = 0;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= 33 && password.charAt(i) <= 63) {
                asciivalue = password.charAt(i) + 63;
                char convertedChar = (char) asciivalue;
                encryptedcode = encryptedcode + convertedChar;
            }
            if (password.charAt(i) >= 96 && password.charAt(i) <= 128) {
                asciivalue = password.charAt(i) - 63;
                char convertedChar = (char) asciivalue;
                encryptedcode = encryptedcode + convertedChar;
            }
            if (password.charAt(i) >= 65 && password.charAt(i) <= 80) {
                asciivalue = password.charAt(i) + 15;
                char convertedChar = (char) asciivalue;
                encryptedcode = encryptedcode + convertedChar;
            }
            if (password.charAt(i) >= 81 && password.charAt(i) <= 95) {
                asciivalue = password.charAt(i) - 15;
                char convertedChar = (char) asciivalue;
                encryptedcode = encryptedcode + convertedChar;
            }
            if (password.charAt(i) > 127 || password.charAt(i) == 64) {
                encryptedcode = encryptedcode + password.charAt(i);
            }
        }
        return encryptedcode;
    }
}
