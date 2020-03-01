import java.util.Arrays;

public class Trial {

    public static void main(String[] args) {
        String s = "172.1.2.3";
//        byte b = (byte) Integer.parseInt(s);
//        int a = b & 0xFF;
//        System.out.println(b);
//        System.out.println(a);
        String[] address =  s.split("\\.");
        System.out.println(address.length);
        System.out.println(Arrays.toString(address));
    }

}
