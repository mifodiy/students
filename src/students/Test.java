package students;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Test extends JFrame implements ListSelectionListener, ActionListener
{
    private JList list;

    public Test(){
        list = new JList();

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel dlm = new DefaultListModel();
        list.setModel(dlm);

        list.addListSelectionListener(this);

        JButton add = new JButton("Add");
        JButton del = new JButton("Del");

        add.addActionListener(this);
        del.addActionListener(this);

        add.setName("add");
        del.setName("del");

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,2));
        p.add(add);
        p.add(del);

        getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);

        getContentPane().add(p, BorderLayout.SOUTH);

        setBounds(100, 100, 200, 200);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Test t = new Test();
                t.setDefaultCloseOperation(EXIT_ON_CLOSE);
                t.setVisible(true);
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            System.out.println("Index: " + list.getSelectedIndex());
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        DefaultListModel dlm = (DefaultListModel) list.getModel();

        JButton sender = (JButton) actionEvent.getSource();
        if (sender.getName().equals("add")) {
            dlm.addElement(String.valueOf(dlm.getSize()));
        }
        if (sender.getName().equals("del") && list.getSelectedIndex() >= 0){
            dlm.remove(list.getSelectedIndex());
        }
    }
}
