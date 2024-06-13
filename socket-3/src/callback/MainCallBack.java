package callback;

// Main.java
public class MainCallBack {
    public static void main(String[] args) {
        Worker worker = new Worker();

        // Creating an anonymous class implementing the Callback interface
        worker.doWork(new Callback() {
            @Override
            public void onComplete(String result) {
                System.out.println("Callback received with message: " + result);
            }
        });
    }
}
