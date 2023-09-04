

import java.awt.Color;

import javax.swing.JButton;

public abstract class Block {

	static class Point { // 블록의 위치를 저장할 객체
		int x;
		int y;

		Point(int xx, int yy) {
			x = xx;
			y = yy;
		}
	}

	static Point[] list = new Point[4]; // 블럭을 이루는 버튼들의 좌표를 저장

	static void setList(int size) { // list 배열 크기 재할당
		list = new Point[size];
	}

	// 블럭들에 대한 내용을 담은 클래스 -> 실제 블록들은 이 클래스를 상속받음
	// I, J, L, O, S, T, Z 모양 블록들 구현

	// TetrisDemo에서 전역변수들 불러옴
	final int GARO = TetrisDemo.GARO; // 가로 수
	final int SERO = TetrisDemo.SERO; // 세로 수
	JButton btn[][] = TetrisDemo.btn; // 각 버튼들
	final int TIME = TetrisDemo.TIME; // 블럭이 내려오는 주기(단위 : ms)
	final Color block_color = TetrisDemo.block_color; // 블록의 색깔
	final Color ground_color = TetrisDemo.ground_color;
	boolean game_over = TetrisDemo.game_over; // 게임이 끝났는지 여부
	boolean isLoaded = TetrisDemo.isLoaded; // 불러오기 여부
	int RotateCnt = 0; // 현재의 회전상태를 나타냄

	public int getRotateCnt() { // 회전상태 얻기 -> 파일저장에 필요
		return RotateCnt;
	}

	public void setRotateCnt(int cnt) { // 회전상태 설정 -> 파일저장에 필요
		RotateCnt = cnt;
	}

	boolean isGameOver() { // 현재 게임오버 상태인지를 반환
		for (int i = 0; i < GARO; i++)
			if (btn[0][i].getBackground() == block_color)
				return true;
		return false;
	}

	boolean blockIsFixed() { // 블럭이 바닥에 닿았는지 여부를 반환 -> 바닥만 확인

		boolean flag = false; // 블럭 고정여부

		for (int i = list.length - 1; i >= 0; i--) { // 각 버튼마다 아래가 비었는지 확인하면서 지워나감
			int x = list[i].x, y = list[i].y;
			if (x >= 0 && x + 1 < SERO && btn[x + 1][y].getBackground() == ground_color) { // 아래칸이 비어있는 경우
				btn[x][y].setBackground(ground_color);
			} else {
				flag = true;
				break;
			}
		}

		for (int i = 0; i < list.length; i++) { // 지웠던 버튼들 다시 블록으로 만들기
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(block_color);
		}

		return flag;

	}

	// 하나의 블럭이 생성되고 고정되기 까지의 동작들을 구현
	public void blockPlay() throws InterruptedException {

		if (!TetrisDemo.isLoaded) { // 불러오기가 된 경우에는 블록을 새로 생성할 필요가 없음
			RotateCnt = 0;
			if (!makeBlock()) {
				TetrisDemo.game_over = true;
				return; // 블럭 생성 -> 실패 시 게임 오버
			}
		}
		TetrisDemo.isLoaded = false;

		while (true) {

			if (blockIsFixed()) // 블럭이 바닥에 닿아서 고정되면 탈출
				break;

			blockDown(); // 블럭이 한칸 내려감

			Thread.sleep(TIME);
		}

		// 한 줄이 다 차면 줄 제거
		TetrisDemo.lineCheck();

		// 블럭이 천장에 닿아서 게임오버이면 게임종료
		if (isGameOver())
			TetrisDemo.game_over = true;

	}

	abstract boolean makeBlock(); // 해당 블럭 생성 - 생성 실패 시 게임 오버

	abstract void Rotate(); // 해당 블럭이 90도 회전하도록 구현

	public void quickDown() { // 블럭 빠르게 내려오도록 하기
		while (!blockIsFixed())
			blockDown();
	}

