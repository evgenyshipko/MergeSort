import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//объекты класса StreamValue содержат в себе поток и текущее значение, считанное из потока
public class StreamValue {

    public BufferedReader stream;
    public String value;

    StreamValue(String name) throws IOException {
        this.stream = new BufferedReader(new FileReader(name));
        this.value = stream.readLine();
    }

    public boolean isEmpty() throws IOException {
        if (!this.stream.ready()) {
            this.stream.close();
            return true;
        }
        else {
            return false;
        }
    }
}
