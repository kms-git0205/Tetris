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

	static final int GARO = 10; // ���� ��
	static final int SERO = 20; // ���� ��

	static final int TIME = 500; // ���� �������� �ֱ�(���� : ms)
	static final Color block_color = Color.DARK_GRAY; // ����� ����
	static final Color ground_color = Color.white;

	static JButton btn[][] = new JButton[SERO][GARO]; // �� ��ư��
	static boolean game_over = false; // ������ �������� ����

	static final String path = "./Tetris_state.dat"; // ���� ������

	static boolean isLoaded = false; // ������ �ҷ����� �Ǿ����� ����

	static int score = 0; // ���� -> ���� ���� �� ���� �� �ʱ�ȭ

	JPanel menu = new JPanel(); // �޴�ȭ��
	JPanel game = new JPanel(); // ����ȭ��

	// ������ ����Ʈ
	Block[] rlist = { new IBlock(), new JBlock(), new LBlock(), new OBlock(), new TBlock(), new SBlock(),
			new ZBlock() };
	Block random_block; // ������ ���� ��



	static void lineDown(int n) { // n��°�� ������ ��ü ��ĭ ���������� ���� -> n��° ���� �������� �� ����
		for (int i = n; i >= 1; i--) {
			for (int j = 0; j < GARO; j++) {

				// �ٴ��� ����� ��ĥ�Ǿ� �ִ� ���
				if (btn[i][j].getBackground() == block_color && i == SERO - 1)
					continue;

				// ���� ���̵��� ���� ����
				if (i + 1 < SERO && btn[i][j].getBackground() == block_color
						&& btn[i + 1][j].getBackground() == block_color)
					continue;

				btn[i][j].setBackground(btn[i - 1][j].getBackground());
				btn[i - 1][j].setBackground(ground_color);
			}
		}
	}

	static void lineCheck() { // �� ���� ��� ä�������� Ȯ�� �� �������� ����

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

	static void removeLine(int n) { // ä���� �� �� ����� -> n��° �� ����

		if (n == -1)
			return; // -1�ΰ��, ������ �� ����

		for (int i = 0; i < GARO; i++)
			btn[n][i].setBackground(ground_color);

		score += GARO;
		lineDown(n);

	}

	void playing() throws InterruptedException { // ���� �÷��� ����

		// �ϳ��� ���� ����
		while (!game_over) { // ������ ������ ��������
			
			if (!isLoaded) { // �ҷ������� ��� �̹� �������� �����Ǿ� ����
				
				random_block = rlist[(int)(Math.random() * 7)];
				random_block.setRotateCnt(0);
			}

			random_block.blockPlay();

		}
		JOptionPane.showMessageDialog(null, "Game Over!\nScore : " + score);

	}

	void gameQuit() { // ���� ���� ����
		gamePlay.interrupt();
		for (int i = 0; i < SERO; i++) // ���� �ʱ�ȭ
			for (int j = 0; j < GARO; j++)
				btn[i][j].setBackground(ground_color);

		Block.list = new Block.Point[4];
	}

	void gameStart() { // ���ӽ���
		game_over = false;
		gamePlay.interrupt();
		gamePlay = new Thread(new Runnable() { // �����带 ���� �Ҵ� (���ο� ������ ����)
			@Override
			public void run() {
				try {
					playing();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		if (!isLoaded) { // �ҷ������� ��� �ʱ�ȭ ����
			Block.list = new Block.Point[4];
			for (int i = 0; i < SERO; i++) // ���� �ʱ�ȭ
				for (int j = 0; j < GARO; j++)
					btn[i][j].setBackground(ground_color);
		}
		gamePlay.start();
	}

	// ���� ����
	static void NewFile(String path) { // path�� ���ο� ���� ����, ��λ��� ������ ����� ������
		try {
			System.out.println(path);

			File f = new File(path);
			f.createNewFile();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// ���� ����
	static void UpdateFile(String path, String Text) { // path ������ ������ Text�� ����
		try {
			File f = new File(path); // ���� ����
			if (f.exists() == false) { // ������ ���°�� ����
				NewFile(path);
			}

			// ���� ����
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(f));
			buffWrite.write(Text, 0, Text.length());

			// ���� �ݱ�
			buffWrite.flush();
			buffWrite.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void fileSave(String path) { // ���� ���
		String res = ""; // ������ ���ڿ�
		for (int i = 0; i < SERO; i++) {
			for (int j = 0; j < GARO; j++) { // �� ��ư���� 1 �Ǵ� 0 ����
				res += (btn[i][j].getBackground() == block_color) ? Integer.toString(1) : Integer.toString(0);
			}
		}

		res += "\n";

		for (int i = 0; i < 4; i++) { // list�� �ִ� (x.y)�����ֵ� ����
			res += Integer.toString(Block.list[i].x) + "\n" + Integer.toString(Block.list[i].y) + "\n";
		}

		res += random_block.getRotateCnt() + "\n"; // ���� ���� ȸ������ ����

		res += Integer.toString(score) + "\n"; // ���� ���� ����

		res += random_block.getClass().getName(); // ���� ���� Ÿ���� ����
		System.out.println(random_block.getClass().getName());

		UpdateFile(path, res); // ���� ������Ʈ
	}

	void fileLoad(String path) { // ���� ��ο��� �о�ͼ� ��ư ���� �� ���� ��� ���� ����

		String res = ""; // ������ ���ڿ�

		int n; // ���� ���� ������ ����
		try {
			BufferedReader buff = new BufferedReader(new FileReader(path)); // ���۸� �Ἥ ��Ʈ���� ȿ�� ������Ŵ

			for (int i = 0; i < SERO; i++) {
				for (int j = 0; j < GARO; j++) { // �� ��ư���� 1 �Ǵ� 0 ����Ǿ� ����
					n = buff.read() - 48;

					if (n == 1)
						btn[i][j].setBackground(block_color); // n���� ���� btn�� ���� ����
					else
						btn[i][j].setBackground(ground_color);
				}
			}

			buff.readLine();

			for (int i = 0; i < 4; i++) { // list�� ��Ҵ� �׻� 4��
				// list ��� �ϳ��� x, y�� ������ o
				Block.list[i] = new Block.Point(Integer.parseInt(buff.readLine()), Integer.parseInt(buff.readLine()));
				btn[Block.list[i].x][Block.list[i].y].setBackground(block_color);

			}

			int rotateState = Integer.parseInt(buff.readLine()); // ȸ������ �ҷ���
			System.out.println("ȸ������ : " + rotateState);
			
			score = Integer.parseInt(buff.readLine()); // ���� �ҷ���

			String className = buff.readLine(); // ����Ǿ��ִ� ���� ����� Ŭ�����̸�
			System.out.println("className : " + className);
			
			// Tetris.SBlock
			// ���� �������� �� ����
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
				System.out.println("���� �ȵǴ� �κ� - �� �Ҵ��� �ȵ�");
			}
			random_block.RotateCnt = rotateState; // ȸ������ ����

			buff.close();

			System.out.println(className + " " + random_block.getRotateCnt() + " " + score);

			JOptionPane.showMessageDialog(null, "Game Loaded, Ȯ�ι�ư�� ������ 3�� �� �����մϴ�");
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

			requestFocus(); // Ű �̺�Ʈ������ ��Ŀ�� ��������

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
	
	// �� �̵�, ��������, ���α׷�����, ���� �� �ҷ����� ���� ���� �̺�Ʈ ó��
	class MyListener implements KeyListener {

		@Override
		// Ű�� ������ ���� �̺�Ʈ�� ���
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			switch (key) {

			case KeyEvent.VK_ENTER: // ���� ����
				gameQuit();
				score = 0;
				isLoaded = false;
				gameStart();
				break;
				
			//��� �̵� ����
			case KeyEvent.VK_LEFT:
				random_block.blockLeft();

				break;
			case KeyEvent.VK_RIGHT:
				random_block.blockRight();
				break;
				
			case KeyEvent.VK_DOWN:
				random_block.blockDown();
				break;		
				
			case KeyEvent.VK_SPACE: // ������ ��������
				random_block.quickDown();
				lineCheck();

				// �� �ʱ�ȭ -> RotateCnt �ʱ�ȭ�� ����
				rlist[1] = new IBlock();
				rlist[2] = new JBlock();
				rlist[3] = new LBlock();
				rlist[4] = new OBlock();
				rlist[5] = new TBlock();
				rlist[6] = new SBlock();
				rlist[0] = new ZBlock();

				random_block = rlist[(int) (Math.random() * 7)]; 
				// ���ο� ���� ������ �����ϱ� ���� ���⼭ makeBlock()
				random_block.makeBlock();
				break;
				
			case KeyEvent.VK_R: // rŰ : ȸ��
				random_block.Rotate();
				break;
						
			case KeyEvent.VK_S: // ���̺�
				fileSave(path); // ���� ��ο� �����������
				gamePlay.interrupt();
				JOptionPane.showMessageDialog(null, "Game Saved");
				gameQuit();
				break;

			case KeyEvent.VK_O: // �ҷ�����

				gameQuit();
				gameQuit();
				isLoaded = true;
				fileLoad(path);
				gameStart();
				break;

			case KeyEvent.VK_Q: // ���� ���� ����
				gameQuit();
				score = 0;
				random_block.setRotateCnt(0);
				break;

			case KeyEvent.VK_ESCAPE: // ���α׷�����
				quit();
				break;
			

			
			}
			System.out.println(e);
		}

		public void keyTyped(KeyEvent e) {	}
		public void keyReleased(KeyEvent e) {	}
	}
	
	void makeUI() { // ��Ʈ���� ȭ�� ����
		getContentPane().setLayout(new GridLayout(20, 10)); // ���̾ƿ� ����
		setSize(500, 700);
		for (int i = 0; i < SERO; i++) { // ��ư �߰�
			for (int j = 0; j < GARO; j++) {
				btn[i][j] = new JButton();
				btn[i][j].setBackground(ground_color);
				getContentPane().add(btn[i][j]);

				btn[i][j].addActionListener(new ActionListener() { //��ư�� Ŭ���ص� ���� JFrame�� ��Ŀ���� �α�
					@Override
					public void actionPerformed(ActionEvent e) {
						requestFocus(); 
					}
				});
			}
		}
		setVisible(true);
	}

	TetrisDemo() throws InterruptedException { // ���ӽ���ȭ�� ������
		makeUI();
		addKeyListener(new MyListener()); // ��� �̵��� ���� Ű ������ ����
		setFocusable(true); // ��Ŀ���� JFrame�� �α�
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws InterruptedException {
		Thread menu = new Thread(new Runnable() {
			@Override
			public void run() { // ó���� ����â ���
				JOptionPane.showMessageDialog(null,
						"<��Ʈ���� ��� ����>\n���� :                           ENTER\n���̵� :                  ����Ű\n��ȸ�� :                   R\n���� ������� :         SPACE BAR\n������� ���� :         S\n�ҷ����� :                   O\n��������(�ʱ�ȭ) :   Q\n���α׷� ���� :         ESC or ������ư ������\n�������� ��� :         ./file_state.dat");
			}
		});
		menu.start();

		TetrisDemo t = new TetrisDemo();

	}

}
