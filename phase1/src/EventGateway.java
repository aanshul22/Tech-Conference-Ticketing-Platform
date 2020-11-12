import java.io.*;
import java.util.HashMap;


public class EventGateway implements Serializable {

    public EventManager readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path); //the path needs to be a .ser so eventData.ser in parameter
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize the EventManager
            EventManager em = (EventManager) input.readObject();
            input.close();
            return em;
        } catch (IOException ex) {
            System.out.println("Cannot read from input file, returning a new EventManager.");
            return new EventManager();
        }
    }

    public void saveToFile(String filePath, EventManager eventManager) throws IOException {

        OutputStream file = new FileOutputStream(filePath); //the path needs to be a .ser so eventData.ser in parameter
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutputStream output = new ObjectOutputStream(buffer);

        // serialize the EventManager object
        output.reset();
        output.writeObject(eventManager);
        output.close();
    }
}