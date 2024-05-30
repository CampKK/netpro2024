package callback;

// Main.java
public class MainCallBackLambda {
    public static void main(String[] args) {
        Worker worker = new Worker();

        // Using a lambda expression for the Callback implementation
        worker.doWork(result -> System.out.println("Callback received with message: " + result));
    }
}

