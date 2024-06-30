package view;

import controller.FeedController;
import controller.RequestsController;
import domain.enums.AnimalState;
import domain.enums.RequestState;
import domain.enums.RequestType;
import domain.model.User;
import domain.serializeddata.UsersList;
import domain.model.Request;
import dtos.PostDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VolunteerWindow extends JFrame {
    private FeedController feedController;
    private RequestsController requestsController;
    private User user;

    public VolunteerWindow(User user) {
        // Set the title of the frame
        setTitle("Volunteer");
        feedController = new FeedController();
        requestsController = new RequestsController();
        this.user = user;
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame
        setSize(600, 600);

        // top panel with login and register buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        Color topPanelsColor = new Color(207, 198, 176, 98);
        topPanel.setBackground(topPanelsColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        Color buttonPanelColor = new Color(181, 171, 145);
        buttonPanel.setBackground(buttonPanelColor);

        // change all buttons' background after focusing/clicking on them
        UIManager.put("Button.select", buttonPanelColor);

        // "view profile" button with icon
        JButton viewProfileButton = new JButton();
        try {
            ImageIcon profileIcon = new ImageIcon("src/images/icons/profile_view.png"); // Specify the path to your PNG file
            Image img = profileIcon.getImage();
            Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Adjust the size as needed
            viewProfileButton.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            viewProfileButton.setText("View Profile");
        }
        viewProfileButton.setBackground(buttonPanelColor);  // Set the background color
        viewProfileButton.setBorder(null);
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.setFocusable(false);
        viewProfileButton.setBorder(new EmptyBorder(0, 0, 0, 10));
        viewProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        viewProfileButton.addActionListener(e -> {
            UserProfileDialog userProfileDialog = new UserProfileDialog(this, this.user);
            userProfileDialog.setVisible(true);
        });

        // adding the "view profile" and "volunteer request" buttons
        buttonPanel.add(viewProfileButton);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // title label
        JLabel titleLabel = new JLabel("Welcome, " + user.getName() + " " + user.getLastname() + "!", JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Add the top panel to the top of the frame
        add(topPanel, BorderLayout.NORTH);
        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        // Create tabs with panels
        JPanel pets = createTabPanel("Pets Feed");
        setUpPetsPanel(pets);

        JPanel requests = createTabPanel("Requests");
        setUpRequestsPanel(requests);
        JPanel panel3 = createTabPanel("This is the content of Tab 3");

        // Add panels to the tabbed pane
        tabbedPane.addTab("Animals", pets);
        tabbedPane.addTab("Requests", requests);
        tabbedPane.addTab("Tab 3", panel3);

        // Add the tabbed pane to the frame
        add(tabbedPane, BorderLayout.CENTER);


        tabbedPane.setBackgroundAt(0, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(1, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(2, new Color(202, 191, 168));

        // "Animal Shelter ©" label at the bottom
        JLabel bottomLabel = new JLabel("Animal Shelter ©", JLabel.CENTER);
        bottomLabel.setBorder(new EmptyBorder(5, 0, 8, 0));
        add(bottomLabel, BorderLayout.SOUTH);
        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Make the frame visible
        setVisible(true);
    }

    // Helper method to create a panel for each tab
    private JPanel createTabPanel(String labelText) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.setSize(450, 580);
        return panel;
    }

    private static void center(Component component) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = component.getSize().width;
        int h = component.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // set the new location for the component
        component.setLocation(x, y);
    }

    private void setUpPetsPanel(JPanel pets) {
        JPanel petPanel = new JPanel();
        petPanel.setLayout(new BoxLayout(petPanel, BoxLayout.Y_AXIS));
        Color petPanelColor = new Color(207, 198, 176, 234);

        // search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(207, 198, 176, 98));

        JTextField searchField = new JTextField("Search...", 15);
        JButton searchButton = new JButton("Search");
        searchButton.setFocusable(false);
        searchButton.setBackground(new Color(156, 148, 131, 255));  // Set the background color
        searchButton.setForeground(Color.WHITE);  // Set the text color
        searchButton.setFocusPainted(false);
        searchButton.setBorder(new EmptyBorder(2, 10, 5, 10));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        petPanel.add(searchPanel, BorderLayout.SOUTH);

        for (PostDTO post : feedController.getAllPostsWithAnimalsAndBreeds()) {
            JPanel petPostPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;

            // pet image
            JLabel petImageLabel;
            try {
                ImageIcon petImage = new ImageIcon(post.getPicture());
                Image img = petImage.getImage();

                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                petImageLabel = new JLabel(new ImageIcon(scaledImg));
            } catch (Exception e) {
                // image not found -> placeholder text
                petImageLabel = new JLabel("Picture not found");
                petImageLabel.setPreferredSize(new Dimension(150, 150));
            }

            petImageLabel.setPreferredSize(new Dimension(150, 150));
            petImageLabel.setHorizontalAlignment(JLabel.CENTER);
            petImageLabel.setVerticalAlignment(JLabel.CENTER);
            gbc.gridx = 0;
            petPostPanel.add(petImageLabel, gbc);

            // panel for pet info
            JPanel petInfoPanel = new JPanel();
            petInfoPanel.setLayout(new BoxLayout(petInfoPanel, BoxLayout.Y_AXIS));
            petInfoPanel.setBackground(petPanelColor);

            petInfoPanel.add(new JLabel(" "));
            petInfoPanel.add(new JLabel("Name: " + post.getName()));
            petInfoPanel.add(new JLabel("Breed: " + post.getBreed()));
            petInfoPanel.add(new JLabel("Color: " + post.getColor()));
            petInfoPanel.add(new JLabel("Date: " + post.getDate()));
            petInfoPanel.add(new JLabel(" "));

            JLabel adopted = new JLabel("Status: " + post.getStatus());
            switch (post.getStatus()) {
                case "Adopted" -> adopted.setForeground(new Color(67, 177, 26));
                case "Not adopted" -> adopted.setForeground(new Color(214, 116, 3));
                case "In foster care" -> adopted.setForeground(new Color(9, 120, 188));
                case "Under treatment" -> adopted.setForeground(new Color(221, 9, 9));
            }
            petInfoPanel.add(adopted);

            gbc.gridx = 1;
            petPostPanel.add(petInfoPanel, gbc);

            // "View" button
            JButton viewButton = new JButton("View");
            viewButton.setFocusable(false);
            viewButton.setBackground(new Color(163, 153, 131));  // Set the background color
            viewButton.setForeground(Color.WHITE);  // Set the text color
            viewButton.setFocusPainted(false);
            viewButton.setBorder(new EmptyBorder(5, 10, 5, 10));
            viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // set constraints for the view button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 15, 15, 15); // Adjust as needed for padding

            petPostPanel.add(viewButton, gbc);

            // create a line separator - separates pets
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 2px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            petPanel.add(lineSeparator, gbc);

            petPostPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            petPostPanel.setBackground(petPanelColor);

            petPanel.add(petPostPanel);
        }

        JScrollPane scrollPane = new JScrollPane(petPanel);

        // add the scrollable panel to the frame
        pets.setLayout(new BorderLayout());
        pets.add(scrollPane, BorderLayout.CENTER);
        center(pets);
        pets.setVisible(true);
    }

    private void setUpRequestsPanel(JPanel requests) {
        JPanel reqPanel = new JPanel();
        reqPanel.setLayout(new BoxLayout(reqPanel, BoxLayout.Y_AXIS));
        Color petPanelColor = new Color(207, 198, 176, 234);

        for (Request request : requestsController.getPendingForVolunteer(user)) {
            JPanel infoPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;

            // buttons panel
            JPanel buttonsPanel = createButtonPanel(request);
            gbc.gridx = 0;
            infoPanel.add(buttonsPanel, gbc);

            // panel for pet info
            JPanel reqInfoPanel = new JPanel();
            reqInfoPanel.setLayout(new BoxLayout(reqInfoPanel, BoxLayout.Y_AXIS));
            reqInfoPanel.setBackground(petPanelColor);

            JLabel type = new JLabel(request.getType().toString());
            switch (request.getType().toString()) {
                case "ADOPTION" -> type.setForeground(new Color(67, 177, 26));
                case "TEMPORARY CARE" -> type.setForeground(new Color(214, 116, 3));
                case "VOLUNTEERING" -> type.setForeground(new Color(9, 120, 188));
                case "ANIMAL REGISTRATION" -> type.setForeground(new Color(221, 9, 9));
                case "POST EDITING" -> type.setForeground(new Color(128, 0, 228));
            }
            reqInfoPanel.add(type);

            reqInfoPanel.add(new JLabel(" "));
            reqInfoPanel.add(new JLabel("User info: "));
            UsersList usersList = new UsersList();
            User reqUser = usersList.getInstance().getById(request.getUserId());
            reqInfoPanel.add(new JLabel("Name: " + reqUser.getName()));
            reqInfoPanel.add(new JLabel("Lastname: " + reqUser.getLastname()));
            reqInfoPanel.add(new JLabel("Email: " + reqUser.getEmail()));
            reqInfoPanel.add(new JLabel("Date of birth: " + reqUser.getBirthDate().toString()));
            reqInfoPanel.add(new JLabel("Status: " + reqUser.getUserState()));

            gbc.gridx = 1;
            infoPanel.add(reqInfoPanel, gbc);
            if (!request.getType().toString().equals("VOLUNTEERING")) {
                // "View" button
                JButton viewButton = new JButton("View");
                viewButton.setFocusable(false);
                viewButton.setBackground(new Color(163, 153, 131));  // Set the background color
                viewButton.setForeground(Color.WHITE);  // Set the text color
                viewButton.setFocusPainted(false);
                viewButton.setBorder(new EmptyBorder(5, 10, 5, 10));
                viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                viewButton.addActionListener(e -> {
                    if (request.getType() == RequestType.ADOPTION) {
                        AnimalDialog animalDialog = new AnimalDialog(this, feedController.getById(request.getPostId()));
                    } else {
                        // Handle approval for other types
                    }
                });
                // set constraints for the view button
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.weightx = 0.0;
                gbc.weighty = 0.0;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(15, 15, 15, 15); // Adjust as needed for padding

                infoPanel.add(viewButton, gbc);
            }

            // create a line separator - separates pets
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 2px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            reqPanel.add(lineSeparator, gbc);

            infoPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            infoPanel.setBackground(petPanelColor);

            reqPanel.add(infoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(reqPanel);

        // add the scrollable panel to the frame
        requests.setLayout(new BorderLayout());
        requests.add(scrollPane, BorderLayout.CENTER);
        center(requests);
        requests.setVisible(true);
    }

    private JPanel createButtonPanel(Request r) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set background color
        Color petPanelColor = new Color(207, 198, 176, 234);
        panel.setBackground(petPanelColor);

        // Common properties for buttons
        Dimension buttonSize = new Dimension(150, 40); // Fixed size for the buttons

        // Create the "Approve" button
        JButton approveButton = new JButton("Approve");
        approveButton.setBackground(new Color(67, 177, 26));
        approveButton.setForeground(Color.WHITE);
        approveButton.setPreferredSize(buttonSize);
        approveButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        approveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        approveButton.setToolTipText("Click to approve");

        // Add action listener to the "Approve" button
        approveButton.addActionListener(e -> {
            if (r.getType() == RequestType.VOLUNTEERING) {
                // Handle approval for volunteering
            } else {
                // Handle approval for other types
            }
            JOptionPane.showMessageDialog(panel, "You have approved!");
        });

        // Create the "Reject" button
        JButton rejectButton = new JButton("Reject");
        rejectButton.setBackground(new Color(221, 9, 9));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setPreferredSize(buttonSize);
        rejectButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        rejectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rejectButton.setToolTipText("Click to reject");

        // Add action listener to the "Reject" button
        rejectButton.addActionListener(e -> {
            if (r.getType() == RequestType.VOLUNTEERING) {
                // Handle rejection for volunteering
            } else {
                // Handle rejection for other types
            }
            JOptionPane.showMessageDialog(panel, "You have rejected!");
        });

        // Configure GridBagConstraints for buttons
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0); // Space around buttons

        // Center the buttons horizontally
        gbc.anchor = GridBagConstraints.CENTER;

        // Add "Approve" button to the panel
        gbc.gridy = 0;
        panel.add(approveButton, gbc);

        // Add "Reject" button to the panel
        gbc.gridy = 1;
        panel.add(rejectButton, gbc);

        return panel;
    }
}