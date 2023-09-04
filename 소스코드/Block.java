

import java.awt.Color;

import javax.swing.JButton;

public abstract class Block {

	static class Point { // ����� ��ġ�� ������ ��ü
		int x;
		int y;

		Point(int xx, int yy) {
			x = xx;
			y = yy;
		}
	}

	static Point[] list = new Point[4]; // ���� �̷�� ��ư���� ��ǥ�� ����

	static void setList(int size) { // list �迭 ũ�� ���Ҵ�
		list = new Point[size];
	}

	// ���鿡 ���� ������ ���� Ŭ���� -> ���� ��ϵ��� �� Ŭ������ ��ӹ���
	// I, J, L, O, S, T, Z ��� ��ϵ� ����

	// TetrisDemo���� ���������� �ҷ���
	final int GARO = TetrisDemo.GARO; // ���� ��
	final int SERO = TetrisDemo.SERO; // ���� ��
	JButton btn[][] = TetrisDemo.btn; // �� ��ư��
	final int TIME = TetrisDemo.TIME; // ���� �������� �ֱ�(���� : ms)
	final Color block_color = TetrisDemo.block_color; // ����� ����
	final Color ground_color = TetrisDemo.ground_color;
	boolean game_over = TetrisDemo.game_over; // ������ �������� ����
	boolean isLoaded = TetrisDemo.isLoaded; // �ҷ����� ����
	int RotateCnt = 0; // ������ ȸ�����¸� ��Ÿ��

	public int getRotateCnt() { // ȸ������ ��� -> �������忡 �ʿ�
		return RotateCnt;
	}

	public void setRotateCnt(int cnt) { // ȸ������ ���� -> �������忡 �ʿ�
		RotateCnt = cnt;
	}

	boolean isGameOver() { // ���� ���ӿ��� ���������� ��ȯ
		for (int i = 0; i < GARO; i++)
			if (btn[0][i].getBackground() == block_color)
				return true;
		return false;
	}

	boolean blockIsFixed() { // ���� �ٴڿ� ��Ҵ��� ���θ� ��ȯ -> �ٴڸ� Ȯ��

		boolean flag = false; // �� ��������

		for (int i = list.length - 1; i >= 0; i--) { // �� ��ư���� �Ʒ��� ������� Ȯ���ϸ鼭 ��������
			int x = list[i].x, y = list[i].y;
			if (x >= 0 && x + 1 < SERO && btn[x + 1][y].getBackground() == ground_color) { // �Ʒ�ĭ�� ����ִ� ���
				btn[x][y].setBackground(ground_color);
			} else {
				flag = true;
				break;
			}
		}

		for (int i = 0; i < list.length; i++) { // ������ ��ư�� �ٽ� ������� �����
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(block_color);
		}

		return flag;

	}

	// �ϳ��� ���� �����ǰ� �����Ǳ� ������ ���۵��� ����
	public void blockPlay() throws InterruptedException {

		if (!TetrisDemo.isLoaded) { // �ҷ����Ⱑ �� ��쿡�� ����� ���� ������ �ʿ䰡 ����
			RotateCnt = 0;
			if (!makeBlock()) {
				TetrisDemo.game_over = true;
				return; // �� ���� -> ���� �� ���� ����
			}
		}
		TetrisDemo.isLoaded = false;

		while (true) {

			if (blockIsFixed()) // ���� �ٴڿ� ��Ƽ� �����Ǹ� Ż��
				break;

			blockDown(); // ���� ��ĭ ������

			Thread.sleep(TIME);
		}

		// �� ���� �� ���� �� ����
		TetrisDemo.lineCheck();

		// ���� õ�忡 ��Ƽ� ���ӿ����̸� ��������
		if (isGameOver())
			TetrisDemo.game_over = true;

	}

	abstract boolean makeBlock(); // �ش� �� ���� - ���� ���� �� ���� ����

	abstract void Rotate(); // �ش� ���� 90�� ȸ���ϵ��� ����

	public void quickDown() { // �� ������ ���������� �ϱ�
		while (!blockIsFixed())
			blockDown();
	}

