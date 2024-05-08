public class NoHolidayException extends Exception {


    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.err.println("この日は平日だよ！はたらきたくないねー。エラーメッセージです。");
    }
}