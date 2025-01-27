import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ToDoListGUI {
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private ArrayList<Task> tasks;

    public ToDoListGUI() {
        tasks = new ArrayList<>();

        // Frame setup
        frame = new JFrame("ToDo List - Task Manager");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Title Label
        JLabel titleLabel = new JLabel("ToDo List Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 63, 65));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Task List Panel
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        taskList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taskList.setBackground(Color.WHITE);
        taskList.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Filter Buttons Panel (Above Task List)
        JPanel filterPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        JButton viewCompletedButton = createStyledButton("View Completed", new Color(52, 152, 219));
        JButton viewOngoingButton = createStyledButton("View Ongoing", new Color(241, 196, 15));
        filterPanel.add(viewCompletedButton);
        filterPanel.add(viewOngoingButton);
        frame.add(filterPanel, BorderLayout.NORTH);

        // Action Buttons Panel (Below Task List)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = createStyledButton("Add Task", new Color(46, 204, 113));
        JButton markCompletedButton = createStyledButton("Mark Completed", new Color(70, 130, 180));
        JButton removeButton = createStyledButton("Remove Task", new Color(231, 76, 60));
        JButton resetButton = createStyledButton("Reset Filters", new Color(155, 89, 182));

        buttonPanel.add(addButton);
        buttonPanel.add(markCompletedButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event Handlers
        addButton.addActionListener(e -> addTask());
        markCompletedButton.addActionListener(e -> markTaskCompleted());
        viewCompletedButton.addActionListener(e -> filterTasks(true));
        viewOngoingButton.addActionListener(e -> filterTasks(false));
        removeButton.addActionListener(e -> removeTask());
        resetButton.addActionListener(e -> updateTaskList(tasks)); // Reset to show all tasks

        // Display the frame
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return button;
    }

    private void addTask() {
        String taskDescription = JOptionPane.showInputDialog(frame, "Enter task description:");
        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            tasks.add(new Task(taskDescription));
            updateTaskList(tasks);
        } else {
            JOptionPane.showMessageDialog(frame, "Task description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskCompleted() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.get(selectedIndex).setCompleted(true);
            updateTaskList(tasks);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to mark as completed.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            updateTaskList(tasks);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void filterTasks(boolean showCompleted) {
          ArrayList<Task> filteredTasks = tasks.stream()
                .filter(task -> task.isCompleted() == showCompleted)
                .collect(Collectors.toCollection(ArrayList::new));
        updateTaskList(filteredTasks);
    }

    private void updateTaskList(ArrayList<Task> taskListToDisplay) {
        taskListModel.clear();
        for (int i = 0; i < taskListToDisplay.size(); i++) {
            Task task = taskListToDisplay.get(i);
            String status = task.isCompleted() ? "[✔]" : "[ ]";
            taskListModel.addElement((i + 1) + ". " + status + " " + task.getDescription());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListGUI::new);
    }

    // Task class to represent a task
    class Task {
        private String description;
        private boolean completed;

        public Task(String description) {
            this.description = description;
            this.completed = false;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
