package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.StatusService;

public class MainPresenterUnitTest {
    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;

    private MainPresenter mainPresenterSpy;

    private String post;

    private Answer<Void> answer;


    private void initAnswer(int option){
        answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.MainObserver observer = invocation.getArgument(1,
                        MainPresenter.MainObserver.class);
                String passedInPost = invocation.getArgument(0);
                assert(post.equals(passedInPost));
                if(option == 0){
                    observer.postStatus();
                }else{
                    observer.handleFailure("Failed to post the status");
                }
                return null;
            }
        };
        Mockito.doAnswer(answer).
                when(mockStatusService).makePost(Mockito.any(),Mockito.any(),
                        Mockito.any(),Mockito.any());
    }


    private void verifyPresenterMethodCalls(){
        mainPresenterSpy.onStatusPosted(post);
        Mockito.verify(mockView).postStatusToast();
        Mockito.verify(mainPresenterSpy).parseURLs(post);
        Mockito.verify(mainPresenterSpy).parseMentions(post);
    }



    @BeforeEach
    public void setup(){
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        post = "Some Post";
    }

    @Test
    public void testPostStatus_postStatusSuccessful(){

        initAnswer(0);
        verifyPresenterMethodCalls();
    }

    @Test
    public void testPostStatus_postStatusError(){
              initAnswer(1);
              verifyPresenterMethodCalls();
              Mockito.verify(mockView).displayMessage(Mockito.startsWith("Failed to post the status"));
    }

    @Test
    public void testPostStatus_postStatusException(){
        Mockito.doThrow(new RuntimeException("Some Problem")).when(mockStatusService).
                makePost(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
        verifyPresenterMethodCalls();
        Mockito.verify(mockView).displayMessage(Mockito.startsWith("Failed to post the status because of exception: "));
    }



}
