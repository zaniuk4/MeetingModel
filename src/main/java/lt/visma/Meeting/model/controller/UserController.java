package lt.visma.Meeting.model.controller;

import com.google.gson.Gson;
import lt.visma.Meeting.model.model.Meeting;
import lt.visma.Meeting.model.model.User;
import lt.visma.Meeting.model.utility.meeting.Utility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@RestController
public class UserController {

    public final String filePath = "user.json";

    @PostMapping ("/user/insert")
    String insertUser(@RequestBody User user) {

        try {

            Gson gson = new Gson();
            ArrayList<User> allUser= (ArrayList<User>) Utility.getAllUsersFromFile();
            allUser.add(user);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(gson.toJson(allUser));
            writer.close();

        }
        catch (IOException e){
            System.out.println("nepavyko issaugoti i faila");
        }
        return "sekminga issaugotas user";
    }


}
