package twitter4j;

import twitter4j.internal.org.json.JSONObject;

public interface JSONListener extends StreamListener {
	/**
	 * Called for a events in a Twitter stream. "Raw" JSON for futher processing
	 * @param jo a JSONObject containing the "tree"-like structure of the event
	 */
	void onJSON(JSONObject jo);

}
