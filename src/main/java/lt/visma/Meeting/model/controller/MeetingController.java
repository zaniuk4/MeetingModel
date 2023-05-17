package lt.visma.Meeting.model.controller;

import com.google.gson.Gson;
import lt.visma.Meeting.model.model.*;
import lt.visma.Meeting.model.utility.meeting.Utility;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MeetingController {
    public final String filePath = "meetings.json";


    @PostMapping ("/meeting/insert")
    String insertMeeting(@RequestBody Meeting meeting) {

        try {

            Gson gson = new Gson();

            ArrayList<Meeting>allMeetings= (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
            allMeetings.add(meeting);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(gson.toJson(allMeetings));
            writer.close();
            String data = Files.readString(Path.of(filePath));



        }
        catch (IOException e){
            System.out.println("nepavyko issaugoti i faila");
        }
        return "sekmingai issaugotas meeting";
    }
    @GetMapping ("/allMeetings/get")
    String getAllMeetings() {
        ArrayList<Meeting> meetings = (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
        Gson gson = new Gson();
        return gson.toJson(meetings);
    }
    @GetMapping ("/allMeetings/filter")
    String filterAllMeetings(@RequestParam(required = false) String description,@RequestParam(required = false) Long responsiblePersonId,
                             @RequestParam(required = false) Category category, @RequestParam (required = false)Type type,
                             @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
                             @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate,
                             @RequestParam(required = false)Integer numberOfAttendees) {

        ArrayList<Meeting> meetings = (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
        if (description != null) {
            meetings = (ArrayList<Meeting>) meetings.stream().filter(m -> m.getDescription().equals(description)).collect(Collectors.toList());
        }
        if (responsiblePersonId != null) {
            meetings = (ArrayList<Meeting>) meetings.stream().filter(m -> m.getResponsiblePerson().getId() == responsiblePersonId).collect(Collectors.toList());
        }

        if (category != null) {
            meetings = (ArrayList<Meeting>) meetings.stream().filter(m -> m.getCategory().equals(category)).collect(Collectors.toList());
        }

        if (type != null) {
            meetings = (ArrayList<Meeting>) meetings.stream().filter(m -> m.getType().equals(type)).collect(Collectors.toList());
        }

        if (startDate !=null && endDate !=null){
            meetings = (ArrayList<Meeting>) meetings.stream().filter(m -> m.getEndDate().compareTo(startDate) >=0 && m.getStartDate().compareTo(endDate)<=0).collect(Collectors.toList());
        }

        if(numberOfAttendees !=null){
            meetings =(ArrayList<Meeting>) meetings.stream().filter(m-> m.getAllUsers().size()>=numberOfAttendees).collect(Collectors.toList());

        }
        Gson gson = new Gson();

        return gson.toJson(meetings);
    }


    @PostMapping ("/meeting/delete")
    String deleteMeeting(@RequestBody Meeting meeting) {
        ArrayList<Meeting> meetings = (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
        meetings.remove(meeting);

        BufferedWriter writer = null;
        try {
            Gson gson = new Gson();
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(gson.toJson(meetings));
            writer.close();

        }
        catch (IOException e) {
            System.out.println("Nepavyko issaugoti i faila"+e);
        }
        return "Meeting deleted succesfully";

    }

    @PostMapping ("/meeting/addUsers")
    String addUsersToMeeting (@RequestBody UserMeeting userMeeting) {
        ArrayList<Meeting> meetings = (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
        for (Meeting meeting : meetings){
            if (meeting.equals(userMeeting.getMeeting())){
                if(meeting.getAllUsers().contains(userMeeting.getUser())){
                    return "This usre is already added to the meeting";
                }
                meeting.getAllUsers().add(userMeeting.getUser());
            }

        }
        Utility.saveMeetingsToFile(meetings);
        return "user added";

    }
    @PostMapping ("/meetings/removeUsers")
    String removeUsers (@RequestBody UserMeeting userMeeting) {
        ArrayList<Meeting> meetings = (ArrayList<Meeting>) Utility.getAllMeetingsFromFile();
        for (Meeting meeting : meetings) {
            if (meeting.equals(userMeeting.getMeeting())) {
                if(meeting.getResponsiblePerson().equals(userMeeting.getUser())){
                    return "cannot remove this person,because he's responsible for the meeting";
                }
                for (Meeting meeting1 : meetings) {
                    if (!meeting1.equals(meeting) && meeting1.getAllUsers().contains(userMeeting.getUser()) &&
                    meeting.getEndDate().compareTo(meeting1.getStartDate())>=0 &&
                    meeting.getStartDate().compareTo(meeting1.getEndDate())<=0) {
                        return "Cannot add to the meeting because user is already on another meeting";

                    }

                }
                meeting.getAllUsers().remove(userMeeting.getUser());
            }
        }
        Utility.saveMeetingsToFile(meetings);
        return "user removed";
    }



}
