package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedResponse extends PagedResponse{
    private List<Status> feed;

    FeedResponse(String message) {
        super(false, message, false);
    }

    public FeedResponse(boolean hasMorePages, List<Status> feed) {
        super(true, hasMorePages);
        this.feed = feed;
    }

    public List<Status> getFeed() {
        return feed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feed);
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FeedResponse that = (FeedResponse) param;

        return (Objects.equals(feed, that.feed) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
