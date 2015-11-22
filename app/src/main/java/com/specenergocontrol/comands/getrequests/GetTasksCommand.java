package com.specenergocontrol.comands.getrequests;

import android.content.Context;

import com.specenergocontrol.model.User;
import com.specenergocontrol.parser.TasksParser;
import com.specenergocontrol.utils.Constants;
import com.specenergocontrol.utils.StoreUtils;

/**
 * Created by Комп on 07.08.2015.
 */
public class GetTasksCommand extends GetRequest {


    private final String url = Constants.BASE_URL + "/tasks";

    public GetTasksCommand(Context baseContext) {
        super(baseContext);
        setUrl(String .format(url));
        setParser(new TasksParser());
    }
}
