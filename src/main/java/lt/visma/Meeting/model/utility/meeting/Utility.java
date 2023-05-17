package lt.visma.Meeting.model.utility.meeting;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lt.visma.Meeting.model.model.Meeting;
import lt.visma.Meeting.model.model.User;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List <Meeting> getAllMeetingsFromFile() {
        List <Meeting> meetings = new ArrayList<Meeting>();
        String filePath = "meetings.json";
        try {
            String data = Files.readString(Path.of(filePath));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Meeting>>() {}.getType();
            meetings = new Gson().fromJson(data, listType);


        } catch (IOException e) {
            System.out.println("nepavyko nuskaityti is failo"+e);
        }
        if (meetings==null){
            meetings=new ArrayList<>();
        }
        return meetings;
    }


    public static List <User> getAllUsersFromFile() {
        List <User> user = new ArrayList<User>();
        String filePath = "user.json";
        try {
            String data = Files.readString(Path.of(filePath));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            user = new Gson().fromJson(data, listType);


        } catch (IOException e) {
            System.out.println("nepavyko nuskaityti is failo"+e);
        }
        if (user == null) {
            user=new ArrayList<>();
        }
        return user;
    }
    public static boolean saveMeetingsToFile(ArrayList<Meeting>meetings){
        try {
            Gson gson = new Gson();
            String filePath = "meetings.json";
           BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(gson.toJson(meetings));
            writer.close();

        }
        catch (IOException e) {
            System.out.println("Nepavyko issaugoti i faila"+e);
            return false;
        }
        return true;
    }


}


