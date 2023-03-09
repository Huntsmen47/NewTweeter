package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SendUserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    private static final int PAGE_SIZE = 10;

    private T lastItem;

    private boolean hasMorePages;

    private boolean isLoading = false;

    public interface PagedView extends Presenter.View{
        void addLoadingFooter();

        void removeLoadingFooter();

        void sendUser(User user);
    }

    public PagedPresenter(View view) {
        super(view);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            ((PagedView)view).addLoadingFooter();

            serviceLoad(user,PAGE_SIZE,lastItem);

        }
    }

    @Override
    public void processFailure() {
        isLoading = false;
        ((PagedView)view).removeLoadingFooter();
    }

    public void getUser(String userAlias) {
        userService.getUser(userAlias, new PageClassObserver());
        view.displayMessage("Getting user's profile...");
    }

    public abstract void serviceLoad(User user, int pageSize, T lastItem);

    public abstract void addMoreItems(List<T> items);


    public class PageClassObserver extends Observer implements PageObserver, SendUserObserver {
        @Override
        public void getPaging(List items, boolean hasMorePages) {
            List<T> itemsList = (List<T>) items;
            isLoading = false;
            ((PagedView)view).removeLoadingFooter();
            lastItem = (itemsList.size() > 0) ? itemsList.get(itemsList.size() - 1) : null;
            setHasMorePages(hasMorePages);
            addMoreItems(itemsList);
        }

        @Override
        public void sendUser(User user) {
            ((PagedView)view).sendUser(user);
        }
    }
}
