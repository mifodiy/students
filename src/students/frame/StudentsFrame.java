package students.frame;

import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Vector;

public class StudentsFrame extends JFrame
{
    ManagementSystem ms = null;
    private JList grpList;
    private JList stdList;
    private JSpinner spYear;

    public StudentsFrame() throws Exception{
        getContentPane().setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        top.add(new JLabel("Год обучения:"));

        SpinnerModel sm = new SpinnerNumberModel(2006, 1990, 2100, 1);
        spYear = new JSpinner(sm);
        top.add(spYear);

        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.RAISED));

        Vector gr = null;
        Vector st = null;

        ms = ManagementSystem.getInstance();

        gr = new Vector<Group>(ms.getGroups());

        st = new Vector<Student>(ms.getAllStudents());

        left.add(new JLabel("Группы"), BorderLayout.NORTH);
        grpList = new JList(gr);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.RAISED));

        right.add(new JLabel("Студенты:"), BorderLayout.NORTH);
        stdList = new JList(st);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);

        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        setBounds(100, 100, 600, 400);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentsFrame sf = null;
                try {
                    sf = new StudentsFrame();
                    sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    sf.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