	public void blockDown() { // �� ��ĭ �Ʒ���

		if (blockIsFixed())
			return;

		for (int i = list.length - 1; i >= 0; i--) {

			int x = list[i].x, y = list[i].y;
			if (x + 1 < SERO) { // �Ʒ��� �̵�
				btn[x + 1][y].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].x += 1;
			}
		}
	}

	public void blockLeft() {
		if (blockIsFixed())
			return;

		boolean flag = true; // �������� ���� �Ǵ��� ����

		for (int i = 0; i < list.length; i++) { // �������� �� �� �ִ��� Ȯ��
			int x = list[i].x, y = list[i].y;
			if (y - 1 < 0 || btn[x][y - 1].getBackground() == block_color) { // �̵� �Ұ���
				flag = false;
				break;
			} else
				btn[x][y].setBackground(ground_color);// �ش� �� �������� -> ���Ŀ� �ٽ� ����
		}

		if (flag) { // �� ��ĭ �������� �̵�
			for (int i = 0; i < list.length; i++) {
				int x = list[i].x, y = list[i].y;
				btn[x][y - 1].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].y -= 1;
			}
		} else { // �������� ������ ��� -> �� ���󺹱�
			for (int i = 0; i < list.length; i++) {
				int x = list[i].x, y = list[i].y;
				btn[x][y].setBackground(block_color);
			}
		}

	}

	public void blockRight() {
		if (blockIsFixed())
			return;

		boolean flag = true; // ���������� ���� �Ǵ��� ����

		for (int i = list.length - 1; i >= 0; i--) { // ���������� �� �� �ִ��� Ȯ��
			int x = list[i].x, y = list[i].y;
			if (y + 1 >= GARO || btn[x][y + 1].getBackground() == block_color) { // �̵� �Ұ���
				flag = false;
				break;
			} else
				btn[x][y].setBackground(ground_color);// �ش� �� �������� -> ���Ŀ� �ٽ� ����
		}

		if (flag) { // �� ��ĭ ���������� �̵�
			for (int i = list.length - 1; i >= 0; i--) {
				int x = list[i].x, y = list[i].y;
				btn[x][y + 1].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].y += 1;
			}
		} else { // ���������� ������ ��� -> �� ���󺹱�
			for (int i = list.length - 1; i >= 0; i--) {
				int x = list[i].x, y = list[i].y;
				btn[x][y].setBackground(block_color);
			}
		}

	}

	public int blockOut() { // ���� ������ ������� ���θ� ��ȯ -> -1�� ��������, 1�� ���������� ��� ���, -2�� �ٴڰ� ��� ���, 0�� ����
							// ����(�浹�� ���ΰ˻�)

		for (int i = 0; i < list.length; i++) {
			if (list[i].x >= SERO)
				return -2;
			if (list[i].y < 0)
				return -1;
			if (list[i].y >= GARO)
				return 1;

		}
		return 0;
	}

	public boolean blockConflict() { // �� �浹���θ� ��ȯ - ȸ�� �Ŀ� �� ��ǥ�� ������ ������ �˻��� �� ���
		if (blockOut() != 0)
			return true;
		for (int i = 0; i < list.length; i++) {
			// �� ��ư���� ����ų�, ��ĥ�Ǿ� �ִ� ���� ������ �浹
			int x = list[i].x, y = list[i].y;
			if (x < 0 || x >= SERO || y < 0 || y >= GARO || btn[x][y].getBackground() == block_color)
				return true;
		}
		return false;
	}

	public void drawBlock() { // ���� list���� �ִ� ��ǥ �������� ���� �׸� -> ȸ���� ���
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(block_color);
		}
	}

	public void eraseBlock() { // ���� list���� �ִ� ��ǥ �������� ���� ���� -> ȸ���� ���
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(ground_color);

		}
	}

	public int rotateDraw(Point[] tmplist, Point[] tmp, int RotateCnt, int cases) { // tmplist üũ�ϰ�, �浹 ������ �� ȸ�� ->
																					// rotate�Լ����� �Ķ���� ����
		// tmplist�� ȸ�� ���� ��, tmp�� ȸ�� ������ ��, RotateCnt�� ���� ȸ������, cases�� ȸ�������� ����� �� ��
		// �ǹ�
		boolean isConflict = false;
		for (int i = 0; i < 4; i++) {
			if (tmplist[i].x < 0 || tmplist[i].y < 0 || tmplist[i].x >= SERO || tmplist[i].y >= GARO
					|| btn[tmplist[i].x][tmplist[i].y].getBackground() == block_color) {
				isConflict = true;
				break;
			}
		}

		if (isConflict) { // �浹�ΰ�� -> �ٽ� �ǵ���
			for (int i = 0; i < list.length; i++)
				list[i] = tmp[i];
		} else { // ���⼭ �������� ȸ���� �Ͼ
			for (int i = 0; i < list.length; i++)
				list[i] = tmplist[i];
			RotateCnt = (RotateCnt + 1) % cases;
		}

		drawBlock(); // �� �׸���
		return RotateCnt;
	}

}
