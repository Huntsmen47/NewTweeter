package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.ConcreteDaoFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.dto.FeedDTO;


public class UpdateFeedQueue implements RequestHandler<SQSEvent,Void> {

    DAOFactory daoFactory;
    Gson gson;


    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        for(SQSEvent.SQSMessage msg:input.getRecords()){
            FeedDAO feedDAO = getDaoFactory().makeFeedDAO();
            Type listOfMyClassObject = new TypeToken<ArrayList<FeedDTO>>() {}.getType();
            List<FeedDTO> feedDTOList = getGson().fromJson(msg.getBody(),listOfMyClassObject);
            feedDAO.addFeedBatch(feedDTOList);
        }

        return null;
    }

    private DAOFactory getDaoFactory(){
        if(daoFactory == null){
            daoFactory = new ConcreteDaoFactory();
        }
        return daoFactory;
    }

    private Gson getGson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }
}
