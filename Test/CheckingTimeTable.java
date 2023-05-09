package Test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CheckingTimeTable {
	CheckingTimeTable(Frame f, String username){
		f.removeAll();
		f.repaint();
		Font font = new Font("Times new roman", Font.PLAIN, 20);
		Label l1 = new Label("Select Class and Section to check TimeTable");
		l1.setFont(new Font("Arial", Font.BOLD, 20));
	    l1.setAlignment(Label.CENTER);  
	    l1.setBounds(50,50,500,40);
	    f.add(l1);
		Button b1 = new Button("Back");
        b1.setFont(font);
        b1.setBounds(200,400,100,30);
        f.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Admin_after_login(f,username);
            }
        });
        Button b2 = new Button("Check TimeTable");
        b2.setFont(font);
        b2.setBounds(200,350,200,30);
        f.add(b2);
        Label l2 = new Label("Select Class : ");
		l2.setFont(font); 
	    l2.setBounds(100,200,150,40);
	    f.add(l2);
	    Label l3 = new Label("Select Section : ");
		l3.setFont(font); 
	    l3.setBounds(100,300,150,40);
	    f.add(l3);
        Choice c1 = new Choice();
        c1.add("--select--");
        addItemsInChoiceOfClasses(c1);
        c1.setBounds(300,210,100,70);
        f.add(c1);
        Choice c2 = new Choice();
        c2.add("--select--");
        c2.setBounds(300,310,100,70);
        f.add(c2);
        c1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedClass = c1.getSelectedItem();
                if(!selectedClass.equals("--select--")) {
                    c2.removeAll();
                    c2.add("--select--");
                    addItemsInChoiceOfSections(c2, selectedClass);
                }
            }
        });
        String selectedClass = c1.getSelectedItem();
        c2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedSection = c2.getSelectedItem();
                if(selectedSection.equals("--select--")) {
                	JOptionPane.showMessageDialog(f, "Select a section to check attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        if(selectedClass.equals("--select--")) {
            JOptionPane.showMessageDialog(f, "Select a Class to check attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            addItemsInChoiceOfSections(c2, selectedClass);
        }
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String selectedClass = c1.getSelectedItem();
            	String selectedSection = c2.getSelectedItem();
            	new Checking_TimeTable(selectedClass, selectedSection, f);
            }
        });
	}
	CheckingTimeTable(Frame f, String username, String subject, int id) {
		f.removeAll();
		f.repaint();
		Font font = new Font("Times new roman", Font.PLAIN, 20);
		Label l1 = new Label("Select Class and Section to check TimeTable");
		l1.setFont(new Font("Arial", Font.BOLD, 20));
	    l1.setAlignment(Label.CENTER);  
	    l1.setBounds(50,50,500,40);
	    f.add(l1);
		Button b1 = new Button("Back");
        b1.setFont(font);
        b1.setBounds(200,400,100,30);
        f.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Faculty_after_login(f,username, subject, id);
            }
        });
        Button b2 = new Button("Check TimeTable");
        b2.setFont(font);
        b2.setBounds(200,350,200,30);
        f.add(b2);
        Label l2 = new Label("Select Class : ");
		l2.setFont(font); 
	    l2.setBounds(100,200,150,40);
	    f.add(l2);
	    Label l3 = new Label("Select Section : ");
		l3.setFont(font); 
	    l3.setBounds(100,300,150,40);
	    f.add(l3);
        Choice c1 = new Choice();
        c1.add("--select--");
        addItemsInChoiceOfClasses(c1);
        c1.setBounds(300,210,100,70);
        f.add(c1);
        Choice c2 = new Choice();
        c2.add("--select--");
        c2.setBounds(300,310,100,70);
        f.add(c2);
        c1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedClass = c1.getSelectedItem();
                if(!selectedClass.equals("--select--")) {
                    c2.removeAll();
                    c2.add("--select--");
                    addItemsInChoiceOfSections(c2, selectedClass);
                }
            }
        });
        String selectedClass = c1.getSelectedItem();
        c2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedSection = c2.getSelectedItem();
                if(selectedSection.equals("--select--")) {
                	JOptionPane.showMessageDialog(f, "Select a section to check attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        if(selectedClass.equals("--select--")) {
            JOptionPane.showMessageDialog(f, "Select a Class to check attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            addItemsInChoiceOfSections(c2, selectedClass);
        }
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String selectedClass = c1.getSelectedItem();
            	String selectedSection = c2.getSelectedItem();
            	new Checking_TimeTable(selectedClass, selectedSection, f);
            }
        });
	}
	void addItemsInChoiceOfClasses(Choice c) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams","root","@LEELadhar2719");
			PreparedStatement p = con.prepareStatement("select distinct class from students");
			ResultSet r = p.executeQuery();
			while(r.next()) {
				c.add(r.getString("class"));
			}
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	void addItemsInChoiceOfSections(Choice c,String cl) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams","root","@LEELadhar2719");
			PreparedStatement p = con.prepareStatement("select distinct section from students where class = ?;");
			p.setString(1, cl);
			ResultSet r = p.executeQuery();
			while(r.next()) {
				c.add(r.getString("section"));
			}
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
class Checking_TimeTable extends JFrame{
	Checking_TimeTable(String selectedClass, String selectedSection, Frame f){
		setTitle("Attendance Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String[] days = getDays(selectedClass, selectedSection);
        Object[][] time = getTime(selectedClass, selectedSection, days);
        JTable table = new JTable(time, days);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(backButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
		setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	String[] getDays(String selectedClass, String selectedSection) {
		String[] days = null;
		Connection con;
		int cnt = 0;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams","root","@LEELadhar2719");
			PreparedStatement p = con.prepareStatement("select distinct day_of_week from timetable where class_name = ? and section_name = ?;");
			p.setString(1, selectedClass);
			p.setString(2, selectedSection);
			ResultSet r = p.executeQuery();
			while(r.next()) {
				cnt++;
			}
			days = new String[cnt+2];
			r = p.executeQuery();
			days[0] = "Start Time";
			days[1] = "End Time";
			cnt = 2;
			while(r.next()) {
				days[cnt++] = r.getString("day_of_week");
			}
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return days;
	}
	Object[][] getTime(String selectedClass, String selectedSection, String[] days) {
		Object[][] time = null;
		Connection con;
		int cnt = 0;
		try {
			int i = 2, j;
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams","root","@LEELadhar2719");
			PreparedStatement p = con.prepareStatement("select distinct time_slot_start, time_slot_end from timetable where class_name = ? and section_name = ?;");
			p.setString(1, selectedClass);
			p.setString(2, selectedSection);
			ResultSet r = p.executeQuery();
			while(r.next()) {
				cnt++;
			}
			time = new Object[cnt][days.length];
			r = p.executeQuery();
			cnt = 0;
			while(r.next()) {
				i = 2;
				j = 2;
				time[cnt][0] = r.getString("time_slot_start");
				time[cnt][1] = r.getString("time_slot_end");
				PreparedStatement pr = con.prepareStatement("select subject from timetable where class_name = ? and section_name = ? and day_of_week = ?  and time_slot_start  = ? and time_slot_end = ?;");
				pr.setString(1, selectedClass);
				pr.setString(2, selectedSection);
				pr.setString(4, time[cnt][0].toString());
				pr.setString(5, time[cnt][1].toString());
				while(i < days.length) {
					pr.setString(3, days[i]);
					ResultSet rs = pr.executeQuery();
					if(rs.next()) time[cnt][j] = rs.getString("subject"); 
					j++;
					i++;
				}
				cnt++;
			}
			con.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return time;
	}
}
