
public class cryptography implements Runnable {
    int encryptionType = 0;
    String method;

    @Override
    public void run() {
        switch (encryptionType) {
            /*
             * Notes on each method:
             * 1. AES is the "oldest"
             */
            case 1:
                method = "AES 128"; // easy encryption
                break;
            case 2:
                method = "AES 192"; // alright method getting more secure
                break;
            case 3:
                method = "AES 256"; // more bits = more power
                break;
            case 4:
                method = "RSA"; // public key methodology.
                // RSA rsa = new RSA();
                // try {
                //     String encryptedMessage = rsa.encrypt(method);
                //     String decryptedMessage = rsa.decrypt(encryptedMessage);
                // } catch (Exception ignored) {
                // }
                break;
            case 5:
                method = "OpenVPN 1"; // UDP used for streaming, things that don't need to be crazy secure
                break;
            case 6:
                method = "OpenVPN 2"; // TCP used for security and reliability
                break;
            case 7:
                method = "WireGaurd"; // speed and best suited for mobile
                break;
        }
    }
}