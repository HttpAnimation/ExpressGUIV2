import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExpressGUI {

    private static Process expressVPNProcess;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpressGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        try {
            // Use system look and feel for a native appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("ExpressGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 10)); // Vertical layout with spacing
        buttonPanel.setBackground(Color.DARK_GRAY);
        frame.add(new JScrollPane(buttonPanel), BorderLayout.CENTER); // Add scrolling

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("expressvpn", "list", "all");
            Process expressVPNProcess = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(expressVPNProcess.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                final String locationLine = line; // Make it effectively final
                JButton button = new JButton(locationLine);
                button.setBackground(new Color(33, 33, 33)); // Darker background
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setFont(new Font("Arial", Font.BOLD, 12)); // Larger font
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        connectToLocation(locationLine);
                    }
                });
                buttonPanel.add(button);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton githubButton = new JButton("GitHub");
        githubButton.setBackground(new Color(25, 118, 210)); // Blue background
        githubButton.setForeground(Color.WHITE);
        githubButton.setFocusPainted(false);
        githubButton.setFont(new Font("Arial", Font.BOLD, 12)); // Larger font
        githubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGitHub();
            }
        });
        frame.add(githubButton, BorderLayout.EAST);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(new Color(198, 40, 40)); // Red background
        disconnectButton.setForeground(Color.WHITE);
        disconnectButton.setFocusPainted(false);
        disconnectButton.setFont(new Font("Arial", Font.BOLD, 12)); // Larger font
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        frame.add(disconnectButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void connectToLocation(String location) {
        try {
            if (expressVPNProcess != null) {
                expressVPNProcess.destroy();
            }

            String locationName = location.split("\\s+")[0];
            ProcessBuilder processBuilder = new ProcessBuilder("expressvpn", "connect", locationName);
            expressVPNProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        if (expressVPNProcess != null) {
            expressVPNProcess.destroy();
        }
    }

    private static void openGitHub() {
        try {
            String url = "https://github.com/HttpAnimation/ExpressGUIV2";
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
