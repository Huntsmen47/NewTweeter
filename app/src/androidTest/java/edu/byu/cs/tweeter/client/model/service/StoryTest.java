package edu.byu.cs.tweeter.client.model.service;

import android.os.Looper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;


import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoryTest {

    private CountDownLatch countDownLatch;

    private StatusService statusServiceSpy;

    private StoryPageObserver observer;

    private User user;

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @BeforeAll
    public void setup(){
        statusServiceSpy = Mockito.spy(new StatusService());
        observer = new StoryPageObserver();
        user = new User("FirstName", "LastName", null);
        resetCountDownLatch();
    }

    private class StoryPageObserver implements PageObserver {

        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;


        @Override
        public void getPaging(List items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = items;
            this.hasMorePages = hasMorePages;

            countDownLatch.countDown();
        }


        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

    }

    @Test
    public void getStory_valid_correctResponse() throws InterruptedException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                statusServiceSpy.loadMoreItems(user,3,null,observer);
                Looper.loop();
            }
        }).start();


        awaitCountDownLatch();

        List<Status> actualStory = observer.getStory();
        List<Status> expectedStory = FakeData.getInstance().getFakeStatuses().subList(0,3);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertEquals(expectedStory,actualStory);
        Assertions.assertTrue(observer.getHasMorePages());
    }


}


