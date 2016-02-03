package saysai.app.easyweather.Utils;

/**
 * Created by isay on 2/3/2016.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
