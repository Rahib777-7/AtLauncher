/**
```````` * Copyright 2013 by ATLauncher and Contributors
 *
 * ATLauncher is licensed under CC BY-NC-ND 3.0 which allows others you to
 * share this software with others as long as you credit us by linking to our
 * website at http://www.atlauncher.com. You also cannot modify the application
 * in any way or make commercial use of this software.
 *
 * Link to license: http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
package com.atlauncher.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.atlauncher.data.Account;

@SuppressWarnings("serial")
public class BottomBar extends JPanel {

    private JPanel leftSide;
    private JPanel middle;
    private JPanel rightSide;

    private Account fillerAccount;
    private boolean dontSave = false;

    private JButton toggleConsole;
    private JComboBox<Account> username;
    private JButton facebookIcon;
    private JButton twitterIcon;
    private JButton redditIcon;

    public BottomBar() {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 50)); // Make the bottom bar at least
                                                // 50 pixels high

        leftSide = new JPanel();
        leftSide.setLayout(new GridBagLayout());
        middle = new JPanel();
        middle.setLayout(new GridBagLayout());
        rightSide = new JPanel();
        rightSide.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        createButtons();
        setupListeners();

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 5);
        leftSide.add(toggleConsole, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 5);
        middle.add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 5);
        rightSide.add(facebookIcon, gbc);
        gbc.gridx++;
        rightSide.add(redditIcon, gbc);
        gbc.gridx++;
        rightSide.add(twitterIcon, gbc);

        add(leftSide, BorderLayout.WEST);
        add(middle, BorderLayout.CENTER);
        add(rightSide, BorderLayout.EAST);
    }

    /**
     * Sets up the listeners on the buttons
     */
    private void setupListeners() {
        toggleConsole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (LauncherFrame.settings.getConsole().isVisible()) {
                    LauncherFrame.settings.getConsole().log("Hiding console");
                    LauncherFrame.settings.getConsole().setVisible(false);
                    toggleConsole.setText("Show Console");
                } else {
                    LauncherFrame.settings.getConsole().log("Showing console");
                    LauncherFrame.settings.getConsole().setVisible(true);
                    toggleConsole.setText("Hide Console");
                }
            }
        });
        username.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!dontSave) {
                        LauncherFrame.settings.switchAccount((Account) username.getSelectedItem());
                    }
                }
            }
        });
        facebookIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LauncherFrame.settings.getConsole().log("Opening Up ATLauncher Facebook Page");
                Utils.openBrowser("http://www.facebook.com/ATLauncher");
            }
        });
        redditIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LauncherFrame.settings.getConsole().log("Opening Up ATLauncher Reddit Page");
                Utils.openBrowser("http://www.reddit.com/r/ATLauncher");
            }
        });
        twitterIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LauncherFrame.settings.getConsole().log("Opening Up ATLauncher Twitter Page");
                Utils.openBrowser("http://www.twitter.com/ATLauncher");
            }
        });
    }

    /**
     * Creates the JButton's for use in the bar
     */
    private void createButtons() {
        if (LauncherFrame.settings.getConsole().isVisible()) {
            toggleConsole = new JButton("Hide Console");
        } else {
            toggleConsole = new JButton("Show Console");
        }

        username = new JComboBox<Account>();
        username.setRenderer(new AccountsDropDownRenderer());
        fillerAccount = new Account("Select An Account");
        username.addItem(fillerAccount);
        for (Account account : LauncherFrame.settings.getAccounts()) {
            username.addItem(account);
        }
        Account active = LauncherFrame.settings.getAccount();
        if (active == null) {
            username.setSelectedIndex(0);
        } else {
            username.setSelectedItem(active);
        }

        facebookIcon = new JButton(Utils.getIconImage("/resources/FacebookIcon.png"));
        facebookIcon.setBorder(BorderFactory.createEmptyBorder());
        facebookIcon.setContentAreaFilled(false);
        facebookIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        redditIcon = new JButton(Utils.getIconImage("/resources/RedditIcon.png"));
        redditIcon.setBorder(BorderFactory.createEmptyBorder());
        redditIcon.setContentAreaFilled(false);
        redditIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        twitterIcon = new JButton(Utils.getIconImage("/resources/TwitterIcon.png"));
        twitterIcon.setBorder(BorderFactory.createEmptyBorder());
        twitterIcon.setContentAreaFilled(false);
        twitterIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Changes the text on the toggleConsole button when the console is hidden
     */
    public void hideConsole() {
        toggleConsole.setText("Show Console");
    }

    public void reloadAccounts() {
        username.removeAllItems();
        username.addItem(fillerAccount);
        for (Account account : LauncherFrame.settings.getAccounts()) {
            username.addItem(account);
        }
        dontSave = true;
        username.setSelectedItem(LauncherFrame.settings.getAccount());
        dontSave = false;
    }
}