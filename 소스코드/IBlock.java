

public class IBlock extends Block {
	// I�� ����� ��

	/*
	 * TetrisDemo���� ���������� �ҷ��� static final int GARO = TetrisDemo.GARO; //���� �� static
	 * final int SERO = TetrisDemo.SERO; //���� �� static JButton btn[][] =
	 * TetrisDemo.btn; //�� ��ư�� static final int TIME = TetrisDemo.TIME; // ���� ��������
	 * �ֱ�(���� : ms) static final Color block_color = TetrisDemo.block_color; //����� ����
	 * static final Color ground_color = TetrisDemo.ground_color; static boolean
	 * game_over = TetrisDemo.game_over; //������ �������� ����
	 * 
	 * isGameOver(), blockIsFixed() ��밡��
	 */

	int point = 3; // ���ʻ����� ���� �߽�, ��Ÿ���� ��ġ�� ����

	@Override
	boolean makeBlock() {
		super.setList(4); // ��� ������ 4��

		// list�� I����� ����� btn����
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(0, point + 2);
		list[3] = new Point(0, point + 3);

		// ������ �ڸ��� �̹� ����� �ִ� ��� -> ��������
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

		return true; // �� ���� �Ϸ�
	}

	final int cases = 2; // ����� ���� ��Ÿ��

	@Override
	public void Rotate() {

		if (blockIsFixed())
			return;

		eraseBlock(); // �� �����

		Point[] tmp = new Point[4];// ���� �� ����
		for (int i = 0; i < list.length; i++)
			tmp[i] = new Point(list[i].x, list[i].y);

		Point tmplist[] = new Point[4];

		// ȸ�� ����
		if (RotateCnt == 0) { // '��' -> '��'
			tmplist[0] = new Point(list[0].x, list[0].y + 3);
			tmplist[1] = new Point(list[1].x + 1, list[1].y + 2);
			tmplist[2] = new Point(list[2].x + 2, list[2].y + 1);
			tmplist[3] = new Point(list[3].x + 3, list[3].y);
		} else if (RotateCnt == 1) { // '��' -> '��'
			tmplist[0] = new Point(list[0].x, list[0].y - 3);
			tmplist[1] = new Point(list[1].x - 1, list[1].y - 2);
			tmplist[2] = new Point(list[2].x - 2, list[2].y - 1);
			tmplist[3] = new Point(list[3].x - 3, list[3].y);
		}

		RotateCnt = rotateDraw(tmplist, tmp, RotateCnt, cases);

	}

}
