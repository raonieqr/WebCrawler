package webcrawler.Utils

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.EmailException
import org.apache.commons.mail.MultiPartEmail

class EmailHandler {

    static void sendMsg(String to, String pathFile, String fileName,
                                 String from) {
        try {
            MultiPartEmail email = new MultiPartEmail()

            email.setHostName('smtp.elasticemail.com')
            email.setAuthenticator(new DefaultAuthenticator(from,
                    'password'))
            email.setStartTLSRequired(true)
            email.setSmtpPort(2525)
            email.addTo(to)
            email.setFrom(from)
            email.setSubject('Arquivos do TISS')
            email.setMsg('Olá!\nSegue em anexo o historico de versões')

            if (pathFile != null && fileName != null) {
                try {
                    EmailAttachment attachment = new EmailAttachment()

                    attachment.setPath(pathFile + fileName)
                    attachment.setDisposition(EmailAttachment.ATTACHMENT)
                    attachment.setName(fileName)

                    email.attach(attachment)
                } catch (Exception e) {
                    println("Error: ao anexar o arquivo: ${e.message}")
                }
            }

            email.send()

            System.out.println('Email enviado com sucesso!')
        } catch (EmailException e) {
            System.err.println('Error: ao enviar o email: ' + e.getMessage())
        }
                                 }

}
