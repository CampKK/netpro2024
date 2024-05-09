public class XmasTree {
    public static void main(String[] args) {
        int N=20;
        for (int j = 0; j < N; j++) {

            for (int i = 0; i <= N-j; i++) {
                if(i%2==1){
                System.out.print("+");
                }else{
                    System.out.print(" ");
                                }

            }

            for (int i = 0; i <= j*2; i++) {
                System.out.print("*");
            }

            for (int i = 0; i <= N-j; i++) {
                if(i%2==1){
                    System.out.print("+");
                    }else{
                        System.out.print(" ");
                                    }
    
            }

            System.out.print("\n");
        }
        int M = 7;
        int n = N*2;
        for(int j = 0; j < M; j++){

            for(int i = 0; i < n - (n/3)*2; i++){
                System.out.print(" ");
            }

            for(int i = n - (n/3)*2; i < n - (n/3); i++){
                System.out.print("@");
            }

            for(int i = n - (n/3); i <= n ; i++){
                System.out.print("");
            }
            System.out.print("\n");
        }
    }
}