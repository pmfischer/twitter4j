package twitter4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import twitter4j.conf.Configuration;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.z_T4JInternalParseUtil;

public class SyncTweetFileStream extends CoreStreamFunctions implements StatusStream{
	InputStream is;
	BufferedReader br;
    
    public SyncTweetFileStream (InputStream stream, Configuration conf) throws IOException {
    	super(conf);
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }

    
    protected void handleNextElement() throws TwitterException {
    	String line = null;
    	 try {
            line = br.readLine();
            if (null == line) {
                //invalidate this status stream
               
				
            }
    	 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (line != null && line.length() > 0) {
            	try {
            		handleTweetTypes(line);
            	} catch (Exception ex) {
            		onException(ex);
            	}
            }
    }
        protected StreamListener[] listeners;


        
        /**
         * Reads next status from this stream.
         *
         * @param listener a StatusListener implementation
         * @throws TwitterException      when the end of the stream has been reached.
         * @throws IllegalStateException when the end of the stream had been reached.
         */
        public void next(StatusListener listener) throws TwitterException {
            StreamListener[] list = new StreamListener[1];
            list[0] = listener;
            this.listeners = list;
            handleNextElement();
        }

        public void next(StreamListener[] listeners) throws TwitterException {
            this.listeners = listeners;
            handleNextElement();
        }

        protected void onStatus(JSONObject json) throws TwitterException {
            for (StreamListener listener : listeners) {
                ((StatusListener) listener).onStatus(asStatus(json));
            }
        }

        protected void onDelete(JSONObject json) throws TwitterException, JSONException {
            for (StreamListener listener : listeners) {
                JSONObject deletionNotice = json.getJSONObject("delete");
                if (deletionNotice.has("status")) {
                    ((StatusListener) listener).onDeletionNotice(new StatusDeletionNoticeImpl(deletionNotice.getJSONObject("status")));
                } else {
                    JSONObject directMessage = deletionNotice.getJSONObject("direct_message");
                    ((UserStreamListener) listener).onDeletionNotice(z_T4JInternalParseUtil.getLong("id", directMessage)
                            , z_T4JInternalParseUtil.getLong("user_id", directMessage));
                }
            }
        }

        protected void onLimit(JSONObject json) throws TwitterException, JSONException {
            for (StreamListener listener : listeners) {
                ((StatusListener) listener).onTrackLimitationNotice(z_T4JInternalParseUtil.getInt("track", json.getJSONObject("limit")));
            }
        }

        protected void onScrubGeo(JSONObject json) throws TwitterException, JSONException {
            JSONObject scrubGeo = json.getJSONObject("scrub_geo");
            for (StreamListener listener : listeners) {
                ((StatusListener) listener).onScrubGeo(z_T4JInternalParseUtil.getLong("user_id", scrubGeo)
                        , z_T4JInternalParseUtil.getLong("up_to_status_id", scrubGeo));
            }

        }

        public void onException(Exception e) {
            for (StreamListener listener : listeners) {
                listener.onException(e);
            }
        }

		@Override
		public void close() throws IOException {
			br.close();
		}

        
}
