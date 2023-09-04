

public class SBlock extends Block {

	int point = 4; // 좌측 상단 시작지점

	@Override
	boolean makeBlock() {

		super.setList(4); // 블록 개수가 4개

		// list에 S블록의 모양대로 btn설정
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(1, point - 1);
		list[3] = new Point(1, point);

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

	final int cases = 2; // 경우의 수를 나타냄

	@Override
	void Rotate() {
		if (blockIsFixed())
			return;

		eraseBlock(); // 블럭 지우기

		Point[] tmp = new Point[4];// 원래 값 저장
		for (int i = 0; i < list.length; i++)
			tmp[i] = new Point(list[i].x, list[i].y);

		Point tmplist[] = new Point[4];

		// 회전 구현
		if (RotateCnt == 0) { // 'S'에서 시작해서 반시계
			tmplist[0] = new Point(list[0].x - 1, list[0].y - 1);
			tmplist[1] = new Point(list[1].x, list[1].y - 2);
			tmplist[2] = new Point(list[2].x - 1, list[2].y + 1);
			tmplist[3] = new Point(list[3].x, list[3].y);
		} else if (RotateCnt == 1) {
			tmplist[0] = new Point(list[0].x + 1, list[0].y + 1);
			tmplist[1] = new Point(list[1].x, list[1].y + 2);
			tmplist[2] = new Point(list[2].x + 1, list[2].y - 1);
			tmplist[3] = new Point(list[3].x, list[3].y);
		}

		RotateCnt = rotateDraw(tmplist, tmp, RotateCnt, cases);

	}

}
