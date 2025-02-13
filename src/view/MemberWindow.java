package view;

import controller.FeedController;
import controller.RequestsController;
import domain.enums.MessageOwner;
import domain.enums.RequestState;
import domain.enums.RequestType;
import domain.model.Message;
import domain.model.Request;
import domain.model.User;
import domain.serializeddata.MessagesList;
import domain.serializeddata.UsersList;
import dtos.PostDTO;
import util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MemberWindow extends JFrame {

    private User user;
    private FeedController feedController;
    private RequestsController requestsController;
    private ArrayList<PostDTO> posts;
    private JPanel inboxPanel;

    public MemberWindow(User user) {
        this.user = user;
        feedController = new FeedController();
        requestsController = new RequestsController();
        this.posts = feedController.getAllPostsWithAnimalsAndBreeds();
        inboxPanel = new JPanel();
        setWindowData();

        // top panel with login and register buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        Color topPanelsColor = new Color(207, 198, 176, 98);
        topPanel.setBackground(topPanelsColor);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        Color buttonPanelColor = new Color(181, 171, 145);
        buttonPanel.setBackground(buttonPanelColor);

        // Left panel for logout button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(buttonPanelColor);
        leftPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(92, 92, 92));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFocusable(false);
        logoutButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            LoginWindow loginWindow = new LoginWindow();
            this.dispose();
        });

        leftPanel.add(logoutButton);

        // Right panel for profile and volunteer request buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(buttonPanelColor);

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
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.setFocusable(false);
        viewProfileButton.setBorder(new EmptyBorder(0, 0, 0, 10));
        viewProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        viewProfileButton.addActionListener(e -> {
            UserProfileDialog userProfileDialog = new UserProfileDialog(this, this.user);
            userProfileDialog.setVisible(true);
        });

        // "send volunteer request" button with icon
        JButton volunteerRequestButton = new JButton();
        try {
            ImageIcon profileIcon = new ImageIcon("src/images/icons/send_volunteer_request.png"); // Specify the path to your PNG file
            Image img = profileIcon.getImage();
            Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Adjust the size as needed
            volunteerRequestButton.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            volunteerRequestButton.setText("Send Volunteer Request");
        }

        volunteerRequestButton.setBackground(buttonPanelColor);  // Set the background color
        volunteerRequestButton.setFocusPainted(false);
        volunteerRequestButton.setBorder(new EmptyBorder(0, 0, 0, 16));
        volunteerRequestButton.setFocusable(false);
        volunteerRequestButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rightPanel.add(viewProfileButton);
        rightPanel.add(volunteerRequestButton);

        // Adding left and right panels to the main button panel
        buttonPanel.add(leftPanel, BorderLayout.WEST);
        buttonPanel.add(rightPanel, BorderLayout.EAST);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Add buttonPanel to the topPanel or the main container as required
        topPanel.add(buttonPanel, BorderLayout.NORTH);

        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // title label
        JLabel titleLabel = new JLabel("Welcome, " + user.getName() + " " + user.getLastname() + "!", JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 14));

        topPanel.add(titleLabel, BorderLayout.CENTER);

        // create tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // first tab: Pet Panel
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

        JPanel requestsPanel = new JPanel();

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            posts.clear();
            petPanel.removeAll();

            if(searchField.getText().equals("")) {
                posts = feedController.getAllPostsWithAnimalsAndBreeds();
            }
            else {
                String[] tokens = searchField.getText().split(" ");

                for(String token : tokens) {
                    posts.addAll(feedController.getFilteredPosts(token, token, token, token));
                }
            }

            setPetsPanel(petPanel, petPanelColor, searchPanel, requestsPanel);
        });

        setPetsPanel(petPanel, petPanelColor, searchPanel, requestsPanel);

        JScrollPane scrollPane = new JScrollPane(petPanel);
        tabbedPane.addTab("Pets", scrollPane);

        // second tab: My posts
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

        JPanel addPostPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addPostButton = new JButton("Add post");

        addPostPanel.add(addPostButton);
        postsPanel.add(addPostPanel, BorderLayout.SOUTH);

        for (PostDTO post : feedController.getAllPostsWithAnimalsAndBreeds(user)) {
            JPanel petPostPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center the component
            gbc.insets = new Insets(0, 0, 0, 0); // Padding around the component
            gbc.fill = GridBagConstraints.NONE; // Do not resize the component

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

            // "Edit" button
            JButton editButton = new JButton("Edit");
            editButton.setFocusable(false);
            editButton.setBackground(new Color(163, 153, 131));  // Set the background color
            editButton.setForeground(Color.WHITE);  // Set the text color
            editButton.setFocusPainted(false);
            editButton.setBorder(new EmptyBorder(5, 10, 5, 10));
            editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            editButton.addActionListener(e -> {
                EditPostDialog editPostDialog = new EditPostDialog(this, user, feedController.getAnimalFromPost(post), post.getId());
                editPostDialog.setVisible(true);
                this.updateRequests(requestsPanel, petPanelColor);
            });

            // set constraints for the view button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 15, 15, 15); // Adjust as needed for padding

            petPostPanel.add(editButton, gbc);

            // create a line separator - separates pets
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 2px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            petPanel.add(lineSeparator, gbc);

            petPostPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            petPostPanel.setBackground(petPanelColor);

            postsPanel.add(petPostPanel);
        }

        tabbedPane.addTab("Posts", postsPanel);







        // second tab: My requests
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));

        updateRequests(requestsPanel, petPanelColor);

        JScrollPane scrollRequestsPanel = new JScrollPane(requestsPanel);

        tabbedPane.addTab("My requests", scrollRequestsPanel);

        volunteerRequestButton.addActionListener(e -> {
            VolunteeringRequestDialog volunteeringRequestDialog = new VolunteeringRequestDialog(this);
            volunteeringRequestDialog.setVisible(true);
            String reason = volunteeringRequestDialog.getText();

            if(reason != null) {
                // add text to a request?
                requestsController.requestToBeVolunteer(this.user, reason);
                this.updateRequests(requestsPanel, petPanelColor);
            }
        });

        addPostButton.addActionListener(e -> {
            CreatePostDialog createPostDialog = new CreatePostDialog(this, user);
            createPostDialog.setVisible(true);
            this.updateRequests(requestsPanel, petPanelColor);
        });











        // third tab: My Pets
        JPanel petsAdoptedPanel = new JPanel();
        petsAdoptedPanel.setLayout(new BoxLayout(petsAdoptedPanel, BoxLayout.Y_AXIS));

        for (PostDTO post : feedController.getAllPostsUserAdopted(user)) {
            JPanel petPostPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center the component
            gbc.insets = new Insets(0, 0, 0, 0); // Padding around the component
            gbc.fill = GridBagConstraints.NONE; // Do not resize the component

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

            // "Grade" button
            JButton gradeButton = new JButton("Grade");
            gradeButton.setFocusable(false);
            gradeButton.setBackground(new Color(163, 153, 131));  // Set the background color
            gradeButton.setForeground(Color.WHITE);  // Set the text color
            gradeButton.setFocusPainted(false);
            gradeButton.setBorder(new EmptyBorder(5, 10, 5, 10));
            gradeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            gradeButton.addActionListener(e -> {
                // to be added
            });

            // set constraints for the view button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 15, 15, 15); // Adjust as needed for padding

            petPostPanel.add(gradeButton, gbc);

            // create a line separator - separates pets
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 2px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            petPanel.add(lineSeparator, gbc);

            petPostPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            petPostPanel.setBackground(petPanelColor);

            petsAdoptedPanel.add(petPostPanel);
        }

        tabbedPane.addTab("My pets", petsAdoptedPanel);

        // fourth tab: Payments
        JPanel paymentsPanel = new JPanel();
        paymentsPanel.add(new JLabel("Payments will be displayed here."));
        tabbedPane.addTab("Payments", paymentsPanel);

        // fifth tab: Inbox
        //inboxPanel.add(new JLabel("Inbox will be displayed here."));
        setInboxPanel();
        JScrollPane scrollInboxPane = new JScrollPane(inboxPanel);
        tabbedPane.addTab("Inbox", scrollInboxPane);

        // add top panel and tabbed pane to the frame
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.setBackgroundAt(0, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(1, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(2, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(3, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(4, new Color(202, 191, 168));
        tabbedPane.setBackgroundAt(5, new Color(202, 191, 168));

        // "Animal Shelter ©" label at the bottom
        JLabel bottomLabel = new JLabel("Animal Shelter ©", JLabel.CENTER);
        bottomLabel.setBorder(new EmptyBorder(5, 0, 8, 0));
        add(bottomLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setWindowData() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        setTitle("Member Homepage");
        UIUtils.center(this);
    }

    private void setPetsPanel(JPanel petPanel, Color petPanelColor, JPanel searchPanel, JPanel requestsPanel) {
        petPanel.add(searchPanel, BorderLayout.SOUTH);

        for (PostDTO post : this.posts) {
            JPanel petPostPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center the component
            gbc.insets = new Insets(0, 0, 0, 0); // Padding around the component
            gbc.fill = GridBagConstraints.NONE; // Do not resize the component

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

            JLabel status = new JLabel("Status: " + post.getStatus());
            switch (post.getStatus()) {
                case "Adopted" -> status.setForeground(new Color(67, 177, 26));
                case "Not adopted" -> status.setForeground(new Color(214, 116, 3));
                case "In foster care" -> status.setForeground(new Color(9, 120, 188));
                case "Under treatment" -> status.setForeground(new Color(221, 9, 9));
            }
            petInfoPanel.add(status);

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

            viewButton.addActionListener(e -> {
                PetPostWindow petPostWindow = new PetPostWindow(this,user, post);
                petPostWindow.setVisible(true);

                petPostWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateRequests(requestsPanel, petPanelColor);
                    }
                });
            });

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
    }

    public void setInboxPanel() {
        inboxPanel.removeAll();

        inboxPanel.setLayout(new BoxLayout(inboxPanel, BoxLayout.Y_AXIS));
        Color messagePanelColor = new Color(207, 198, 176, 234);

        ArrayList<Message> userMessages = MessagesList.getInstance().getAll();

        for (Message message : userMessages) {
            JPanel messagePostPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            // panel for message info
            JPanel messageInfoPanel = new JPanel();
            messageInfoPanel.setLayout(new BoxLayout(messageInfoPanel, BoxLayout.Y_AXIS));
            messageInfoPanel.setBackground(messagePanelColor);

            messageInfoPanel.add(new JLabel(" "));
            messageInfoPanel.add(new JLabel("From: " + MessagesList.getInstance().messageFrom(message)));
            messageInfoPanel.add(new JLabel("To: " + MessagesList.getInstance().messageTo(message)));
            messageInfoPanel.add(new JLabel("Content: " + message.getText()));
            messageInfoPanel.add(new JLabel("Sent at: " + message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            messageInfoPanel.add(new JLabel(" "));

            if (!MessagesList.getInstance().messageFrom(message).equals("shelter")) {
                messageInfoPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align components to the right
            } else {
                addReplyButton(messageInfoPanel, message, this);
            }
            gbc.gridx = 0;
            messagePostPanel.add(messageInfoPanel, gbc);

            // create a line separator - separates messages
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 1px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            inboxPanel.add(lineSeparator, gbc);

            messagePostPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            messagePostPanel.setBackground(messagePanelColor);

            inboxPanel.add(messagePostPanel);
        }
    }

    public void addReplyButton(JPanel messageInfoPanel, Message message, MemberWindow memberWindow){
        messageInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align components to the left
        JButton replyButton = new JButton("Reply");
        replyButton.setFocusable(false);
        replyButton.setBackground(new Color(12, 129, 213));  // Set the background color
        replyButton.setForeground(Color.WHITE);  // Set the text color
        replyButton.setFocusPainted(false);
        replyButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        replyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        replyButton.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) null, "Reply", true);
            dialog.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea(5, 20);
            dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String replyText = textArea.getText();
                    Message newMessage = new Message(replyText, MessageOwner.MEMBER, user.getId());
                    MessagesList.getInstance().addMessage(newMessage);
                    memberWindow.setInboxPanel();
                    memberWindow.revalidate();
                    memberWindow.repaint();
                    dialog.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(sendButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        messageInfoPanel.add(replyButton);
    }

    private JPanel createButtonPanel(Request r, JPanel requestsPanel, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set background color
        Color petPanelColor = new Color(207, 198, 176, 234);
        panel.setBackground(petPanelColor);

        // Common properties for buttons
        Dimension buttonSize = new Dimension(150, 40); // Fixed size for the buttons

        // Create the "Delete" button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(221, 9, 9));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setToolTipText("Click to reject");

        if(r.getState() != RequestState.PENDING) {
            deleteButton.setEnabled(false);
            deleteButton.setBackground(new Color(122, 193, 117));
            deleteButton.setText("<html><font color = white>Delete</font></html>");
        }

        // Add action listener to the "Delete" button
        deleteButton.addActionListener(e -> {
            String message = "Do you want to proceed?";
            int result = JOptionPane.showConfirmDialog(
                    this, // Parent component; null makes it appear centered on the screen
                    message, // Message to display
                    "Confirm", // Title of the dialog
                    JOptionPane.YES_NO_OPTION, // Option type: Yes/No
                    JOptionPane.QUESTION_MESSAGE // Message type: Question
            );

            if (result == JOptionPane.YES_OPTION) {
                requestsController.deleteRequest(r.getId());
                this.updateRequests(requestsPanel, color);
            }
        });

        // Configure GridBagConstraints for buttons
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0); // Space around buttons

        // Center the buttons horizontally
        gbc.anchor = GridBagConstraints.CENTER;

        // Add "Approve" button to the panel
        //gbc.gridy = 0;
        //panel.add(editButton, gbc);

        // Add "Reject" button to the panel
        gbc.gridy = 0;
        panel.add(deleteButton, gbc);

        return panel;
    }

    public void updateRequests(JPanel requestsPanel, Color petPanelColor) {
        requestsPanel.removeAll();

        for (Request request : requestsController.getAllByUser(user)) {
            JPanel infoPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            // gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.33;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER; // Center the component
            gbc.insets = new Insets(0, 0, 0, 0); // Padding around the component
            gbc.fill = GridBagConstraints.NONE; // Do not resize the component

            // buttons panel
            JPanel buttonsPanel = createButtonPanel(request, requestsPanel, petPanelColor);
            gbc.gridx = 0;
            infoPanel.add(buttonsPanel, gbc);

            // panel for pet info
            JPanel reqInfoPanel = new JPanel();
            reqInfoPanel.setLayout(new BoxLayout(reqInfoPanel, BoxLayout.Y_AXIS));
            reqInfoPanel.setBackground(petPanelColor);
            reqInfoPanel.add(Box.createHorizontalGlue());

            JLabel type = new JLabel(request.getType().toString());
            type.setAlignmentX(Component.CENTER_ALIGNMENT);
            switch (request.getType().toString()) {
                case "ADOPTION" -> type.setForeground(new Color(67, 177, 26));
                case "TEMPORARY CARE" -> type.setForeground(new Color(214, 116, 3));
                case "VOLUNTEERING" -> type.setForeground(new Color(9, 120, 188));
                case "ANIMAL REGISTRATION" -> type.setForeground(new Color(221, 9, 9));
                case "POST EDITING" -> type.setForeground(new Color(128, 0, 228));
            }
            reqInfoPanel.add(type);

            JLabel state = new JLabel(request.getState().toString());
            state.setAlignmentX(Component.CENTER_ALIGNMENT);
            switch (request.getState()) {
                case RequestState.APPROVED -> state.setForeground(new Color(45, 138, 9));
                case RequestState.PENDING -> state.setForeground(new Color(81, 96, 220));
                case RequestState.REJECTED -> state.setForeground(new Color(222, 41, 41));
            }
            reqInfoPanel.add(state);

            reqInfoPanel.add(new JLabel(" "));
            if (request.getAdditionalText() == null) {
                JLabel noteLabel = new JLabel("Note: No note added!");
                noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                reqInfoPanel.add(noteLabel);
            }
            else {
                JLabel noteLabel = new JLabel("<html><body>Note: " + request.getAdditionalText() +
                        "</body></html>");
                noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                noteLabel.setPreferredSize(new Dimension(200,100));
                reqInfoPanel.add(noteLabel);
            }

            gbc.gridx = 1;
            infoPanel.add(reqInfoPanel, gbc);
            JButton viewButton = new JButton("View animal");
            viewButton.setFocusable(false);
            viewButton.setBackground(new Color(163, 153, 131));  // Set the background color
            viewButton.setForeground(Color.WHITE);  // Set the text color
            viewButton.setFocusPainted(false);
            viewButton.setBorder(new EmptyBorder(5, 10, 5, 10));
            viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            viewButton.addActionListener(e -> {
                if (request.getType() == RequestType.ADOPTION || request.getType() == RequestType.TEMPORARY_CARE) {
                    PostDTO post = feedController.getDTOById(request.getPostId());
                    PostDialog postDialog = new PostDialog(this, post);
                } else if (request.getType() == RequestType.ANIMAL_REGISTRATION) {
                    AnimalDialog animalDialog = new AnimalDialog(this, request.getUpdatedAnimal(), null);
                } else if (request.getType() == RequestType.POST_EDITING) {
                    PostDTO post = feedController.getDTOById(request.getPostId());
                    AnimalDialog animalDialog = new AnimalDialog(this, request.getUpdatedAnimal(), post);
                }
            });

            if(request.getType() == RequestType.VOLUNTEERING){
                viewButton.disable();
                viewButton.setForeground(new Color(0,0,0,0));
            }
            // set constraints for the view button
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 15, 15, 15); // Adjust as needed for padding

            infoPanel.add(viewButton, gbc);

            // create a line separator - separates pets
            JPanel lineSeparator = new JPanel();
            lineSeparator.setBackground(Color.GRAY);
            lineSeparator.setPreferredSize(new Dimension(0, 1)); // Height 2px, width 0 to be adjusted by layout
            gbc.gridy = 1;
            requestsPanel.add(lineSeparator, gbc);

            infoPanel.setBorder(new EmptyBorder(7, 0, 7, 0));
            infoPanel.setBackground(new Color(207, 198, 176, 234));

            requestsPanel.add(infoPanel);
        }
    }
}
