package lt.visma.Meeting.model.controller;

import com.google.gson.Gson;
import lt.visma.Meeting.model.model.Meeting;
import lt.visma.Meeting.model.model.Meeting2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class MeetingController {

    @PostMapping ("/meeting/insert")
    String insertMeeting(@RequestBody Meeting meeting) {
        String filePath = "meetings.json";
        try {

            Gson gson = new Gson();
            String meetingJson = gson.toJson(meeting);
            String data = Files.readString(Path.of(filePath));

            if (data!= null && data.length() > 0){
                data= data.substring(0, data.length() - 1);
                data +=",";
                data += meetingJson;
            }
            else {
                data ="[";
                data +=meetingJson;
                data +="]";
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(data);
            writer.close();


        }

        catch (IOException e){
            System.out.println("nepavyko issaugoti i faila");
        }
        return "name";
    }
    @GetMapping ("/allMeetings/get")
    String getAllMeetings() {
        return "description";

    }














}
