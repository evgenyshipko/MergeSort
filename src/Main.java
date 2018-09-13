import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static String dataType;
    public static String regime;
    public static String out;


    public static void main(String[] args) throws IOException {
        //cписок имен файлов
        ArrayList<String> names = new ArrayList<>();

        //список объектов StreamValue
        ArrayList<StreamValue> list = new ArrayList<>();

        //обрабатываем считанное с консоли + обрабатываем случай, когда в консоль введено недостатоно данных
        try {
            if (!args[0].equals("-a") && !args[0].equals("-d") && !args[0].equals("-i") && !args[0].equals("-s")){
                System.out.println("Illegal argument args[0]");
                System.exit(0);
            }
            else if (args[0].equals("-a") || args[0].equals("-d")) {
                regime = args[0];
                dataType = args[1];
                out = args[2];
                for (int i = 3; i < args.length; i++) {
                    names.add(args[i]);
                }
            }
            else if (args[0].equals("-i") || args[0].equals("-s")){
                regime = "-a";
                dataType = args[0];
                out = args[1];
                for (int i = 2; i < args.length; i++) {
                    names.add(args[i]);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Name of the output file was not found.");
            System.exit(0);
        }

        //обработка случая, когда введен неправильный аргумент
        if (!dataType.equals("-i") && !dataType.equals("-s")){
            System.out.println("Illegal argument (data type).");
            System.exit(0);
        }


        //заполняем список с объектами StreamValue (поток - значение)
        for (int i = 0; i < names.size(); i++){
            StreamValue str;
            //обработка ситуации когда файл не найден
            try {
                str = new StreamValue(names.get(i));
                list.add(str);
            }
            catch (FileNotFoundException e){
                System.out.println("File " + names.get(i) + " was not found.");
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(out));

        int ind = 0;
        String min_val = null;

        //цикл сортировки слиянием
        while(true) {
//            вывод значений StreamValue из списка list на экран
//            for (StreamValue a : list){
//                System.out.print(a.value + " ");
//            }

            if (list.size() > 0) {
                //обработка ситуации, когда среди численных значений присутствует строка
                try {
                    if (regime.equals("-d"))
                        ind = maxIndex(list);
                    else
                        ind = minIndex(list);

                    min_val = list.get(ind).value;

                    writer.write(min_val + "\r\n");
                }
                catch (NumberFormatException e){
                    System.out.println("String detected!");
                }
                if (list.get(ind).isEmpty())
                    list.remove(ind);
                else
                    list.get(ind).value = list.get(ind).stream.readLine();
            }
            else
                break;

//            вывод минимального (максимального) значения на экран
//            System.out.print("|| Min: " + min_val);
//            System.out.println();
        }

        writer.close();
    }

    public static int minIndex(ArrayList<StreamValue> list) throws NumberFormatException{
        String min = list.get(0).value;
        int index = 0;
        for (int i = 0; i < list.size(); i++){
            String iter = list.get(i).value;
            if (dataType.equals("-i")){
                Integer num_iter = Integer.parseInt(iter);
                Integer num_min = Integer.parseInt(min);
                if (num_iter.compareTo(num_min) < 0) {
                    min = list.get(i).value;
                    index = i;
                }
            }
            else if(dataType.equals("-s")){
                //ловим исключение если есть пустой файл
                try {
                    if (iter.compareTo(min) < 0) {
                        min = list.get(i).value;
                        index = i;
                    }
                }
                catch (NullPointerException e){}
            }
        }
        return index;
    }

    public static int maxIndex(ArrayList<StreamValue> list) throws NumberFormatException{
        String max = list.get(0).value;
        int index = 0;
        for (int i = 1; i < list.size(); i++){
            String iter = list.get(i).value;
            if (dataType.equals("-i")){
                Integer num_iter = Integer.parseInt(iter);
                Integer num_min = Integer.parseInt(max);
                if (num_iter.compareTo(num_min) > 0) {
                    max = list.get(i).value;
                    index = i;
                }
            }
            else if (dataType.equals("-s")){
                //ловим исключение если есть пустой файл
                try {
                    if (iter.compareTo(max) > 0) {
                        max = list.get(i).value;
                        index = i;
                    }
                }
                catch (NullPointerException e){}
            }
        }
        return index;
    }
}