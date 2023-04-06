package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.dao_interfaces.FeedDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class ConcreteFeedDAO implements FeedDAO {
@Override
    public Pair<List<Status>,Boolean> getFeed(int pageLimit, String targetUser, Status lastStatus) {
        assert pageLimit > 0;
        assert targetUser != null;

        List<Status> allStatus = getDummyStatus();
        List<Status> responseStatuses = new ArrayList<>(pageLimit);

        boolean hasMorePages = false;

        if(pageLimit > 0) {
            if (allStatus != null) {
                int statusIndex = getStatusStartingIndex(lastStatus, allStatus);

                for(int limitCounter = 0; statusIndex < allStatus.size() && limitCounter <
                        pageLimit; statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatus.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatus.size();
            }
        }

        return new Pair<List<Status>,Boolean>(responseStatuses,hasMorePages);
    }

    private List<Status> getDummyStatus() {
        return getFakeData().getFakeStatuses();
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private int getStatusStartingIndex(Status lastStatus, List<Status> allStatus) {
        int statusIndex = 0;
        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatus.size(); i++) {
                if(lastStatus.equals(allStatus.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }
}
