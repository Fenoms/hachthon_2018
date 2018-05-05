package team_3.hackathon.backend.database;

import com.google.gson.JsonObject;
import org.lightcouch.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
public class Generate {

    private static Random randomGenerator = new Random();

    private static ArrayList<String> problems = new ArrayList<String>(Arrays.asList("Why is it faster to process a sorted array than an unsorted array?", "How to undo the most recent commits in Git?",
            "How do I delete a Git branch both locally and remotely?", "What is the difference between 'git pull' and 'git fetch'?", "What is the correct JSON content type?", "What does the “yield” keyword do?",
            "How do I redirect to another webpage?", "How to modify existing, unpushed commits?", "How do JavaScript closures work?", "What is the “-->” operator in C++?", "How to check whether a string contains a substring in JavaScript?",
            "How to undo 'git add' before commit?", "What and where are the stack and heap?", "What does “use strict” do in JavaScript, and what is the reasoning behind it?",
            "How do I check if an element is hidden in jQuery?","How do I rename a local Git branch?", "Why does HTML think “chucknorris” is a color?", "var functionName = function() {} vs function functionName() {}",
            "Why is subtracting these two times (in 1927) giving a strange result?","How do I remove a particular element from an array in JavaScript?", "How to revert Git repository to a previous commit?",
            "How to remove local (untracked) files from the current Git working tree?","Can comments be used in JSON?","Which equals operator (== vs ===) should be used in JavaScript comparisons?",
            "Is Java “pass-by-reference” or “pass-by-value”?","What is the difference between String and string in C#?","How do I check out a remote Git branch?","What is the difference between “px”, “dip”, “dp” and “sp”?"));

    public static String getProblem(){
        int index = randomGenerator.nextInt(problems.size());
        return problems.get(index);
    }

    public static UUID generateUUID(){
        return UUID.randomUUID();
    }

    public static void main(String[] args) {

        CouchDbProperties properties = new CouchDbProperties()
                .setDbName("db-ticket")
                .setCreateDbIfNotExist(true)
                .setProtocol("http")
                .setHost("127.0.0.1")
                .setPort(5984)
                .setMaxConnections(100)
                .setConnectionTimeout(0);

        CouchDbClient dbClient = new CouchDbClient(properties);
        for(int i = 0; i < 1000; i++){
            JsonObject json = new JsonObject();
            json.addProperty("_id", Integer.toString(i));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();

            String title = "problem_" + Integer.toString(i);
            json.addProperty("t_title", title);
            String des = getProblem();
            UUID userid = generateUUID();
            json.addProperty("t_description", des);
            json.addProperty("t_time", dateFormat.format(cal.getTime()));
            json.addProperty("t_status", "open");
            json.addProperty("t_userid", userid.toString());
            json.addProperty("t_agentid", 0);
            json.addProperty("t_replay", "");
            dbClient.save(json);
        }
//
//        for(int i = 0; i < 10; i++){
//            if(dbClient.contains(Integer.toString(i))) {
//                JsonObject json = dbClient.find(JsonObject.class, Integer.toString(i));
//                json.addProperty("t_status", Integer.toString(100));
//                dbClient.update(json);
//            }
//        }

        dbClient.shutdown();
    }

}
