public class CenterKamokuManager {
    private CenterKamoku[] subjects;

    public CenterKamokuManager() {
        subjects = new CenterKamoku[5];
        subjects[0] = new CenterKamoku("英");
        subjects[1] = new CenterKamoku("国");
        subjects[2] = new CenterKamoku("数");
        subjects[3] = new CenterKamoku("社");
        subjects[4] = new CenterKamoku("理");
    }

    public void printAllScore() {
        for (CenterKamoku subject : subjects) {
            subject.printNameAndScore();
        }
    }

    public void printAverageScore() {
        int totalScore = 0;
        for (CenterKamoku subject : subjects) {
            totalScore += subject.getScore();
        }
        double averageScore = (double) totalScore / subjects.length;
        System.out.println("5科目の平均は：" + (int) averageScore + "点");
    }

    public void printMaxScore() {
        int maxScore = -1;
        String maxSubject = "";
        for (CenterKamoku subject : subjects) {
            if (subject.getScore() > maxScore) {
                maxScore = subject.getScore();
                maxSubject = subject.getName();
            }
        }
        System.out.println("最高点は" + maxScore + "点の" + maxSubject + "です。");
    }

    public static void main(String[] args) {
        CenterKamokuManager manager = new CenterKamokuManager();
        manager.printAllScore();
        manager.printAverageScore();
        manager.printMaxScore();
    }
}
