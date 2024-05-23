public class DinnerFullCource {
    private Dish[] list = new Dish[5];// [0]-[4]の計5個

    public static void main(String[] args) {

        DinnerFullCource fullcourse = new DinnerFullCource();
        fullcourse.eatAll();
    }

    DinnerFullCource(){

        list[0] = new Dish();
		list[0].setName("特選シーザサラダ");
		list[0].setValune(10);
		list[1] = new Dish();
		list[1].setName("銀しゃり");
		list[1].setValune(20);
		list[2] = new Dish();
		list[2].setName("梅");
		list[2].setValune(10);
		list[3] = new Dish();
		list[3].setName("ワイン");
		list[3].setValune(30);
		list[4] = new Dish();
		list[4].setName("ステーキ");
		list[4].setValune(50);
	}
    void eatAll() {
		String str = "";

		for (Dish ryouri : list) {
			str += ryouri.getName() + "は" + ryouri.getValune() + "円、";
		}

		System.out.println("たかしへ、ママです。 今日の晩御飯コースは" + str + "よ");
	}
    }

