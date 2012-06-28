package twitter4j;

public interface JSONStream {
	
	   void next(JSONListener listener) throws TwitterException;

}
