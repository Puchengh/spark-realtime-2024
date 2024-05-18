package study;

public class Test {
    public static void main(String[] args) {
        byte b = 10;
        test(b);

        char a = 'a';
        test(a);

    }

    public static void test(byte b){
        System.out.println("bbbb");
    }
    public static void test(short b){
        System.out.println("ssss");
    }
    public static void test(char b){
        System.out.println("cccc");
    }
    public static void test(int b){
        System.out.println("iiii");
    }
}
