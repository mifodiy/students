package students.frame;

import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

public class StudentsFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener
{
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_ST = "insertStudent";
    private static final String UPDATE_ST = "updateStudent";
    private static final String DELETE_ST = "deleteStudent";
    private static final String ALL_STUDENTS = "allStudent";

    private ManagementSystem ms = null;
    private JList grpList;
    private JTable stdList;
    private JSpinner spYear;

    public StudentsFrame() throws Exception{
        getContentPane().setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Отчеты");
        JMenuItem menuItem = new JMenuItem("Все студенты");
        menuItem.setName(ALL_STUDENTS);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        top.add(new JLabel("Год обучения:"));

        SpinnerModel sm = new SpinnerNumberModel(2006, 1990, 2100, 1);
        spYear = new JSpinner(sm);
        spYear.addChangeListener(this);
        top.add(spYear);

        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        GroupPanel left = new GroupPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        Vector gr = null;
        Vector st = null;

        ms = ManagementSystem.getInstance();

        gr = new Vector<Group>(ms.getGroups());

        st = new Vector<Student>(ms.getAllStudents());

        left.add(new JLabel("Группы"), BorderLayout.NORTH);
        grpList = new JList(gr);
        grpList.addListSelectionListener(this);
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);

        JButton btnMvGr = new JButton("Переместить");
        btnMvGr.setName(MOVE_GR);
        JButton btnClGr = new JButton("Очистить");
        btnClGr.setName(CLEAR_GR);
        btnMvGr.addActionListener(this);
        btnClGr.addActionListener(this);

        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1,2));
        pnlBtnGr.add(btnMvGr);
        pnlBtnGr.add(btnClGr);
        left.add(pnlBtnGr,BorderLayout.SOUTH);

        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));

        right.add(new JLabel("Студенты:"), BorderLayout.NORTH);
        stdList = new JTable(1,4);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);

        JButton btnAddSt = new JButton("Добавить");
        btnAddSt.setName(INSERT_ST);
        btnAddSt.addActionListener(this);
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_ST);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_ST);
        btnDelSt.addActionListener(this);

        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1,3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        setBounds(100, 100, 700, 500);
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
                    sf.reloadStudents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Component){
            Component c = (Component) actionEvent.getSource();
            if (c.getName().equals(MOVE_GR)){
                moveGroup();
            }
            if (c.getName().equals(CLEAR_GR)){
                clearGroup();
            }
            if (c.getName().equals(ALL_STUDENTS)){
                showAllStudents();
            }
            if (c.getName().equals(INSERT_ST)){
                insertStudent();
            }
            if (c.getName().equals(UPDATE_ST)){
                updateStudent();
            }
            if (c.getName().equals(DELETE_ST)){
                deleteStudent();
            }
        }

    }

    private void deleteStudent() {
        Thread t = new Thread(){
            public void run(){
                if (stdList != null){
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0){
                        if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                                "Вы хотите удалить студента?", "Удаление студента",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            Student s = stm.getStudent(stdList.getSelectedRow());
                            try{
                                ms.deleteStudent(s);
                                reloadStudents();
                            }catch (SQLException e){
                                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                            }
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(StudentsFrame.this,
                                "Необходимо выделить студента в списке");
                    }
                }
            }
        };
        t.start();
    }

    private void updateStudent() {
        Thread t = new Thread(){
            public void run(){
                if (stdList != null){
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0){
                        Student s = stm.getStudent(stdList.getSelectedRow());
                        try{
                            StudentDialog std = new StudentDialog(ms.getGroups(), false, StudentsFrame.this);
                            std.setStudent(s);
                            std.setModal(true);
                            std.setVisible(true);
                            if (std.getResult()) {
                                ms.updateStudent(std.getStudent());
                                reloadStudents();
                            }
                        }catch (Exception e){
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                }

            }
        };
        t.start();
    }

    private void insertStudent() {
        Thread t = new Thread(){
            public void run(){
                try{
                    StudentDialog std = new StudentDialog(ms.getGroups(), true, StudentsFrame.this);
                    std.setModal(true);
                    std.setVisible(true);
                    if (std.getResult()) {
                        ms.insertStudent(std.getStudent());
                        reloadStudents();
                    }
                }catch (Exception e){
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    private void showAllStudents() {
        JOptionPane.showMessageDialog(this, "showAllStudents");
    }

    private void clearGroup() {
        Thread t = new Thread(){
            public void run(){
                    if (grpList.getSelectedValue() != null){
                        if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                                "Вы хотите удалить студентов из группы?", "Удаление студентов",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            Group g = (Group) grpList.getSelectedValue();
                            int y = ((SpinnerNumberModel)spYear.getModel()).getNumber().intValue();
                            try{
                                ms.removeStudentsFromGroup(g,y);
                                reloadStudents();
                            }catch (SQLException e){
                                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                            }
                        }
                    }
                }
        };
        t.start();
    }

    private void moveGroup() {
        Thread t = new Thread(){
            public void run(){
                if (grpList.getSelectedValue() == null) {
                    return;
                }
                try{
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel)spYear.getModel()).getNumber().intValue();
                    GroupDialog gd = new GroupDialog(y, ms.getGroups());
                    gd.setModal(true);
                    gd.setVisible(true);
                    if (gd.getResult()) {
                        ms.moveStudentsToGroup(g, y, gd.getGroup(), gd.getYear());
                        reloadStudents();
                    }
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        reloadStudents();
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()){
            reloadStudents();
        }
    }

    public void reloadStudents() {
        Thread t = new Thread(){
            public void run(){
                if (stdList != null){
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try{
                        Collection<Student> s = ms.getStudentsFromGroup(g, y);
                        stdList.setModel(new StudentTableModel(new Vector<Student>(s)));
                    }catch (SQLException e){
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                }
            }
        };
        t.start();
    }

    class GroupPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250,0);
        }
    }
}
