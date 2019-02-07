import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener
{
    //定义一个二维数组用于接收从MainFrame传入的数组
    private int[][] block_Data;
    public MyKeyListener(int[][] block_Data)
    {
        this.block_Data = block_Data;
    }
    public void keyTyped(KeyEvent e)
    {
    }
    public void keyPressed(KeyEvent e)
    {
    }
    //有键被按下时的调用逻辑
    public void keyReleased(KeyEvent e)
    {
        //获取到键盘按键的code值（左上右下分别对应37、38、39、40）
        int keyCode = e.getKeyCode();
        if(keyCode >=37 && keyCode<=40)
        {
            //分别从四个方向处理4个按键时的逻辑，以下以向左移动为例来解释
            //若按下的是左键，并且当前界面能够向左移动，执行向左移动的方法
            if(keyCode == 37 && canMoveLeft())
            {
                //真正向左移动的方法
                MoveLeft();
                MainFrame.createOneRandomNumber();
            }
            else if(keyCode == 39 && canMoveRight())
            {
                MoveRight();
                MainFrame.createOneRandomNumber();
            }
            else if(keyCode == 38 && canMoveUp())
            {
                MoveUp();
                MainFrame.createOneRandomNumber();
            }
            else if(keyCode == 40 && canMoveDown())
            {
                MoveDown();
                MainFrame.createOneRandomNumber();
            }
            //刷新界面和当前得分和判断游戏是否结束
            MainFrame.upDateUI(block_Data);
            MainFrame.reFreshScore();
            isGameOver();
        }
    }
    private void isGameOver()
    {
        //胜利
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(block_Data[i][j] == 256)
                {
                    int result = JOptionPane.showConfirmDialog(null, "恭喜您，游戏胜利，再来一局？", "游戏结果",JOptionPane.YES_NO_OPTION);
                    if(result == 0)
                    {
                        MainFrame.reSetBlocks();
                        MainFrame.initData();
                    }
                    else
                    {
                        System.exit(0);
                    }
                    return;
                }
            }
        }
        //失败
        if(block_DataIsFull() && !canMove())
        {
            int result = JOptionPane.showConfirmDialog(null, "很遗憾您输了,再来一局？", "游戏结果", JOptionPane.YES_NO_OPTION);
            if(result == 0)
            {
                MainFrame.reSetBlocks();
                MainFrame.initData();
            }
            else
            {
                System.exit(0);
            }
            return;
        }
    }
    private boolean block_DataIsFull()
    {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(block_Data[i][j] == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean canMove()
    {
        return canMoveLeft() || canMoveUp() || canMoveUp() || canMoveDown();
    }
    private void MoveDown()
    {
        for(int i=0; i<4; i++)
        {
            for(int j=2; j>=0; j--)
            {
                if(isNeedMoveDown(i, j))
                {
                    int k;
                    for(k=j+1; k<4;)
                    {
                        if(block_Data[i][k] == 0)
                        {
                            k++;
                            continue;
                        }
                        if(block_Data[i][k] == block_Data[i][j])
                        {
                            block_Data[i][k] = 2 * block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                        else
                        {
                            block_Data[i][k-1] = block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                    }
                    if(k==4)
                    {
                        block_Data[i][k-1] = block_Data[i][j];
                        block_Data[i][j] = 0;
                    }
                }
            }
        }
    }
    private void MoveUp()
    {
        for(int i=0; i<4; i++)
        {
            for(int j=1; j<4; j++)
            {
                if(isNeedMoveUp(i,j))
                {
                    int k;
                    for(k=j-1; k>=0;)
                    {
                        if(block_Data[i][k] == 0)
                        {
                            k--;
                            continue;
                        }
                        if(block_Data[i][k] == block_Data[i][j])
                        {
                            block_Data[i][k] = 2 * block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                        else
                        {
                            block_Data[i][k+1] = block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                    }
                    if(k<0)
                    {
                        block_Data[i][k+1] = block_Data[i][j];
                        block_Data[i][j] = 0;
                    }
                }
            }
        }
    }
    private void MoveRight()
    {
        for(int j=0; j<4; j++)
        {
            for(int i=2; i>=0; i--)
            {
                if(isNeedMoveRight(i,j))
                {
                    int k;
                    for(k=i+1; k<4;)
                    {
                        if(block_Data[k][j] == 0)
                        {
                            k++;
                            continue;
                        }
                        if(block_Data[k][j] == block_Data[i][j])
                        {
                            block_Data[k][j] = 2 * block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                        else
                        {
                            block_Data[k-1][j] = block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                    }
                    if(k == 4)
                    {
                        block_Data[k-1][j] = block_Data[i][j];
                        block_Data[i][j] = 0;
                    }
                }
            }
        }

    }
    //真正向左移动的方法。处理思想是遍历除了最左边一列的所有块，判断该块是否需要移动，若需要则移动
    //若不需要，则继续遍历下一个元素块
    private void MoveLeft()
    {
        for(int j=0; j<4; j++)
        {
            for(int i=1; i<4; i++)
            {
                //先判断当前block_Data[i][j]是否需要移动,需要拿另一个变量k用来查找当前块需要
                //移动到的最终位置
                if(isNeedMoveLeft(i,j))
                {
                    int k;
                    for(k=i-1; k>=0;)
                    {
                        //向左查找，若为0则继续查找
                        if(block_Data[k][j] == 0)
                        {
                            k--;
                            continue;
                        }
                        //在左边找到相同的合并
                        if(block_Data[k][j] == block_Data[i][j])
                        {
                            block_Data[k][j] = 2 * block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                        //没有找到相同的移动到相对较左的位置
                        else
                        {
                            block_Data[k+1][j] = block_Data[i][j];
                            block_Data[i][j] = 0;
                            break;
                        }
                    }
                    //在左侧方向没有找到相同的，并且已经找到最左边，则移动当前块到最左边的位置
                    if(k<0)
                    {
                        block_Data[k+1][j] = block_Data[i][j];
                        block_Data[i][j] = 0;
                    }
                }
            }
        }
    }

    private boolean isNeedMoveRight(int i, int j)
    {
        if(block_Data[i][j]!=0&&(block_Data[i+1][j]==0||block_Data[i][j]==block_Data[i+1][j]))
        {
            return true;
        }
        return false;
    }
    private boolean isNeedMoveLeft(int i, int j)
    {
        if(block_Data[i][j]!=0&&(block_Data[i-1][j]==0||block_Data[i][j]==block_Data[i-1][j]))
        {
            return true;
        }
        return false;
    }
    private boolean isNeedMoveUp(int i, int j)
    {
        if(block_Data[i][j]!=0&&(block_Data[i][j-1]==0||block_Data[i][j]==block_Data[i][j-1]))
        {
            return true;
        }
        return false;
    }
    private boolean isNeedMoveDown(int i, int j)
    {
        if(block_Data[i][j]!=0&&(block_Data[i][j+1]==0||block_Data[i][j]==block_Data[i][j+1]))
        {
            return true;
        }
        return false;
    }
    //判断整个界面是否能够向左移动
    private boolean canMoveLeft()
    {
        //判断逻辑：遍历当前界面的块元素，如果当前块不是0 &&（当前块的左边块是0或当前块的左边块和当前块相同，则返回true）
        for(int i=1; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(block_Data[i][j]!=0&&(block_Data[i-1][j]==0||block_Data[i][j]==block_Data[i-1][j]))
                {
                    return true;
                }
            }
        }
        return false;
    }
    //判断整个界面是否能够向右移动
    private boolean canMoveRight()
    {
        for(int i=2; i>=0; i--)
        {
            for(int j=0; j<4; j++)
            {
                if(block_Data[i][j]!=0&&(block_Data[i+1][j]==0||block_Data[i][j]==block_Data[i+1][j]))
                {
                    return true;
                }
            }
        }
        return false;
    }
    //判断整个界面能否向上移动
    public boolean canMoveUp()
    {
        for(int j=1; j<4; j++)
        {
            for(int i=0; i<4; i++)
            {
                if(block_Data[i][j]!=0&&(block_Data[i][j-1]==0||block_Data[i][j]==block_Data[i][j-1]))
                {
                    return true;
                }
            }
        }
        return false;
    }
    //判断整个界面能否向下移动
    public boolean canMoveDown()
    {
        for(int j=2; j>=0; j--)
        {
            for(int i=0; i<4; i++)
            {
                if(block_Data[i][j]!=0&&(block_Data[i][j+1]==0||block_Data[i][j]==block_Data[i][j+1]))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
