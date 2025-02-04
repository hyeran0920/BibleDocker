package com.library.bible.qr;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    /*
     * @param data      QR 코드에 저장할 데이터
     * @param filePath  QR 코드 이미지 파일 경로
     * @throws WriterException QR 코드 생성 중 예외
     * @throws IOException     파일 저장 중 예외
    */
	
	//이거 UploadController에 넣을까 고민중
	//QR 생성하고 저장
	public static void generateQRCode(String data, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        
        System.out.println("generateQRCode = "+path);
    }
	
	//QR 이미지를 Base64 문자열로 반환
	public static String generateQRCodeURL(String data) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 50, 50);
        
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", outputStream);

        byte[] qrBytes = outputStream.toByteArray();
        String base64QRCode = Base64.getEncoder().encodeToString(qrBytes);

        return "data:image/png;base64," + base64QRCode;
    }
}
