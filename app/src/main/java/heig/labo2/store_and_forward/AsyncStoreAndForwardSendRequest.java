package heig.labo2.store_and_forward;
import java.util.LinkedList;
import java.util.List;

import heig.labo2.utils.AsyncSendRequest;
import heig.labo2.utils.CommunicationEventListener;

/**
 * @author Henrik Akesson
 */
class AsyncStoreAndForwardSendRequest implements Runnable {
    private static AsyncStoreAndForwardSendRequest asfsr;
    private CommunicationEventListener celForActivity, celForAsyncTasks;
    private List<String> requests = new LinkedList<>();
    private String url;

    private AsyncStoreAndForwardSendRequest() {
        this.celForAsyncTasks = new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                celForActivity.handleServerResponse(response);
                return true;
            }
        };
    }

    static AsyncStoreAndForwardSendRequest getInstance() {
        if (asfsr == null) {
            asfsr = new AsyncStoreAndForwardSendRequest();
        }
        return asfsr;
    }

    void setCommunicationEventListener(CommunicationEventListener cel) {
        this.celForActivity = cel;
    }

    void addRequest(String request, String url) {
        requests.add(request);
        AsyncSendRequest asyncSendRequest = new AsyncSendRequest(celForAsyncTasks);
        try {
            asyncSendRequest.execute(request, url);
        } catch(Exception e) {
            System.out.println("ERROR FROM STORE AND FORWARD");
            e.printStackTrace();
        } finally {
            requests.remove(request);
        }
    }

    void setURL(String url) {
        this.url = url;
    }

    @Override
    public void run() {

    }
}
