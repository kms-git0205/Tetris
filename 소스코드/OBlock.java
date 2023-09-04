

public class OBlock extends Block {

	int point = 4; // ���� ��� ��������

	@Override

	boolean makeBlock() {
		super.setList(4); // ��� ������ 4��

		// list�� O����� ����� btn����
		list[0] = new Point(0, point);
		list[1] = new Point(0, point + 1);
		list[2] = new Point(1, point);
		list[3] = new Point(1, point + 1);

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

	@Override
	void Rotate() {
		// ȸ���� ���ǹ��� ��
		return;
	}

}
