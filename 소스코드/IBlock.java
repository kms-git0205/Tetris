

public class IBlock extends Block {
	// I자 모양의 블럭

	/*
	 * TetrisDemo에서 전역변수들 불러옴 static final int GARO = TetrisDemo.GARO; //가로 수 static
	 * final int SERO = TetrisDemo.SERO; //세로 수 static JButton btn[][] =
	 * TetrisDemo.btn; //각 버튼들 static final int TIME = TetrisDemo.TIME; // 블럭이 내려오는
	 * 주기(단위 : ms) static final Color block_color = TetrisDemo.block_color; //블록의 색깔
	 * static final Color ground_color = TetrisDemo.ground_color; static boolean
	 * game_over = TetrisDemo.game_over; //게임이 끝났는지 여부
	 * 
	 * isGameOver(), blockIsFixed() 사용가능
	 */

	int point = 3; // 왼쪽상위의 점이 중심, 나타나는 위치는 고정

	@Override
	boolean makeBlock() {
		super.setList(4); // 블록 개수가 4개

		// list에 I블록의 모양대로 btn설정
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(0, point + 2);
		list[3] = new Point(0, point + 3);

		// 생성할 자리에 이미 블록이 있는 경우 -> 생성실패
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			if (btn[x][y].getBackground() == block_color) {
				return false;
			}
		}

		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(block_color);
		}

		return true; // 블럭 생성 완료
	}

	final int cases = 2; // 경우의 수를 나타냄

	@Override
	public void Rotate() {

		if (blockIsFixed())
			return;

		eraseBlock(); // 블럭 지우기

		Point[] tmp = new Point[4];// 원래 값 저장
		for (int i = 0; i < list.length; i++)
			tmp[i] = new Point(list[i].x, list[i].y);

		Point tmplist[] = new Point[4];

		// 회전 구현
		if (RotateCnt == 0) { // 'ㅡ' -> 'ㅣ'
			tmplist[0] = new Point(list[0].x, list[0].y + 3);
			tmplist[1] = new Point(list[1].x + 1, list[1].y + 2);
			tmplist[2] = new Point(list[2].x + 2, list[2].y + 1);
			tmplist[3] = new Point(list[3].x + 3, list[3].y);
		} else if (RotateCnt == 1) { // 'ㅣ' -> 'ㅡ'
			tmplist[0] = new Point(list[0].x, list[0].y - 3);
			tmplist[1] = new Point(list[1].x - 1, list[1].y - 2);
			tmplist[2] = new Point(list[2].x - 2, list[2].y - 1);
			tmplist[3] = new Point(list[3].x - 3, list[3].y);
		}

		RotateCnt = rotateDraw(tmplist, tmp, RotateCnt, cases);

	}

}
