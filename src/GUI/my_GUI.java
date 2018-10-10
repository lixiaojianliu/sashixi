package GUI;
import test.mns;
import test.mns.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class my_GUI {
    private JTextField textField1;
    private String log;
    public my_GUI(){
        创建队列Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test.mns tempQu=new mns();
                String Qname=textField1.getText();
                if(Qname.equals(""))
                {
                    log="错误：队列名不能为空!";
                    textArea2.append(log+"\n");
                }
                else {
                    log = tempQu.creatQuene(Qname);
                    textArea2.append(log + "\n");
                }
            }
        });
        删除队列Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test.mns tempQu=new mns();
                String Qname=textField1.getText();
                if(Qname.equals(""))
                {
                    log="错误：队列名不能为空!";
                    textArea2.append(log+"\n");
                }
                else {
                    log = tempQu.deleteQuene(Qname);
                    textArea2.append(log + "\n");
                }
            }
        });
        接收Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test.mns tempQu=new mns();
                String Qname=textField1.getText();
                if(Qname.equals(""))
                {
                    log="错误：队列名不能为空!";
                    textArea2.append(log+"\n");
                }
                else
                {
                    log=tempQu.receiveMessage(Qname);
                    textArea2.append(log+"\n");
                }
            }
        });
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.setText("");
            }
        });
        发送Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test.mns tempQu=new mns();
                String Qname=textField1.getText();
                String Qbody=textArea1.getText();
                if(Qname.equals(""))
                {
                    log="错误：队列名不能为空!";
                    textArea2.append(log+"\n");
                }
                else{
                    log=tempQu.sendMessage(Qname,Qbody);
                    textArea2.append(log+"\n");
                }
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("my_GUI");
        frame.setContentPane(new my_GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 400);
        frame.setLocation(300, 200);
        frame.setVisible(true);
    }

    private JButton 创建队列Button;
    private JButton 删除队列Button;
    private JButton 接收Button;
    private JButton 发送Button;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JPanel panel;
    private JButton cleanButton;
}
