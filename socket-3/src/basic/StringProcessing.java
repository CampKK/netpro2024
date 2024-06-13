package basic;

import java.util.LinkedList;
import java.util.List;

public class StringProcessing {

    public static void main(String args[]) {

        // ===========文字列から数値型への変換=============
        // int型
        String str = "10";

        int a_int = Integer.parseInt(str);
        // short型
        short a_shrot = Short.parseShort(str);
        // byte型
        byte a_byte = Byte.parseByte(str);
        // long型

        long l_a = Long.parseLong(str);
        // float型

        str = "10.5";
        float f_a = Float.parseFloat(str);
        // double型
        str = "10.5";
        double d_a = Double.parseDouble(str);

        // ===========数値から文字列への変換===================
        // int型
        int intvalue = 10;
        String s1 = String.valueOf(intvalue);

        // short型
        short shortvalue = 10;
        s1 = String.valueOf(shortvalue);
        // byte型
        byte bytevalue = 10;
        s1 = String.valueOf(bytevalue);
        // long型
        long longvalue = 10;
        s1 = String.valueOf(longvalue);
        // float型
        float floatvalue = 10.5f;
        s1 = String.valueOf(floatvalue);
        // double型
        double doublevalue = 10.5;
        s1 = String.valueOf(doublevalue);

        // =================文字列の処理
        // String型操作クラス
        // 文字列のコピー
        s1 = "ねこ";
        String s2 = "こねこ";

        if(s1==s2){// 中の文字列をみて判定してくれている。

            System.out.println("yes! s1==s2:"+s1+":"+s2);
        
        }else{
        
            System.out.println("no! s1=s2"+s1+":"+s2); //new Stringしたときだけobjectとしてふるまい判定にも
        
            //オブジェクトハッシュIDで比較させる。なのでその場合はequals()で比較しよう。
        
        }

        // 文字列のコピー
        //s2 = new String(s1);
        //s2 = s1.toString();

        // 文字列の比較（==での比較は×）
        s1.equals(s2);
        // 文字列の結合１
        s1 = s1.concat(s2);
        // 文字列の結合２
        s1 = s2 + "abc";

        // 大小文字を無視して比較
        s1.equalsIgnoreCase(s2);
        // 文字列の長さ
        s1.length();
        // 部分文字列(1文字)の取得
        int offset = 3;
        s1.charAt(offset);

        // 部分文字列の取得
        // int start=5; int end=7;
        // s1.substring(start,end);
        // s1.replace(c1,c2) 文字列の置換
        // s1.indexOf(c1) 指定文字(列)の最初の出現位置
        // s1.lastIndexOf(c1) 指定文字(列)の最後の出現位置

        // s1.endsWith(s1) 文字列の末尾をチェック
        // s1.startsWith(s1)

        // s1.startsWith(s1,offset) オフセットからの文字列をチェック

        // s1.compareTo(c1) 文字列の比較
        // s1.trim() 前後のスペースの削除
        // s1.toLowerCase() 小文字への変換
        // s1.toUpperCase() 大文字への変換
        // s1.valueOf(variable) 文字列への変換
        // s1.toString(stringbuffer) StringBufferから文字列への変換
        // s1.split(",") 文字列の分割（左の例では、","を区切りで分割）

        /// =============前方一致比較===========
        String str1 = "Stringクラスは文字列を表します。";
        String str2 = "String";

        if (str1.startsWith(str2)) {
            // 前方一致（接頭辞）です
        } else {
            // 前方一致ではありません
        }

        /// =============後方一致比較===========

        String k1 = "Stringクラスは文字列を表します。";
        String k3 = "String";

        if (k1.endsWith(k3)) {
            // 後方一致（接尾辞）です
        } else {
            // 後方一致ではありません
        }

        System.out.println("Hello");

        List<String> strings = new LinkedList<>();
        strings.add("Java");
        strings.add("is");
        strings.add("cool");
        String message = String.join("_", strings);
        System.out.println(message);

    }

    // 文字列先頭からの部分文字列取り出し
    public static void kiridashi() {
        String str = "壱２34伍６７89０";
        System.out.println(String.format("取り出し前の文字列 ： %s", str));
        System.out.println("文字列先頭から3文字取り出す -> " + str.substring(0, 3));
        System.out.println("文字列先頭から7文字取り出す -> " + str.substring(0, 7));
    }

    /// 文字列から部分文字列を取り出す

    static public void bubunkiridashi() {
        String str = "壱２34伍６７89０";
        System.out.println(String.format("取り出し前の文字列 ： %s", str));
        System.out.println("文字列の3文字目から7文字目を取り出す -> " + str.substring(2, 7));
        System.out.println("文字列の2文字目から2文字目を取り出す -> " + str.substring(1, 2));
        System.out.println("文字列の3文字目から3文字分を取り出す -> " + str.substring(2, 2 + 3));
    }

    // 文字列後ろからの部分文字列取り出し
    static public void ushirokiridashi() {
        String str = "壱２34伍６７89０";
        System.out.println(String.format("取り出し前の文字列 ： %s", str));
        System.out.println("文字列の6文字目から最後までを取り出す -> "
                + str.substring(6 - 1));
        System.out.println("文字列の後ろ3文字分を取り出す -> "
                + str.substring(str.length() - 3));
    }
    // 文字列の分離

    static public void split() {

        String a = "1,2,3,4,5";
        String b[] = a.split(",");
        System.out.print(b.length);
        System.out.print(b[0]);
        System.out.print(b[4]);

    }

}