	public void blockDown() { // 블럭 한칸 아래로

		if (blockIsFixed())
			return;

		for (int i = list.length - 1; i >= 0; i--) {

			int x = list[i].x, y = list[i].y;
			if (x + 1 < SERO) { // 아래로 이동
				btn[x + 1][y].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].x += 1;
			}
		}
	}

	public void blockLeft() {
		if (blockIsFixed())
			return;

		boolean flag = true; // 왼쪽으로 가도 되는지 여부

		for (int i = 0; i < list.length; i++) { // 왼쪽으로 갈 수 있는지 확인
			int x = list[i].x, y = list[i].y;
			if (y - 1 < 0 || btn[x][y - 1].getBackground() == block_color) { // 이동 불가능
				flag = false;
				break;
			} else
				btn[x][y].setBackground(ground_color);// 해당 블럭 지워놓음 -> 이후에 다시 복구
		}

		if (flag) { // 블럭 한칸 왼쪽으로 이동
			for (int i = 0; i < list.length; i++) {
				int x = list[i].x, y = list[i].y;
				btn[x][y - 1].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].y -= 1;
			}
		} else { // 왼쪽으로 못가는 경우 -> 블럭 원상복구
			for (int i = 0; i < list.length; i++) {
				int x = list[i].x, y = list[i].y;
				btn[x][y].setBackground(block_color);
			}
		}

	}

	public void blockRight() {
		if (blockIsFixed())
			return;

		boolean flag = true; // 오른쪽으로 가도 되는지 여부

		for (int i = list.length - 1; i >= 0; i--) { // 오른쪽으로 갈 수 있는지 확인
			int x = list[i].x, y = list[i].y;
			if (y + 1 >= GARO || btn[x][y + 1].getBackground() == block_color) { // 이동 불가능
				flag = false;
				break;
			} else
				btn[x][y].setBackground(ground_color);// 해당 블럭 지워놓음 -> 이후에 다시 복구
		}

		if (flag) { // 블럭 한칸 오른쪽으로 이동
			for (int i = list.length - 1; i >= 0; i--) {
				int x = list[i].x, y = list[i].y;
				btn[x][y + 1].setBackground(block_color);
				btn[x][y].setBackground(ground_color);
				list[i].y += 1;
			}
		} else { // 오른쪽으로 못가는 경우 -> 블럭 원상복구
			for (int i = list.length - 1; i >= 0; i--) {
				int x = list[i].x, y = list[i].y;
				btn[x][y].setBackground(block_color);
			}
		}

	}

	public int blockOut() { // 블럭이 범위를 벗어났는지 여부를 반환 -> -1은 왼쪽으로, 1은 오른쪽으로 벗어난 경우, -2는 바닥과 닿는 경우, 0은 문제
							// 없음(충돌은 따로검사)

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

	public boolean blockConflict() { // 블럭 충돌여부를 반환 - 회전 후에 각 좌표가 가능한 값인지 검사할 때 사용
		if (blockOut() != 0)
			return true;
		for (int i = 0; i < list.length; i++) {
			// 각 버튼마다 벗어나거나, 색칠되어 있는 곳이 있으면 충돌
			int x = list[i].x, y = list[i].y;
			if (x < 0 || x >= SERO || y < 0 || y >= GARO || btn[x][y].getBackground() == block_color)
				return true;
		}
		return false;
	}

	public void drawBlock() { // 현재 list값에 있는 좌표 기준으로 블럭을 그림 -> 회전에 사용
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(block_color);
		}
	}

	public void eraseBlock() { // 현재 list값에 있는 좌표 기준으로 블럭을 지움 -> 회전에 사용
		for (int i = 0; i < list.length; i++) {
			int x = list[i].x, y = list[i].y;
			btn[x][y].setBackground(ground_color);

		}
	}

	public int rotateDraw(Point[] tmplist, Point[] tmp, int RotateCnt, int cases) { // tmplist 체크하고, 충돌 없으면 블럭 회전 ->
																					// rotate함수에서 파라미터 전달
		// tmplist는 회전 후의 블럭, tmp는 회전 이전의 블럭, RotateCnt는 현재 회전상태, cases는 회전상태의 경우의 수 를
		// 의미
		boolean isConflict = false;
		for (int i = 0; i < 4; i++) {
			if (tmplist[i].x < 0 || tmplist[i].y < 0 || tmplist[i].x >= SERO || tmplist[i].y >= GARO
					|| btn[tmplist[i].x][tmplist[i].y].getBackground() == block_color) {
				isConflict = true;
				break;
			}
		}

		if (isConflict) { // 충돌인경우 -> 다시 되돌림
			for (int i = 0; i < list.length; i++)
				list[i] = tmp[i];
		} else { // 여기서 실질적인 회전이 일어남
			for (int i = 0; i < list.length; i++)
				list[i] = tmplist[i];
			RotateCnt = (RotateCnt + 1) % cases;
		}

		drawBlock(); // 블럭 그리기
		return RotateCnt;
	}

}
