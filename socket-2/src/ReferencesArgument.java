public class ReferencesArgument {
    public static void main(String[] args) {
        // プリミティブ型
        int a = 0;
        char c = 'a';
        // String 型
        // https://docs.oracle.com/javase/jp/8/docs/api/java/lang/String.html
        String s = "nochange";
        System.out.println("a = " + a);
        System.out.println("c = " + c);
        System.out.println("s = " + s);
        methodPrimitive(a, c, s);
        System.out.println("a = " + a);
        System.out.println("c = " + c);
        System.out.println("s = " + s);
        System.out.println();

        int[] nums = new int[] { 0, 1, 2, 3 };
        MyClass obj = new MyClass(1, 2);
        System.out.println("nums = " + arrayDump(nums));
        System.out.println("obj  = " + obj);
        methodObject(nums, obj);
        System.out.println("nums = " + arrayDump(nums));
        System.out.println("obj  = " + obj.toString());
    }

    static String arrayDump(int[] p) {
        String s = "";
        for (int i = 0; i < p.length; ++i) {
            // s += String.format("%2d ", p[i]);
            s += p[i] + ",";
        }
        return s;
    }

    static void methodPrimitive(int n, char c, String s) {
        System.out.println("> Run MethodPrimitive");
        n = 10;
        c = 'X';
        s = "changed";
    }

    static void methodObject(int[] nums, MyClass obj) {
        System.out.println("> Run methodObject");
        nums[0] = 9999;
        obj.a = 9999;
        obj.b = 9999;
    }

    static class MyClass {
        public int a;
        public int b;

        MyClass(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "{ a: " + this.a + ", b: " + this.b + "}";
        }
    }
}