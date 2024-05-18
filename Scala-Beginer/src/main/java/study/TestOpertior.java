package study;

public class TestOpertior {
    public static void main(String[] args) {

        //比较运算符
        String s1 = "hello";
        String s2 = new String("hello");
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));

        //复制运算符
        byte b = 10;
        b = (byte)(b + 1);

        b += 1;
        System.out.println(b);

    }
}
