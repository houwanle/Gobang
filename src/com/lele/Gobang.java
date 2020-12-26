package com.lele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * author: hwl
 * date: 2020/12/26 12:47
 * version: 1.0.0
 * modified by:
 * description:
 */

// 继承JFrame来一个背景图片，那么，我们最快捷的操作，是让类，继承JFrame
public class Gobang extends JFrame {
    public static int[][] qp = new int[15][15];
    public static int flag = 1; // 0是白旗 1黑旗

    public void start() {
        // this指向，指向当前对象->Gobang -> extends JFrame -> JFrame
        this.setTitle("五子棋");
        this.setSize(768, 796);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage(
                        Gobang.class.getResource("/image/yc.png")
                )
        );

        // 可见
        this.setVisible(true);
        //获取到绘图对象
        Graphics g = this.getGraphics();
        // 添加鼠标的点击事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 得到我们的坐标位置 在我们数组里面，x代表行，y代表列 ，与坐标抽的xy恰恰相反
                int x = (e.getY() - 38) / 50;
                int y = (e.getX() - 10) / 50;

                // 每次下棋先判断一下，这个地方有没有被下过
                if (qp[x][y] != 0) { // Wzq.this   ->  this  this的应用指向
                    JOptionPane.showMessageDialog(Gobang.this, "此处已经有子", "警告", JOptionPane.WARNING_MESSAGE);
                    return; // 不让代码继续往下了
                }

                // 如果没有子了，那就开始下棋
                if (flag == 1) { // 说明是白子
                    qp[x][y] = 1;
                    g.setColor(Color.WHITE);
                } else {
                    qp[x][y] = 2;
                    g.setColor(Color.BLACK);
                }
                // 开始画图，还是上面说的，xy跟我们的xy是相反的
                g.fillOval(y * 50 + 10, x * 50 +38, 50, 50);

                //
                if (isWin(x, y)) {
                    // 胜利
                    String str = flag == 1 ? "白子" : "黑子";// 三元运算符
                    int index = JOptionPane.showConfirmDialog(Gobang.this,
                            "游戏结束，" + str + "获取胜利!\n是否再来一局", "游戏结束",
                            JOptionPane.YES_NO_OPTION);
                    if (index == 1) {
                        // 1代表否
                        System.exit(0); // 强行终止程序
                    }
                    // 再来一局
                    flag = 2;
                    // 数组全部改成0
                    for (int i = 0; i < qp.length; i++) {
                        for (int j = 0; j < qp[i].length; j++) {
                            qp[i][j] = 0;
                        }
                    }
                    // 重绘棋盘
                    Gobang.this.repaint();
                }
                // 没有赢，则继续，换另一个玩家下棋
                flag = flag % 2 + 1;
            }
        });
    }

    // 判断输赢
    private static boolean isWin(int x, int y) {
        // 如何判断赢了
        // 以自身为原点，向上，向下，向左，向右，都扩4列，最极限的情况下，我们下的位置，是第五子
        // 先看横的
        int count = 0;
        for (int i = y - 4; i <= y + 4; i++) {
            // i 不能小于0 	i 不能大于等于12 	不能超过边界
            if (i < 0 || i >= qp[0].length) {
                continue;
            }
            // 判断，是否有连的5个值
            if (qp[x][i] == flag) {
                count ++;
                if (count >= 5) {
                    // 胜利
                    return true;
                }
            } else {
                // 中间制药有一个不相等，累积就清空，变成0
                count = 0;
            }
        }

        // 接着判断竖的
        for (int i = x - 4; i <= x + 4; i++) {
            // i 不能小于0 	i不能大于等于12 	不能超过边界
            if (i < 0 || i >= qp.length) {
                continue;
            }
            if (qp[i][y] == flag) {
                count ++;
                if (count >= 5) {
                    // 胜利
                    return true;
                }
            } else {
                // 中间只要有一个不相等， 累积就清空，变成0
                count = 0;
            }
        }

        // 二个对角，襒 	重点： x ， y 都要改变
        for (int i = x - 4, j = y - 4 ; i <= y + 4 && j <= y + 4; i++, j++) {
            if (i < 0 || i >= qp.length || j < 0 || j >= qp[0].length) {
                continue;
            }
            if (qp[i][j] == flag) {
                count ++;
                if (count >= 5) {
                    // 胜利
                    return true;
                }
            } else {
                // 中间只要有一个不相等， 累积就清空，变成0
                count = 0;
            }
        }

        count = 0;

        // 捺
        for (int i = x + 4, j = y - 4; i >= x - 4 && j <= y + 4; i--, j++) {
            if (i < 0 || i >= qp.length || j < 0 || j > qp[0].length) {
                continue;
            }
            if (qp[i][j] == flag) {
                count ++;
                if (count >= 5) {
                    // 胜利
                    return true;
                }
            } else {
                // 中间只要有一个不相等，累积就清空，变成0
                count = 0;
            }
        }

        return false;
    }

    // 继承JFrame，那么，在创建的时候，会自动的调用这个方法
    public void paint (Graphics g) {
        // 定义背景图片
        Image bgImage = Toolkit.getDefaultToolkit().
                getImage(Gobang.class.getResource("/image/chessboard.jpg"));
        // 画图
        g.drawImage(bgImage, 4, 32, 760,760, this);
        g.dispose();
    }

}
