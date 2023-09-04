import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TetrisDemo extends JFrame {

	static final int GARO = 10; // 가로 수
	static final int SERO = 20; // 세로 수

	static final int TIME = 500; // 블럭이 내려오는 주기(단위 : ms)
	static final Color block_color = Color.DARK_GRAY; // 블록의 색깔
	static final Color ground_color = Color.white;

	static JButton btn[][] = new JButton[SERO][GARO]; // 각 버튼들
	static boolean game_over = false; // 게임이 끝났는지 여부

	static final String path = "./Tetris_state.dat"; // 파일 저장경로

	static boolean isLoaded = false; // 게임이 불러오기 되었는지 여부

	static int score = 0; // 점수 -> 게임 오버 때 공개 후 초기화

	JPanel menu = new JPanel(); // 메뉴화면
	JPanel game = new JPanel(); // 게임화면

	// 랜덤블럭 리스트
	Block[] rlist = { new IBlock(), new JBlock(), new LBlock(), new OBlock(), new TBlock(), new SBlock(),
			new ZBlock() };
	Block random_block; // 선정할 랜덤 블럭



	static void lineDown(int n) { // n번째줄 위부터 전체 한칸 내려오도록 구현 -> n번째 줄이 지워졌을 때 실행
		for (int i = n; i >= 1; i--) {
			for (int j = 0; j < GARO; j++) {

				// 바닥의 블록이 색칠되어 있는 경우
				if (btn[i][j].getBackground() == block_color && i == SERO - 1)
					continue;

				// 블럭이 쌓이도록 로직 구현
				if (i + 1 < SERO && btn[i][j].getBackground() == block_color
						&& btn[i + 1][j].getBackground() == block_color)
					continue;

				btn[i][j].setBackground(btn[i - 1][j].getBackground());
				btn[i - 1][j].setBackground(ground_color);
			}
		}
	}

	static void lineCheck() { // 한 줄이 모두 채워졌는지 확인 후 삭제까지 진행

		for (int j = SERO - 1; j >= 0; j--) {

			boolean flag = true;

			for (int i = 0; i < GARO; i++) {
				if (btn[j][i].getBackground() == ground_color) {
					flag = false;
					break;
				}
			}

			if (flag) {
				removeLine(j++);
			}
		}

	}

	static void removeLine(int n) { // 채워진 한 줄 지우기 -> n번째 줄 삭제

		if (n == -1)
			return; // -1인경우, 삭제할 줄 없음

		for (int i = 0; i < GARO; i++)
			btn[n][i].setBackground(ground_color);

		score += GARO;
		lineDown(n);

	}

	void playing() throws InterruptedException { // 게임 플레이 구현

		// 하나의 블럭을 놓기
		while (!game_over) { // 게임이 끝나면 빠져나옴
			
			if (!isLoaded) { // 불러오기의 경우 이미 랜덤블럭이 지정되어 있음
				
				random_block = rlist[(int)(Math.random() * 7)];
				random_block.setRotateCnt(0);
			}

			random_block.blockPlay();

		}
		JOptionPane.showMessageDialog(null, "Game Over!\nScore : " + score);

	}

	void gameQuit() { // 현재 게임 종료
		gamePlay.interrupt();
		for (int i = 0; i < SERO; i++) // 색깔 초기화
			for (int j = 0; j < GARO; j++)
				btn[i][j].setBackground(ground_color);

		Block.list = new Block.Point[4];
	}

	void gameStart() { // 게임시작
		game_over = false;
		gamePlay.interrupt();
		gamePlay = new Thread(new Runnable() { // 쓰레드를 새로 할당 (새로운 시작을 위해)
			@Override
			public void run() {
				try {
					playing();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		if (!isLoaded) { // 불러오기의 경우 초기화 안함
			Block.list = new Block.Point[4];
			for (int i = 0; i < SERO; i++) // 색깔 초기화
				for (int j = 0; j < GARO; j++)
					btn[i][j].setBackground(ground_color);
		}
		gamePlay.start();
	}

	// 파일 생성
	static void NewFile(String path) { // path에 새로운 파일 생성, 경로상의 폴더는 만들어 놔야함
		try {
			System.out.println(path);

			File f = new File(path);
			f.createNewFile();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 파일 수정
	static void UpdateFile(String path, String Text) { // path 파일의 내용을 Text로 수정
		try {
			File f = new File(path); // 파일 변수
			if (f.exists() == false) { // 파일이 없는경우 생성
				NewFile(path);
			}

			// 파일 쓰기
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(f));
			buffWrite.write(Text, 0, Text.length());

			// 파일 닫기
			buffWrite.flush();
			buffWrite.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void fileSave(String path) { // 파일 경로
		String res = ""; // 저장할 문자열
		for (int i = 0; i < SERO; i++) {
			for (int j = 0; j < GARO; j++) { // 각 버튼마다 1 또는 0 저장
				res += (btn[i][j].getBackground() == block_color) ? Integer.toString(1) : Integer.toString(0);
			}
		}

		res += "\n";

		for (int i = 0; i < 4; i++) { // list에 있는 (x.y)순서쌍들 저장
			res += Integer.toString(Block.list[i].x) + "\n" + Integer.toString(Block.list[i].y) + "\n";
		}

		res += random_block.getRotateCnt() + "\n"; // 현재 블럭의 회전상태 저장

		res += Integer.toString(score) + "\n"; // 현재 점수 저장

		res += random_block.getClass().getName(); // 현재 블럭의 타입을 저장
		System.out.println(random_block.getClass().getName());

		UpdateFile(path, res); // 파일 업데이트
	}

	void fileLoad(String path) { // 파일 경로에서 읽어와서 버튼 상태 및 현재 블록 상태 저장

		String res = ""; // 저장할 문자열

		int n; // 읽은 글자 저장할 변수
		try {
			BufferedReader buff = new BufferedReader(new FileReader(path)); // 버퍼를 써서 스트림의 효율 증가시킴

			for (int i = 0; i < SERO; i++) {
				for (int j = 0; j < GARO; j++) { // 각 버튼마다 1 또는 0 저장되어 있음
					n = buff.read() - 48;

					if (n == 1)
						btn[i][j].setBackground(block_color); // n값에 따라 btn의 색깔 결정
					else
						btn[i][j].setBackground(ground_color);
				}
			}

			buff.readLine();

			for (int i = 0; i < 4; i++) { // list의 요소는 항상 4개
				// list 요소 하나의 x, y값 가져옴 o
				Block.list[i] = new Block.Point(Integer.parseInt(buff.readLine()), Integer.parseInt(buff.readLine()));
				btn[Block.list[i].x][Block.list[i].y].setBackground(block_color);

			}

			int rotateState = Integer.parseInt(buff.readLine()); // 회전상태 불러옴
			System.out.println("회전상태 : " + rotateState);
			
			score = Integer.parseInt(buff.readLine()); // 점수 불러옴

			String className = buff.readLine(); // 저장되어있던 현재 블록의 클래스이름
			System.out.println("className : " + className);
			
			// Tetris.SBlock
			// 현재 진행중인 블럭 지정
			System.out.println(rotateState + " " + className + " " + score);
			if (className.equals("SBlock")) {
				random_block = new SBlock();
			} else if (className.equals("IBlock")) {
				random_block = new IBlock();
			} else if (className.equals("JBlock")) {
				random_block = new JBlock();
			} else if (className.equals("LBlock")) {
				random_block = new LBlock();
			} else if (className.equals("OBlock")) {
				random_block = new OBlock();
			} else if (className.equals("TBlock")) {
				random_block = new TBlock();
			} else if (className.equals("ZBlock")) {
				random_block = new ZBlock();
			}
			else {
				System.out.println("오면 안되는 부분 - 블럭 할당이 안됨");
			}
			random_block.RotateCnt = rotateState; // 회전상태 설정

			buff.close();

			System.out.println(className + " " + random_block.getRotateCnt() + " " + score);

			JOptionPane.showMessageDialog(null, "Game Loaded, 확인버튼을 누르면 3초 후 시작합니다");
			Thread.sleep(3000);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}



	void quit() {
		setVisible(false);
		System.exit(0);
	}

	Thread showMenu = new Thread(new Runnable() {

		@Override
		public void run() {

			requestFocus(); // 키 이벤트리스너 포커스 가져오기

			menu.setVisible(true);

		}
	});

	Thread gamePlay = new Thread(new Runnable() {

		@Override
		public void run() {

			try {
				playing();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});
	
	// 블럭 이동, 게임종료, 프로그램종료, 저장 및 불러오기 등을 위한 이벤트 처리
	class MyListener implements KeyListener {

		@Override
		// 키가 눌렸을 때의 이벤트만 사용
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			switch (key) {

			case KeyEvent.VK_ENTER: // 게임 시작
				gameQuit();
				score = 0;
				isLoaded = false;
				gameStart();
				break;
				
			//블록 이동 구현
			case KeyEvent.VK_LEFT:
				random_block.blockLeft();

				break;
			case KeyEvent.VK_RIGHT:
				random_block.blockRight();
				break;
				
			case KeyEvent.VK_DOWN:
				random_block.blockDown();
				break;		
				
			case KeyEvent.VK_SPACE: // 빠르게 내려오기
				random_block.quickDown();
				lineCheck();

				// 블럭 초기화 -> RotateCnt 초기화를 위해
				rlist[1] = new IBlock();
				rlist[2] = new JBlock();
				rlist[3] = new LBlock();
				rlist[4] = new OBlock();
				rlist[5] = new TBlock();
				rlist[6] = new SBlock();
				rlist[0] = new ZBlock();

				random_block = rlist[(int) (Math.random() * 7)]; 
				// 새로운 블럭을 빠르게 생성하기 위해 여기서 makeBlock()
				random_block.makeBlock();
				break;
				
			case KeyEvent.VK_R: // r키 : 회전
				random_block.Rotate();
				break;
						
			case KeyEvent.VK_S: // 세이브
				fileSave(path); // 파일 경로에 현재상태저장
				gamePlay.interrupt();
				JOptionPane.showMessageDialog(null, "Game Saved");
				gameQuit();
				break;

			case KeyEvent.VK_O: // 불러오기

				gameQuit();
				gameQuit();
				isLoaded = true;
				fileLoad(path);
				gameStart();
				break;

			case KeyEvent.VK_Q: // 현재 게임 종료
				gameQuit();
				score = 0;
				random_block.setRotateCnt(0);
				break;

			case KeyEvent.VK_ESCAPE: // 프로그램종료
				quit();
				break;
			

			
			}
			System.out.println(e);
		}

		public void keyTyped(KeyEvent e) {	}
		public void keyReleased(KeyEvent e) {	}
	}
	
	void makeUI() { // 테트리스 화면 구성
		getContentPane().setLayout(new GridLayout(20, 10)); // 레이아웃 설정
		setSize(500, 700);
		for (int i = 0; i < SERO; i++) { // 버튼 추가
			for (int j = 0; j < GARO; j++) {
				btn[i][j] = new JButton();
				btn[i][j].setBackground(ground_color);
				getContentPane().add(btn[i][j]);

				btn[i][j].addActionListener(new ActionListener() { //버튼을 클릭해도 현재 JFrame에 포커스를 두기
					@Override
					public void actionPerformed(ActionEvent e) {
						requestFocus(); 
					}
				});
			}
		}
		setVisible(true);
	}

	TetrisDemo() throws InterruptedException { // 게임실행화면 생성자
		makeUI();
		addKeyListener(new MyListener()); // 블록 이동에 대한 키 리스너 설정
		setFocusable(true); // 포커스를 JFrame에 두기
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws InterruptedException {
		Thread menu = new Thread(new Runnable() {
			@Override
			public void run() { // 처음에 설명창 띄움
				JOptionPane.showMessageDialog(null,
						"<테트리스 기능 설명>\n시작 :                           ENTER\n블럭이동 :                  방향키\n블럭회전 :                   R\n빠른 블록착지 :         SPACE BAR\n현재상태 저장 :         S\n불러오기 :                   O\n게임종료(초기화) :   Q\n프로그램 종료 :         ESC or 엑스버튼 누르기\n저장파일 경로 :         ./file_state.dat");
			}
		});
		menu.start();

		TetrisDemo t = new TetrisDemo();

	}

}
