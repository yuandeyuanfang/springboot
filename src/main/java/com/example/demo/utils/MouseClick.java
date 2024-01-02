package com.example.demo.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * 鼠标连点器
 */
public class MouseClick extends JFrame {

	/*****************
	 * 连点器
	 *
	 * ***/
	JTextField jt = new JTextField("30,370");
	JTextField jt2 = new JTextField("80,575");
	JButton jb = new JButton("点击区域1测试");
	JButton jb2 = new JButton("点击区域2测试");
	JTextField jgt1 = new JTextField("1000");
	JTextField jgt2 = new JTextField("5000");
	JButton jg1 = new JButton("点击区域1后间隔");
	JButton jg2 = new JButton("点击区域2后间隔");
	JTextField jt3 = new JTextField("1");
	JButton jb3 = new JButton("点击时间（分钟）");
	JButton jb1 = new JButton("开始");
	boolean boo;//启动，停止
	Robot b;

	public String type="咸鱼之王无限挑战-3次点击";
//	public String type="";


	public MouseClick() {
		setSize(420, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		try {
			b = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		jt.setBounds(10, 10, 150, 50);
		add(jt);

		jt2.setBounds(180, 10, 150, 50);
		add(jt2);

		jb.setBounds(10, 70, 150, 50);
		add(jb);

		jb2.setBounds(180, 70, 150, 50);
		add(jb2);

		//点击间隔
		jgt1.setBounds(10, 70+60, 150, 50);
		add(jgt1);

		jgt2.setBounds(180, 70+60, 150, 50);
		add(jgt2);

		jg1.setBounds(10, 70+60+60, 150, 50);
		add(jg1);

		jg2.setBounds(180, 70+60+60, 150, 50);
		add(jg2);

		//点击持续时间
		jb3.setBounds(10, 70+60+60+60, 150, 50);
		add(jb3);

		jt3.setBounds(180, 70+60+60+60, 150, 50);
		add(jt3);

		jb1.setBounds(10, 70+60+60+60+60, 100, 50);
		add(jb1);



		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = jt.getText();
				String[] rt = str.split(",");
				move(Integer.parseInt(rt[0]), Integer.parseInt(rt[1]));
			}
		});

		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = jt2.getText();
				String[] rt = str.split(",");
				move(Integer.parseInt(rt[0]), Integer.parseInt(rt[1]));
			}
		});



		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						String str = jt.getText();
						String[] rt = str.split(",");
						String str2 = jt2.getText();
						String[] rt2 = str2.split(",");
						int minute = Integer.parseInt(jt3.getText());
						int jg1 = Integer.parseInt(jgt1.getText());
						int jg2 = Integer.parseInt(jgt2.getText());

						long l = new Date().getTime() + 1000*60*minute;
//						try {
//							Thread.sleep(5000);
//						} catch (InterruptedException e) {
//							// TODO 自动生成的 catch 块
//							e.printStackTrace();
//						}
						while(new Date().getTime()<l) {
							move(Integer.parseInt(rt[0]), Integer.parseInt(rt[1]));
							pressmouseas();
							jb1.setText((l-new Date().getTime())/1000+"");
							try {
								Thread.sleep(jg1);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							move(Integer.parseInt(rt2[0]), Integer.parseInt(rt2[1]));
							pressmouseas();
							try {
								Thread.sleep(jg2);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}

							if("咸鱼之王无限挑战-3次点击".equals(type)){
								move(Integer.parseInt(rt2[0]), Integer.parseInt(rt2[1]));
								pressmouseas();
								try {
									Thread.sleep(jg2);
								} catch (InterruptedException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							}

						}
						jb1.setText("运行");
					}
				}).start();
			}
		});


	}

	//模拟鼠标点击
	public void pressmouseas() {
		b.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		b.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	//模拟鼠标移动
	public void move(int x,int y) {

		b.mouseMove(-1,-1);
		b.mouseMove(x,y);//循坏使移动鼠标准确
//		new Thread(new Runnable() {
//			public void run() {
////				try {
//////					Thread.sleep(1000);
////				} catch (InterruptedException e) {
////					// TODO 自动生成的 catch 块
////					e.printStackTrace();
////				}
//				System.out.println(x+"  "+y);
//				b.mouseMove(x,y);
//			}
//		}).start();;
	}


	public static void main(String[] args) {
		new MouseClick().setVisible(true);
	}

}

