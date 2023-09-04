

public class OBlock extends Block {

	int point = 4; // 좌측 상단 시작지점

	@Override

	boolean makeBlock() {
		super.setList(4); // 블록 개수가 4개

		// list에 O블록의 모양대로 btn설정
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(1, point);
		list[3] = new Point(1, point + 1);

		// 생성할 자리에 이미 블록이 있는 경우 -> 생성실패
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			if (btn[x][y].getBackground() == block_color)
				return false;
			else
				btn[x][y].setBackground(block_color);
		}

		return true; // 블럭 생성 완료
	}

	@Override
	void Rotate() {
		// 회전이 무의미한 블럭
		return;
	}

}
