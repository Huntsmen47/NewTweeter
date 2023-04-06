package edu.byu.cs.tweeter.server.dao.dao_interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dto.StoryDTO;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryDAO {

  Pair<List<StoryDTO>,Boolean> getStory(int pageLimit,String targetUser,StoryDTO lastStatus);

  void postStatus(StoryDTO status);

  boolean update(StoryDTO status);



}
