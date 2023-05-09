package Test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Locale;  
import javax.swing.JOptionPane;

public class Select_Class_and_Section {
	Select_Class_and_Section(Frame f,String username, String subject, int id){
		f.removeAll();
		f.repaint();
		Font font = new Font("Times new roman", Font.PLAIN, 20);
		Label l1 = new Label("Select Class and Section to mark Attendance");
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
                new Faculty_after_login(f,username,subject, id);
            }
        });
        Button b2 = new Button("Mark Attendance");
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
                	JOptionPane.showMessageDialog(f, "Select a section to mark attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        if(selectedClass.equals("--select--")) {
            JOptionPane.showMessageDialog(f, "Select a Class to mark attendance", "Message", JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            addItemsInChoiceOfSections(c2, selectedClass);
        }
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String selectedClass = c1.getSelectedItem();
            	String selectedSection = c2.getSelectedItem();
                if(isFollowingTimetable(selectedClass, selectedSection, id, f)) new Marking_Attendance(selectedClass,selectedSection,subject, f);
                else JOptionPane.showMessageDialog(f, "You didnot have a class with this section at this time", "Message", JOptionPane.INFORMATION_MESSAGE);
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
	public boolean isFollowingTimetable(String selectedClass, String selectedSection, int id, Frame f) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams","root","@LEELadhar2719");
			PreparedStatement p = con.prepareStatement("select time_slot_start,class_name,section_name from timetable where faculty_user_id = ?;");
			p.setInt(1, id);
			ResultSet r = p.executeQuery();
			if(r.next()) {
			int startTime = Integer.parseInt(r.getString("time_slot_start").substring(0, 2));
			String className = r.getString("class_name");
			String sectionName = r.getString("section_name");
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
		    int strTime = Integer.parseInt(formatter.format(date).substring(10, 12));
		    if(strTime == startTime) {
		    	if(selectedClass.equals(className) && selectedSection.equals(sectionName)) return true;
		    	else return false;
		    }
		    else return false;
			}
			else JOptionPane.showMessageDialog(f, "Unknown error! Please try after some time", "Message", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
