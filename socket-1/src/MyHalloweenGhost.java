import java.io.Serializable;

// ハロウィンのゴーストに関する情報を持つクラス
public class MyHalloweenGhost implements Serializable {
    private static final long serialVersionUID = 1L;

    private String okashi;
    private String message;

    // コンストラクタ
    public MyHalloweenGhost(String okashi, String message) {
        this.okashi = okashi;
        this.message = message;
    }

    // おかしを取得
    public String getGhostType() {
        return okashi;
    }

    // ゴーストのタイプを設定
    public void setGhostType(String okashi) {
        this.okashi = okashi;
    }

    // ゴーストの音を取得
    public String getGhostSound() {
        return message;
    }

    // ゴーストの音を設定
    public void setGhostSound(String message) {
        this.message = message;
    }

    public void printGhostInfo() {
        System.out.println(message +" "+ okashi +"くれなきゃいたずらしちゃうぞ");
    }
}
