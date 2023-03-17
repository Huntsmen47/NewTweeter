package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response{

    private boolean isFollowFlag;

    IsFollowerResponse(String message) {
        super(false, message);
    }

    public IsFollowerResponse(boolean isFollowFlag) {
        super(true, null);
        this.isFollowFlag = isFollowFlag;
    }

    public boolean isFollowFlag() {
        return isFollowFlag;
    }
}
