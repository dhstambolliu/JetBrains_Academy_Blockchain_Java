package blockchain.util;

import java.io.*;

public class SerializationUtil {

    public static void serialize(Object obj, String fileName) throws IOException {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            oos.writeObject(obj);
        }
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            return ois.readObject();
        }
    }
}