package com.specenergocontrol.comands;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;


import com.specenergocontrol.R;

import org.json.JSONException;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Комп on 16.12.2014.
 */
public class AsyncTaskExecutor {

    public static final int ERROR_CODE_OK = 0;
    public static final int ERROR_CODE_INTERNAL_EXCEPTION = 1;
    public static final int ERROR_CODE_INTERNET_EXCEPTION = 2;
    public static final int ERROR_CODE_BUSSINESS_EXCEPTION = 3;
    public static final int ERROR_CODE_HTTP_CLIENT_EXCEPTION = 4;
    public static final int ERROR_CODE_HTTP_SERVER_EXCEPTION = 5;
    public static final int ERROR_CODE_UNAUTHORIZED = 6;
    public static final int ERROR_CODE_BUSSINESS_RELOGIN_EXCEPTION = 7;
    public static final int ERROR_CODE_SESSION_DIED = 8;


    private static boolean sessionDied = false;

    private Activity activity;
    private ArrayList<DetachebleAsyncTask> asynctaskList = new ArrayList<>();
    private Command lastCommand;
    private CommandCallback lastCallback;


    public void execute(Command command, CommandCallback callback) {
        this.lastCommand = command;
        this.lastCallback = callback;
        DetachebleAsyncTask detachebleAsyncTask = new DetachebleAsyncTask(command, callback);
        asynctaskList.add(detachebleAsyncTask);
        detachebleAsyncTask.execute();
    }

    public void execute(Command command) {
        DetachebleAsyncTask detachebleAsyncTask = new DetachebleAsyncTask(command);
        asynctaskList.add(detachebleAsyncTask);
        detachebleAsyncTask.execute();
    }

    public void attach(Activity activity){
        this.activity = activity;
    }

    public void detach(){
        activity = null;
    }

    public void executeLastRequest() {
        execute(lastCommand, lastCallback);
    }

    public void setSessionDied(boolean sessionDied) {
        this.sessionDied = sessionDied;
    }

    public Activity getActivity() {
        return activity;
    }

    private class DetachebleAsyncTask extends AsyncTask<Void, Void, Pair<Integer, Serializable>>  {

        private Command command;
        private CommandCallback callback;

        public DetachebleAsyncTask (Command command) {
            this.command = command;
        }

        public DetachebleAsyncTask (Command command, CommandCallback callback) {
            this.command = command;
            this.callback = callback;
        }

        @Override
        protected Pair doInBackground(Void... params) {
            if (sessionDied==true)
                return new Pair(ERROR_CODE_SESSION_DIED, null);

            Serializable result;
            //TODO handle exceptions
            try {
                result = command.execute();
            } catch (ParseException e) {
                e.printStackTrace();
                return new Pair(ERROR_CODE_INTERNAL_EXCEPTION, null);
            } catch (ConnectException e) {
                e.printStackTrace();
                return new Pair(ERROR_CODE_INTERNET_EXCEPTION, null);
            } catch (BussinessException e) {
                e.printStackTrace();
                if (e.getCode()!=0){
                    return new Pair(ERROR_CODE_BUSSINESS_RELOGIN_EXCEPTION, e.getCode());
                } else {
                    return new Pair(ERROR_CODE_BUSSINESS_EXCEPTION, e.getMessage());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return new Pair(ERROR_CODE_INTERNAL_EXCEPTION, null);
            } catch (Exception e){
                e.printStackTrace();
                return new Pair(ERROR_CODE_INTERNAL_EXCEPTION, null);
            } catch (Error e) {
                e.printStackTrace();
                return new Pair(ERROR_CODE_INTERNAL_EXCEPTION, null);
            }
            return new Pair(ERROR_CODE_OK, result);
        }

        @Override
        protected void onPostExecute(Pair result) {
            asynctaskList.remove(this);
            if (sessionDied==true)
                callback.commandExecutedWithError(ERROR_CODE_SESSION_DIED);
            if (callback == null || activity == null)
                return;
            int errorCode = (Integer)result.first;
            if (errorCode == ERROR_CODE_OK) {
                callback.commandSuccessExecuted((Serializable)result.second);
            } else {
                if (errorCode == ERROR_CODE_INTERNET_EXCEPTION) {
                    Toast.makeText(command.getContext(), R.string.exception_no_internet, Toast.LENGTH_LONG).show();
                } else if (errorCode == ERROR_CODE_BUSSINESS_EXCEPTION || errorCode == ERROR_CODE_INTERNAL_EXCEPTION) {
                    Toast.makeText(command.getContext(), R.string.error, Toast.LENGTH_LONG).show();
                } else if (errorCode == ERROR_CODE_HTTP_CLIENT_EXCEPTION) {
                    ErrorHandler.parseError(activity, (String)result.second);
                    Toast.makeText(command.getContext(), R.string.exception_client_error, Toast.LENGTH_LONG).show();
                } else if (errorCode == ERROR_CODE_HTTP_SERVER_EXCEPTION) {
                    ErrorHandler.parseError(activity, (String)result.second);
                    Toast.makeText(command.getContext(), R.string.exception_server_error, Toast.LENGTH_LONG).show();
                }
                callback.commandExecutedWithError(errorCode);
            }
        }
    }
}
