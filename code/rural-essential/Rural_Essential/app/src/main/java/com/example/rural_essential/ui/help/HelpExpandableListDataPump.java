package com.example.rural_essential.ui.help;

import com.example.rural_essential.R;
import com.example.rural_essential.ui.model.Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpExpandableListDataPump {

    public static HashMap<String, List<Help>> getData() {
        HashMap<String, List<Help>> expandableListDetail = new HashMap<String, List<Help>>();
        // add information to child
        List<Help> training = new ArrayList<Help>();
        training.add(new Help("The training is the first screen displayed in the application.",-1));
        training.add(new Help("The user would be able to start the training from this page.",R.drawable.picture1));
        training.add(new Help("After starting the journey, the user would get voice alerts when over speeding. " +
                "Also, there would be voice alerts when the user is near specific areas like “accident-prone areas” as well as “school”.",R.drawable.picture2));
        training.add(new Help("After the user clicks the “Stop” button, a notification is generated. ",-1));
        training.add(new Help("*The function of this section are highly depend on GPS and network.",-1));



        List<Help> news = new ArrayList<Help>();
        news.add(new Help("The user can access the traffic news using the “News” tab.",R.drawable.picture6));

        List<Help> summary = new ArrayList<Help>();
        summary.add(new Help("The user can access the driving report from the “Summary” tab which contains all the driving reports of the user.",R.drawable.picture3));
        summary.add(new Help("",R.drawable.picture4));
        summary.add(new Help("", R.drawable.picture5));
        summary.add(new Help("*The reports are only stored at the user's end. Nobody except the user has access to these reports.",-1));
        List<Help> quiz = new ArrayList<Help>();

        quiz.add( new Help("The user can improve driving knowledge using a driving quiz in the “Quiz” tab. " +
                "These can be attempted as many times as the user likes. " +
                "A random set of questions are prompted every time the user attempts the quiz.",R.drawable.picture7));
        quiz.add(new Help("",R.drawable.picture8));
        quiz.add(new Help("", R.drawable.picture9));

        //add children to group
        expandableListDetail.put("Training", training);
        expandableListDetail.put("News", news);
        expandableListDetail.put("Summary", summary);
        expandableListDetail.put("Quiz", quiz);
        return expandableListDetail;
    }

}
