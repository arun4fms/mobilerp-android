package com.mobilerp.pathwaysstudio.mobilerp;

/**
 * Created by mloki on 02/03/2017.
 */

/**
 * This interfaces allows us to trigger changes in the UI when a WebService
 * has finished doing a request
 */
public interface RequestResponse {

    /**
     * Triggers
     * @param result
     */
    void onResponseReceived(RequestResult result);
}
