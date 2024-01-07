package qrcodeapi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeCreator {

    private BufferedImage image;

    QRCodeWriter writer = new QRCodeWriter();

    public QRCodeCreator() {
    }

    public BufferedImage createQRCode(String data, int size, String type, ErrorCorrectionLevel correction){

        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correction);

        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }

//    public BufferedImage createQRCode(int size){
//        this.image = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
//        Graphics2D g = image.createGraphics();
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, size, size);
//        g.dispose();
//        return image;
//    }

}


