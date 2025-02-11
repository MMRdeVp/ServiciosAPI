package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Swing extends JFrame {
    private JTextField txtTo, txtSubject;
    private JTextArea txtMessage;
    private JButton btnSend;
    private JLabel lblStatus;

    public Swing() {
        setTitle("Envío de Correo SMTP");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel panelInputs = new JPanel();
        panelInputs.setLayout(new GridLayout(4, 1, 10, 10));
        panelInputs.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtTo = new JTextField();
        txtSubject = new JTextField();
        txtMessage = new JTextArea(5, 20);
        btnSend = new JButton("Enviar");
        lblStatus = new JLabel(" ", JLabel.CENTER);


        txtTo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        txtSubject.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        txtMessage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);


        btnSend.setBackground(new Color(60, 179, 113));
        btnSend.setForeground(Color.WHITE);
        btnSend.setFocusPainted(false);
        btnSend.setFont(new Font("Arial", Font.BOLD, 14));


        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);

        panelInputs.add(new JLabel("Para:"));
        panelInputs.add(txtTo);
        panelInputs.add(new JLabel("Asunto:"));
        panelInputs.add(txtSubject);


        add(panelInputs, BorderLayout.NORTH);
        add(new JScrollPane(txtMessage), BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
        add(btnSend, BorderLayout.SOUTH);


        btnSend.addActionListener(this::sendEmail);
    }

    private void sendEmail(ActionEvent e) {
        String to = txtTo.getText().trim();
        String subject = txtSubject.getText().trim();
        String message = txtMessage.getText().trim();

        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            lblStatus.setText("❌ Todos los campos son obligatorios.");
            return;
        }

        btnSend.setEnabled(false);
        lblStatus.setText("📨 Enviando...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                enviarCorreo(to, subject, message);
                return null;
            }

            @Override
            protected void done() {
                btnSend.setEnabled(true);
            }
        }.execute();
    }

    private void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        String host = "smtp.gmail.com";
        String username = "saas2025pito@gmail.com";
        String password = "jfbs hwvs qadh pwlw";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);


            Transport.send(message);
            System.out.println("Correo enviado con éxito.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Swing().setVisible(true));
    }
}
