import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Random;
    public class MainFrame extends JFrame {
        //为了下面图片和组件的方便使用，将资源基本都定义在一起
        private static ImageIcon mainFrameIcon;//游戏窗口背景图片
        private static ImageIcon logoIcon;       //白色背景的2048图片
        private static ImageIcon scoreIcon;       //score图片
        private static ImageIcon bestIcon;       //best图片
        private static ImageIcon newGameIcon;  //newGame按钮图片
        private static ImageIcon emptyIcon;       //空块（零块）
        private static ImageIcon twoIcon;      //2块
        private static ImageIcon fourIcon;       //4块
        private static ImageIcon eightIcon;      //8块

        private JLabel logo_Label;                //2048Lable
        private JLabel contact_Label;            //HOW TO PLAY Label
        private JLabel score_Label;                //scoreLabel
        private static JLabel scoreText_Label;    //scoreTextLabel
        private JLabel best_Label;                //bestLabel
        private static JLabel bestText_Label;    //bestTextLabel
        private JButton newgameBtn;                //newGame按钮
        private JLabel back_Label;                //主窗体背景组件
        private static int[][] block_Data = new int[4][4];    //一个二维int数组数据域
        private static JLabel[][] block_Lable = new JLabel[4][4];    //一个二维组件块域和上面数组对应，各个数字块的移动与合并就是操作者两个数组共同的结果

        //定义一个静态块，将所需要初始化的图片一次性初始化完毕，只初始化0、2、4块的原因是最初只会出现这三种块
        static {
            mainFrameIcon = new ImageIcon("photo/mainframebg.jpg");
            logoIcon = new ImageIcon("photo/logo.png");
            scoreIcon = new ImageIcon("photo/score.png");
            bestIcon = new ImageIcon("photo/best.png");
            newGameIcon = new ImageIcon("photo/newgame.png");
            emptyIcon = new ImageIcon("photo/0.png");
            twoIcon = new ImageIcon("photo/2.png");
            fourIcon = new ImageIcon("photo/4.png");
            eightIcon = new ImageIcon("photo/8.png");

        }

        //MainFrame的构造方法，在Swing中有这么一个问题在窗体中放置图片时，先放置的图片在最上层，后放置的在下层。
        //所以要先初始化游戏块，最后初始化窗体的背景。
        public MainFrame() {
            //初始化窗体大小位置等（设置窗体布局方式为自由布局）
            initBasic();
            //初始化开始游戏安按钮，得分，最好成绩等
            initNewGame();
            //初始化空块
            initEmptyBlocks();
            //初始化两个初始值（2或4）到block_Data数组中，并设置block_Label中对应块的图像
            initData();
            //初始化事件监听
            initListener();
            //初始化窗体背景
            initFrameBackGround();
            setVisible(true);
        }

        private void initListener() {
            //HOW TO PLAY?组件增加监听，设置为模拟超链接的那种方式
            contact_Label.addMouseListener(new MouseAdapter() {
                //鼠标进入设置颜色为蓝色并添加下划线，和提示信息
                public void mouseEntered(MouseEvent e) {
                    contact_Label.setText("<html><font color='blue'><u>" + contact_Label.getText() + "</u></font></html>");
                    contact_Label.setToolTipText("You will open the official website of 2048.");
                }

                //鼠标移出设置为原来的文本，并将提示信息设置为""
                public void mouseExited(MouseEvent e) {
                    contact_Label.setText("HOW TO PLAY?");
                    contact_Label.setToolTipText("");
                }

                //鼠标单击打开2048的官网
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://2048game.com/"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            //newGame按钮设置鼠标放上去时变成手型样式
            newgameBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            //newGame按钮绑定触发事件，即开始依据新游戏
            newgameBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //清空block_Label,全部设置为空块，并设置block_Data数据都为0
                    reSetBlocks();
                    //初始化两个随机数（2或4），并刷新界面
                    initData();
                }
            });
            //增加键盘事件，并将数据数组传入
            newgameBtn.addKeyListener(new MyKeyListener(block_Data));
        }

        //重置每个块元素
        public static void reSetBlocks() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    block_Lable[i][j].setIcon(emptyIcon);
                    block_Data[i][j] = 0;
                }
            }
        }

        //调用两次生成随机块的方法
        public static void initData() {
            for (int n = 0; n < 2; n++) {
                createOneRandomNumber();
            }
        }

        //由于每次移动后，都要生成一个随机块，所以单独定义一个方法来生成随机块，生成2或4的概率是4:1
        //将生成的数存放到block_Data中，并设置对应组件的图片
        public static void createOneRandomNumber() {
            int i, j;
            Random random = new Random();
            i = random.nextInt(4);
            j = random.nextInt(4);
            while (true) {
                if (block_Data[i][j] == 0) {
                    break;
                } else {
                    i = random.nextInt(4);
                    j = random.nextInt(4);
                }
            }
            block_Data[i][j] = random.nextDouble() > 0.2 ? 2 : 4;
            if (block_Data[i][j] == 2) {
                block_Lable[i][j].setIcon(twoIcon);
            } else {
                block_Lable[i][j].setIcon(fourIcon);
            }
        }

        //初始化16个0块（空块），30和166都是自己调试出的左边距和上边距，块元素的宽和高都是90，块之间的
        //间隔是10，所以平均出来每一个块元素的位置都是加上95，这个自己在纸上画图很容易理解。
        //比较难理解的是，此时初始化块的顺序是按照列初始化的，所以如block_Data[0][3]就表示的是第0列，
        //第三行的那个元素，这个点必须要区分清楚，否则在判断移动问题的逻辑就比较混乱了。
        private void initEmptyBlocks() {
            //设置16个空块，设置每个块的位置，将块添加到主界面中
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    block_Lable[i][j] = new JLabel(emptyIcon);
                    block_Lable[i][j].setBounds(30 + i * 95, 166 + j * 95, 90, 90);
                    this.add(block_Lable[i][j]);
                }
            }
        }

        //初始化界面上部分组件，并添加到合适的位置，setBounds(x,y,width,height)方法用于设置组件
        //在窗体中的绝对位置和组件的大小。位置基本没有什么说的，这个基本都是靠自己调试出来的，不熟悉的话
        //调试几个组件就差不多掌握了，大小就基本上都设置为JLabel中图片的大小即可。
        private void initNewGame() {
            logo_Label = new JLabel(logoIcon);
            logo_Label.setBounds(10, 25, 224, 84);
            this.add(logo_Label);
            contact_Label = new JLabel("HOW TO PLAY?");
            contact_Label.setBounds(10, 110, 224, 20);
            this.add(contact_Label);
            newgameBtn = new JButton(newGameIcon);
            newgameBtn.setBounds(280, 80, 142, 44);
            this.add(newgameBtn);
            score_Label = new JLabel(scoreIcon);
            best_Label = new JLabel(bestIcon);
            score_Label.setBounds(280, 25, 60, 24);
            best_Label.setBounds(360, 25, 60, 24);
            this.add(score_Label);
            this.add(best_Label);
            scoreText_Label = new JLabel("0");
            bestText_Label = new JLabel("0");
            scoreText_Label.setBounds(290, 55, 60, 24);
            bestText_Label.setBounds(370, 55, 60, 24);
            this.add(scoreText_Label);
            this.add(bestText_Label);
        }

        private void initFrameBackGround() {
            back_Label = new JLabel(mainFrameIcon);
            back_Label.setBounds(6, 6, 420, 556);
            this.add(back_Label);
        }

        private void initBasic() {
            this.setTitle("2048game");    //设置标题
            this.setSize(450, 614);        //设置窗体大小
            this.setLocation(700, 200);    //设置窗体显示位置
            this.setLayout(null);        //设置窗体布局方式为自由布局方式（自由布局就可以按照像素位置去放置组件）
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);    //设置窗体x为默认关闭窗口
        }

        //根据数据数组来刷新游戏界面，使视觉效果和真实数据是一致的，reFreshScore是在每次移动后刷新当前得分
        public static void upDateUI(int[][] block_Data) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    block_Lable[i][j].setIcon(new ImageIcon("photo/" + block_Data[i][j] + ".png"));
                }
            }
        }

        public static void reFreshScore() {
            int max = block_Data[0][0];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (block_Data[i][j] > max) {
                        max = block_Data[i][j];
                    }
                }
            }
            scoreText_Label.setText(max + "");
        }
    }

