package com.ruuuuuuuby.game;
import com.ruuuuuuuby.domain.Poker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class GameJFrame extends JFrame implements ActionListener {

    //container
    public Container container = null;

    //wanna be landlord or not button
    JButton landlord[] = new JButton[2];

    //publish cards or not
    JButton publishCard[] = new JButton[2];

    int dizhuFlag;
    int turn;

    //the label
    JLabel dizhu;


    //big arraylist which included three little arraylist
    //
    //pokers in arraylist
    //0 index：left player
    //1 index：middle player
    //2 index：right player
    ArrayList<ArrayList<Poker>> currentList = new ArrayList<>();


    ArrayList<ArrayList<Poker>> playerList = new ArrayList<>();

    //lord
    ArrayList<Poker> lordList = new ArrayList<>();

    //all cards
    ArrayList<Poker> pokerList = new ArrayList();

    //3 text fileds in front of 3 players
    JTextField time[] = new JTextField[3];

    //multi thread
    PlayerOperation po;

    boolean nextPlayer = false;

    public GameJFrame() {
        //set icon
        setIconImage(Toolkit.getDefaultToolkit().getImage("image/dizhu.png"));
        //set jFrame
        initJframe();
        //add
        initView();
        //view firstly and then deal cards(including cards movement)
        this.setVisible(true);
        //initialization
        new Thread(this::initCard).start();


        //preparations
        //rob buttons and 3 lists for 3 players
        initGame();

    }


    //prepare,shuffle,deal
    public void initCard() {
        //put all the cards into pokerlist
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 13; j++) {
                if ((i == 5) && (j > 2)) {
                    break;
                } else {
                    Poker poker = new Poker(this, i + "-" + j, false);
                    poker.setLocation(350, 150);

                    pokerList.add(poker);
                    container.add(poker);
                }
            }
        }

        //shuffle
        Collections.shuffle(pokerList);

        //3 players
        ArrayList<Poker> player0 = new ArrayList<>();
        ArrayList<Poker> player1 = new ArrayList<>();
        ArrayList<Poker> player2 = new ArrayList<>();

        for (int i = 0; i < pokerList.size(); i++) {
            //obtain every cards
            Poker poker = pokerList.get(i);

            //lord cards
            if (i <= 2) {
                //move
                Common.move(poker, poker.getLocation(), new Point(270 + (75 * i), 10));
                //add to list
                lordList.add(poker);
                continue;
            }
            //deal cards to 3 players
            if (i % 3 == 0) {
                //left
                Common.move(poker, poker.getLocation(), new Point(50, 60 + i * 5));
                player0.add(poker);
            } else if (i % 3 == 1) {
                //middle myself
                Common.move(poker, poker.getLocation(), new Point(180 + i * 7, 450));
                player1.add(poker);
                //my poker front
                poker.turnFront();

            } else if (i % 3 == 2) {
                //right
                Common.move(poker, poker.getLocation(), new Point(700, 60 + i * 5));
                player2.add(poker);
            }
            //bigger list including 3 small ones
            playerList.add(player0);
            playerList.add(player1);
            playerList.add(player2);

            //put the current one into the top to make the effect
            container.setComponentZOrder(poker, 0);

        }

        //sort
        for (int i = 0; i < 3; i++) {
            //order
            Common.order(playerList.get(i));
            //reorder
            Common.rePosition(this, playerList.get(i), i);
        }





    }

    //prepare work
    private void initGame() {
        //three lists
        for (int i = 0; i < 3; i++) {
            ArrayList<Poker> list = new ArrayList<>();
            //big lists
            currentList.add(list);
        }

        //wanna be lord or not button
        landlord[0].setVisible(true);
        landlord[1].setVisible(true);

        //countdown timer of me
        time[1].setVisible(true);
        //10 secs
        po = new PlayerOperation(this, 30);
        //start
        po.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == landlord[0]) {
            //landlord?
            time[1].setText("Rob");
            po.isRun = false;
        } else if (e.getSource() == landlord[1]) {
            //no?
            time[1].setText("No");
            po.isRun = false;
        } else if (e.getSource() == publishCard[1]) {
            //no
            this.nextPlayer = true;
            currentList.get(1).clear();
            time[1].setText("No");
        } else if (e.getSource() == publishCard[0]) {
            //publish

            //list for cards to publish
            ArrayList<Poker> c = new ArrayList<>();
            //all my cards
            ArrayList<Poker> player2 = playerList.get(1);

            //clicked and add the cards into the list
            for (int i = 0; i < player2.size(); i++) {
                Poker poker = player2.get(i);
                if (poker.isClicked()) {
                    c.add(poker);
                }
            }

            int flag = 0;
            //if both of them don't wanna publish
            if (time[0].getText().equals("Pass") && time[2].getText().equals("Pass")) {
                if (Common.jugdeType(c) != PokerType.c0){
                    flag = 1;
                }
            } else {
                flag = Common.checkCards(c, currentList, this);
            }

            if (flag == 1) {
                //put the ready to publish cards into the list
                currentList.set(1, c);
                //remove the cards have been published
                player2.removeAll(c);

                //calculate the position and move cards
                //to the top
                Point point = new Point();
                point.x = (770 / 2) - (c.size() + 1) * 15 / 2;
                point.y = 300;
                for (int i = 0, len = c.size(); i < len; i++) {
                    Poker poker = c.get(i);
                    Common.move(poker, poker.getLocation(), point);
                    point.x += 15;
                }

                //reposition the left cards
                Common.rePosition(this, player2, 1);
                //win hide the text
                time[1].setVisible(false);
                //next player's turn
                this.nextPlayer = true;
            }

        }
    }

    //add view
    public void initView() {
        //create rob button
        JButton robBut = new JButton("Rob");
        //set position
        robBut.setBounds(320, 400, 75, 20);
        //add click event
        robBut.addActionListener(this);
        //set invisible
        robBut.setVisible(false);
        //add to list
        landlord[0] = robBut;
        //add to container
        container.add(robBut);

        //create a no rob button
        JButton noBut = new JButton("No");
        //set position
        noBut.setBounds(420, 400, 75, 20);
        //add click event
        noBut.addActionListener(this);
        //invisible
        noBut.setVisible(false);
        //add to array
        landlord[1] = noBut;
        //add to container
        container.add(noBut);

        //create a publish button
        JButton outCardBut = new JButton("Publish");
        outCardBut.setBounds(320, 400, 60, 20);
        outCardBut.addActionListener(this);
        outCardBut.setVisible(false);
        publishCard[0] = outCardBut;
        container.add(outCardBut);

        //create a no rob button
        JButton noCardBut = new JButton("No");
        noCardBut.setBounds(420, 400, 60, 20);
        noCardBut.addActionListener(this);
        noCardBut.setVisible(false);
        publishCard[1] = noCardBut;
        container.add(noCardBut);


        //countdown in front of 3 players

        //left 0
        //middle me 1
        //right2
        for (int i = 0; i < 3; i++) {
            time[i] = new JTextField("Countdown: ");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 80, 20);
        time[1].setBounds(355, 360, 110, 20);
        time[2].setBounds(620, 230, 80, 20);


        //创建地主图标
        dizhu = new JLabel(new ImageIcon("image/dizhu.png"));
        dizhu.setVisible(false);
        dizhu.setSize(40, 40);
        container.add(dizhu);



    }

    //设置界面
    public void initJframe() {
        //设置标题
        this.setTitle("Doudizhu");
        //设置大小
        this.setSize(830, 620);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口无法进行调节
        this.setResizable(false);
        //界面居中
        this.setLocationRelativeTo(null);
        //获取界面中的隐藏容器，以后直接用无需再次调用方法获取了
        container = this.getContentPane();
        //取消内部默认的居中放置
        container.setLayout(null);
        //设置背景颜色
        container.setBackground(Color.LIGHT_GRAY);
    }

}
