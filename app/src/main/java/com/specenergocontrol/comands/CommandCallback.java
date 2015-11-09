package com.specenergocontrol.comands;

import java.io.Serializable;

/**
 * Created by Комп on 16.12.2014.
 */
public interface CommandCallback extends Serializable {
    public void commandSuccessExecuted(Serializable result);
    public void commandExecutedWithError(int errorCode);
}
