package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.FollowButtonObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter{


    private User selectedUser;


    public interface MainView extends Presenter.View{
        void setVisibilityFollowButton(boolean value);

        void formatFollowButton(boolean isFollower);

        void setFollowerCount(int count);

        void setFollowingCount(int count);

        void setTheFollowButton(boolean b);

        void setEnabledFollowButton(boolean b);

        void logout();

        void logoutToast();

        void cancelPostToast();

        void postStatusToast();
    }

    public MainPresenter(MainView view){
        super(view);
    }

    @Override
    public void processFailure() {
        ((MainView)view).setEnabledFollowButton(true);
    }


    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void addFollowingButton() {

        if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            ((MainView)view).setVisibilityFollowButton(false);
        } else {
            ((MainView)view).setVisibilityFollowButton(true);
            getFollowService().addFollowingButton(selectedUser,new MainObserver());
        }

    }

    public void updateSelectedUserFollowAndFollower() {

        getFollowService().updateSelectedUserFollowAndFollower(selectedUser,new MainObserver());
    }

    public void checkUserForNull() {
        if (selectedUser == null) {
            throw new RuntimeException("User not passed to activity");
        }
    }

    public void toggleFollowButton(boolean value) {
        if (value) {
            getFollowService().changeToUnfollow(selectedUser,new MainObserver());
            view.displayMessage("Removing " + selectedUser.getName() + "...");
        } else {
            getFollowService().changeToFollow(selectedUser,new MainObserver());
            view.displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }

    public boolean logout() {
        ((MainView)view).logoutToast();
            getUserService().logout(new MainObserver());
            return true;

    }

    public void onStatusPosted(String post) {
        try {
            ((MainView)view).postStatusToast();
            List<String> parsedUrls = parseURLs(post);
            List<String> parsedMentions = parseMentions(post);
            getStatusService().makePost(post,new MainObserver(),parsedUrls,parsedMentions);
        } catch (Exception ex) {
          //  Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public class MainObserver extends Observer implements FollowButtonObserver, CountObserver, IsFollowObserver,
            SimpleTaskObserver {

        @Override
        public void processLogout() {
            ((MainView)view).logout();
        }

        @Override
        public void postStatus() {
            ((MainView)view).cancelPostToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void updateFollowButton(boolean value) {
            updateSelectedUserFollowAndFollower();
            ((MainView)view).setTheFollowButton(value);
            ((MainView)view).setEnabledFollowButton(true);
        }

        @Override
        public void setFollowerCount(int count) {
            ((MainView)view).setFollowerCount(count);
        }

        @Override
        public void setFollowingCount(int count) {
            ((MainView)view).setFollowingCount(count);
        }

        @Override
        public void formatFollowButton(boolean isFollower) {
            ((MainView)view).formatFollowButton(isFollower);
        }
    }

}
