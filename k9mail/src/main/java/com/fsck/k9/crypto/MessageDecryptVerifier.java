package com.fsck.k9.crypto;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.text.TextUtils;

import com.fsck.k9.mail.Body;
import com.fsck.k9.mail.BodyPart;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Multipart;
import com.fsck.k9.mail.Part;
import com.fsck.k9.mail.internet.MessageExtractor;
import com.fsck.k9.mail.internet.MimeUtility;
import org.openintents.openpgp.util.OpenPgpUtils;

import static com.fsck.k9.mail.internet.MimeUtility.isSameMimeType;


public class MessageDecryptVerifier {
    private static final String MULTIPART_ENCRYPTED = "multipart/encrypted";
    private static final String MULTIPART_SIGNED = "multipart/signed";
    private static final String PROTOCOL_PARAMETER = "protocol";
    private static final String APPLICATION_PGP_ENCRYPTED = "application/pgp-encrypted";
    private static final String APPLICATION_PGP_SIGNATURE = "application/pgp-signature";
    private static final String TEXT_PLAIN = "text/plain";


    public static List<Part> findEncryptedParts(Part startPart) {
        List<Part> encryptedParts = new ArrayList<Part>();
        Stack<Part> partsToCheck = new Stack<Part>();
        partsToCheck.push(startPart);

        while (!partsToCheck.isEmpty()) {
            Part part = partsToCheck.pop();
            Body body = part.getBody();

            if (isPgpMimeEncryptedPart(part)) {
                encryptedParts.add(part);
            } else if (body instanceof Multipart) {
                Multipart multipart = (Multipart) body;
                for (int i = multipart.getCount() - 1; i >= 0; i--) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    partsToCheck.push(bodyPart);
                }
            }
        }

        return encryptedParts;
    }

    public static List<Part> findSignedParts(Part startPart) {
        List<Part> signedParts = new ArrayList<Part>();
        Stack<Part> partsToCheck = new Stack<Part>();
        partsToCheck.push(startPart);

        while (!partsToCheck.isEmpty()) {
            Part part = partsToCheck.pop();
            Body body = part.getBody();

            if (isPgpMimeSignedPart(part)) {
                signedParts.add(part);
            } else if (body instanceof Multipart) {
                Multipart multipart = (Multipart) body;
                for (int i = multipart.getCount() - 1; i >= 0; i--) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    partsToCheck.push(bodyPart);
                }
            }
        }

        return signedParts;
    }

    public static List<Part> findPgpInlineParts(Part startPart) {
        List<Part> inlineParts = new ArrayList<Part>();
        Stack<Part> partsToCheck = new Stack<Part>();
        partsToCheck.push(startPart);

        while (!partsToCheck.isEmpty()) {
            Part part = partsToCheck.pop();
            String mimeType = part.getMimeType();
            Body body = part.getBody();

            if (isSameMimeType(mimeType, TEXT_PLAIN)) {
                String text = MessageExtractor.getTextFromPart(part);
                if (TextUtils.isEmpty(text)) {
                    continue;
                }
                switch (OpenPgpUtils.parseMessage(text, true)) {
                    case OpenPgpUtils.PARSE_RESULT_MESSAGE:
                    case OpenPgpUtils.PARSE_RESULT_SIGNED_MESSAGE:
                        inlineParts.add(part);
                }
            } else if (body instanceof Multipart) {
                Multipart multipart = (Multipart) body;
                for (int i = multipart.getCount() - 1; i >= 0; i--) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    partsToCheck.push(bodyPart);
                }
            }
        }

        return inlineParts;
    }

    public static byte[] getSignatureData(Part part) throws IOException, MessagingException {
        if (isPgpMimeSignedPart(part)) {
            Body body = part.getBody();
            if (body instanceof Multipart) {
                Multipart multi = (Multipart) body;
                BodyPart signatureBody = multi.getBodyPart(1);
                if (isSameMimeType(signatureBody.getMimeType(), APPLICATION_PGP_SIGNATURE)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    signatureBody.getBody().writeTo(bos);
                    return bos.toByteArray();
                }
            }
        }

        return null;
    }

    private static boolean isPgpMimeSignedPart(Part part) {
        if (!isSameMimeType(part.getMimeType(), MULTIPART_SIGNED)) {
            return false;
        }

        String contentType = part.getContentType();
        String protocol = MimeUtility.getHeaderParameter(contentType, PROTOCOL_PARAMETER);
        return APPLICATION_PGP_SIGNATURE.equalsIgnoreCase(protocol);
    }

    private static boolean isPgpMimeEncryptedPart(Part part) {
        if (!isSameMimeType(part.getMimeType(), MULTIPART_ENCRYPTED)) {
            return false;
        }

        String contentType = part.getContentType();
        String protocol = MimeUtility.getHeaderParameter(contentType, PROTOCOL_PARAMETER);
        return APPLICATION_PGP_ENCRYPTED.equalsIgnoreCase(protocol);
    }
}
