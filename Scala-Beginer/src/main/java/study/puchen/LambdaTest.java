package study.puchen;

import javax.swing.*;

/**
 * @program: scalatest
 * @description: 测试Lambda
 * @author: Puchen
 * @create: 2019-07-29 10:14
 */
public class LambdaTest  extends JFrame {
    private JButton jb;
    public LambdaTest(){
        this.setBounds(200,200,400,200);
        this.setTitle("LAMBDA测试");

        jb = new JButton("Click Here");
//        jb.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("clicked!!");
//            }
//        });

        jb.addActionListener(s -> System.out.println("clicked again!!"));

        this.add(jb);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new LambdaTest();
    }

}
