package com.example.integracje5;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public class MyFrame extends JFrame {
    private final SpaceXService spaceXService;
    private final JTable table;
    private final JTextField idField;
    private final JDateChooser startDatePicker = new JDateChooser();
    private final JDateChooser endDatePicker = new JDateChooser();
    private final JTextField totalCountField = new JTextField(10);
    private final JTextField successCountField = new JTextField(10);
    private final JTextField failureCountField = new JTextField(10);

    JButton filterButton = new JButton("Filter by Date");

    public MyFrame(SpaceXService service) {
        this.spaceXService = service;
        setTitle("SpaceX Launch Data");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        filterButton.addActionListener(e -> filterByDate(startDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                endDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> loadData());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        idField = new JTextField(20);
        JButton searchButton = new JButton("Search by ID");
        searchButton.addActionListener(e -> searchById());

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(loadButton);
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(searchButton);

        panel.add(new JLabel("Start Date:"));
        panel.add(startDatePicker);
        panel.add(new JLabel("End Date:"));
        panel.add(endDatePicker);
        panel.add(filterButton);

        panel.add(new JLabel("Total Count:"));
        panel.add(totalCountField);
        panel.add(new JLabel("Success Count:"));
        panel.add(successCountField);
        panel.add(new JLabel("Failure Count:"));
        panel.add(failureCountField);

        totalCountField.setEditable(false);
        successCountField.setEditable(false);
        failureCountField.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void loadData() {
        Launch[] launches = spaceXService.getLaunches();
        Arrays.sort(launches, (o1, o2) -> o2.getDate_utc().compareTo(o1.getDate_utc()));

        String[] columnNames = {"ID", "Name", "Details", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int totalCount = launches.length;
        int successCount = 0;
        int failureCount = 0;

        for (Launch launch : launches) {
            String id = launch.getId();
            String name = launch.getName();
            String details = launch.getDetails();
            LocalDateTime date = launch.getDate_utc();
            boolean success = launch.getSuccess() != null && launch.getSuccess();

            Object[] rowData = {id, name, details, date};
            model.addRow(rowData);

            if (success) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        table.setModel(model);
        totalCountField.setText(String.valueOf(totalCount));
        successCountField.setText(String.valueOf(successCount));
        failureCountField.setText(String.valueOf(failureCount));
    }

    private void searchById() {
        String id = idField.getText();
        Launch launch = spaceXService.getLaunchById(id);

        String[] columnNames = {"ID", "Name", "Details", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (launch != null) {
            String launchId = launch.getId();
            String name = launch.getName();
            String details = launch.getDetails();
            LocalDateTime date = launch.getDate_utc();
            boolean success = launch.getSuccess() != null && launch.getSuccess();

            Object[] rowData = {launchId, name, details, date};
            model.addRow(rowData);

            int totalCount = 1;
            int successCount = success ? 1 : 0;
            int failureCount = success ? 0 : 1;

            totalCountField.setText(String.valueOf(totalCount));
            successCountField.setText(String.valueOf(successCount));
            failureCountField.setText(String.valueOf(failureCount));
        }

        table.setModel(model);
    }

    private void filterByDate(LocalDate startDate, LocalDate endDate) {
        Launch[] launches = spaceXService.getLaunches();

        Launch[] filteredLaunches = Arrays.stream(launches)
                .filter(launch -> {
                    LocalDate launchDate = launch.getDate_utc().toLocalDate();
                    return !launchDate.isBefore(startDate) && !launchDate.isAfter(endDate);
                })
                .toArray(Launch[]::new);

        String[] columnNames = {"ID", "Name", "Details", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int totalCount = filteredLaunches.length;
        int successCount = 0;
        int failureCount = 0;

        for (Launch launch : filteredLaunches) {
            String id = launch.getId();
            String name = launch.getName();
            String details = launch.getDetails();
            LocalDateTime date = launch.getDate_utc();
            boolean success = launch.getSuccess() != null && launch.getSuccess();

            Object[] rowData = {id, name, details, date};
            model.addRow(rowData);

            if (success) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        table.setModel(model);
        totalCountField.setText(String.valueOf(totalCount));
        successCountField.setText(String.valueOf(successCount));
        failureCountField.setText(String.valueOf(failureCount));
    }
}
