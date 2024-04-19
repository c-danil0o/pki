package com.security.pki.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.autoconfigure.ssl.SslBundleProperties;
import org.springframework.stereotype.Repository;

import javax.swing.text.Element;
import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class PrivateRepository {
    private String keyFolderPath = "src/main/resources/keys/";
    private String keyStorePasswords = "src/main/resources/passwords/password.csv";

    public PrivateRepository() {

    }

    public PrivateKey getKey(String alias) {
        try (FileReader fileReader = new FileReader(keyFolderPath + alias + ".pem");
             PEMParser pemParser = new PEMParser(fileReader)) {

            Object obj = pemParser.readObject();

            if (obj instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) obj;
                KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
                return keyPair.getPrivate();
            }

            if (obj instanceof PrivateKey) {
                return (PrivateKey) obj;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveKey(PrivateKey key, String alias) {
        try (FileWriter fileWriter = new FileWriter(keyFolderPath + alias + ".pem"); PEMWriter pemWriter = new PEMWriter(fileWriter)) {
            pemWriter.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void deleteKey(String alias){
        File myObj = new File(keyFolderPath + alias + ".pem");
        myObj.delete();
    }

    public void deletePassword(String keyStoreName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(keyStorePasswords));
            List<String[]> allElements = reader.readAll();
            allElements.removeIf(e -> e[0].equals(keyStoreName));
            FileWriter sw = new FileWriter(keyStorePasswords);
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(allElements);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPassword(String keyStoreName) {

        try {
            FileReader filereader = new FileReader(keyStorePasswords);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[0].equals(keyStoreName)) {
                    return nextRecord[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void savePassword(String keyStoreName, String password) {
        try {
            FileWriter outputfile = new FileWriter(keyStorePasswords, true);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] data = {keyStoreName, password};
            writer.writeNext(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
