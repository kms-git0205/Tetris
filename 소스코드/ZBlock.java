

public class ZBlock extends Block {

	int point = 4; // ���� ��� ��������

	@Override
	boolean makeBlock() {

		super.setList(4); // ��� ������ 4��

		// list�� Z����� ����� btn����
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(1, point + 1);
		list[3] = new Point(1, point + 2);

		// ������ �ڸ��� �̹� ����� �ִ� ��� -> ��������
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			if (btn[x][y].getBackground() == block_color)
				return false;
			else
				btn[x][y].setBackground(block_color);
		}

		return true; // �� ���� �Ϸ�
	}

	final int cases = 2; // ����� ���� ��Ÿ��

	@Override
	void Rotate() {
		eraseBlock(); // �� �����

		Point[] tmp = new Point[4];// ���� �� ����
		for (int i = 0; i < list.length; i++)
			tmp[i] = new Point(list[i].x, list[i].y);

		Point tmplist[] = new Point[4];

		// ȸ�� ����
		if (RotateCnt == 0) { // 'Z'���� �����ؼ� �ݽð�
			tmplist[0] = new Point(list[0].x - 1, list[0].y + 1);
			tmplist[1] = new Point(list[1].x, list[1].y - 1);
			tmplist[2] = new Point(list[2].x - 1, list[2].y);
			tmplist[3] = new Point(list[3].x, list[3].y - 2);
		} else if (RotateCnt == 1) {
			tmplist[0] = new Point(list[0].x + 1, list[0].y - 1);
			tmplist[1] = new Point(list[1].x, list[1].y + 1);
			tmplist[2] = new Point(list[2].x + 1, list[2].y);
			tmplist[3] = new Point(list[3].x, list[3].y + 2);
		}

		RotateCnt = rotateDraw(tmplist, tmp, RotateCnt, cases);

	}

}
