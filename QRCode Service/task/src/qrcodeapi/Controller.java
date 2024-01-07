package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.client.HttpClientErrorException;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.*;

@RestController()
public class Controller {

    QRCodeCreator QRCodeCreator = new QRCodeCreator();

    ErrorCorrectionLevel[] correctionLevels = ErrorCorrectionLevel.values();

    @GetMapping("/api/health")
    public ResponseEntity<String> getServiceHealth(){
        return ResponseEntity.status(HttpStatus.OK).body("200 OK");
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getQRCode(
            @RequestParam String contents,
            @RequestParam(defaultValue = "250") int size,
            @RequestParam(defaultValue = "png") String type,
            @RequestParam(defaultValue = "L") String correction) {

        //Contents cannot be null or blank error
        if (contents == null || contents.isBlank()) {
            ApiErrorResponse errorResponseDto = new ApiErrorResponse("Contents cannot be null or blank");
            return new ResponseEntity<ApiErrorResponse>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }

        //Image size error
        if (size < 150 || size > 350) {
            ApiErrorResponse errorResponseDto = new ApiErrorResponse("Image size must be between 150 and 350 pixels");
            return new ResponseEntity<ApiErrorResponse>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }

        if (!(correction.matches("[LHMQ]"))) {
            ApiErrorResponse errorResponseDto = new ApiErrorResponse("Permitted error correction levels are L, M, Q, H");
            return new ResponseEntity<ApiErrorResponse>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }

        BufferedImage qrcode = QRCodeCreator.createQRCode(contents, size, type, ErrorCorrectionLevel.valueOf(correction));

        if ((type.equals("gif")) || type.equals("GIF")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_GIF)
                    .body(qrcode);
        }

        if (type.equals("png") || type.equals("PNG")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrcode);
        }

        if (type.equals("jpeg") || type.equals("JPEG")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(qrcode);
        }

        ApiErrorResponse errorResponseDto = new ApiErrorResponse("Only png, jpeg and gif image types are supported");
        return new ResponseEntity<ApiErrorResponse>(errorResponseDto, HttpStatus.BAD_REQUEST);

    }
}

