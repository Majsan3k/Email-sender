//Då JDK-extensionen Javamail används så skickar jag även med en körbar jar-fil. Hoppas det var OK!

import static javax.swing.JOptionPane.showMessageDialog;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class EmailSender extends JFrame {

    private JTextField server;
    private JTextField userName;
    private JTextField password;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JTextArea emailTextArea;

    public EmailSender(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel emailInformation = new JPanel();
        emailInformation.setLayout(new GridLayout(6,2));
        emailInformation.add(new JLabel("Server: "));
        emailInformation.add(server = new JTextField());
        emailInformation.add(new JLabel("Username: "));
        emailInformation.add(userName = new JTextField());
        emailInformation.add(new JLabel("Password: "));
        emailInformation.add(password = new JTextField());
        emailInformation.add(new JLabel("From: "));
        emailInformation.add(from = new JTextField());
        emailInformation.add(new JLabel("To: "));
        emailInformation.add(to = new JTextField());
        emailInformation.add(new JLabel("Subject: "));
        emailInformation.add(subject = new JTextField());

        add(emailInformation, BorderLayout.NORTH);

        emailTextArea = new JTextArea();
        emailTextArea.setBackground(Color.WHITE);
        JScrollPane guestBookScroll = new JScrollPane(emailTextArea);
        add(guestBookScroll, BorderLayout.CENTER);

        JButton sendBtn = new JButton("Send");
        add(sendBtn, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> {
            sendEmail();
        });

        setSize(640, 480);
        setVisible(true);
    }

    public void sendEmail(){

        Properties properties = new Properties();
        properties.put("mail.smtp.host", server.getText());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(properties, new MyAuthenticator());
        MimeMessage message = new MimeMessage(session);
        try {
            message.setText(emailTextArea.getText());
            message.setSubject(subject.getText());
            Address fromAdress = new InternetAddress(from.getText());
            Address toAdress = new InternetAddress(to.getText());
            message.setFrom(fromAdress);
            message.setRecipients(Message.RecipientType.TO, String.valueOf(toAdress));
            Transport.send(message);
        } catch (MessagingException e) {
            if(e.getMessage().contains("AuthenticationFailedException")){
                showMessageDialog(this, "Authentication failed");
            }else {
                showMessageDialog(this, "Something went wrong, try again!");
            }
            System.out.println(e.getMessage());
        }
    }

    class MyAuthenticator extends Authenticator{
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName.getText(), password.getText());
        }
    }

    public static void main(String[] args){
        new EmailSender();
    }
}
