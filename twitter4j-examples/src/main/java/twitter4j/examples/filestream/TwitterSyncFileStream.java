/**
 * Example for reading completely synchronously from a file, no unlimited buffering and threading
 * 
 */
package twitter4j.examples.filestream;
import java.io.FileInputStream;
import java.io.IOException;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusStream;
import twitter4j.SyncTweetFileStream;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterSyncFileStream {
    public static void main(String[] args) throws TwitterException, IOException {
       
        
        Configuration conf = new ConfigurationBuilder().build();
        
        StatusListener listener = new StatusListener() {
        	 int statCounter = 0;
        	 int retweetCounter =0;
             int locCounter = 0;
             int deleteCounter = 0;
             
            public void onStatus(Status status) {
            	statCounter++;
            	if (status.getPlace() != null || status.getGeoLocation() != null)
            		locCounter++;
            	if (status.isRetweet()) {
            		retweetCounter++;
            		//System.out.println("Is retweet");
            	}
            	if (statCounter%50==0)
            		System.out.println("Status: No "+statCounter+"with Loc "+locCounter+" retweet: "+retweetCounter);

            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            	deleteCounter++;
            	if (deleteCounter%10 == 0)
            		System.out.println("Delete: No "+deleteCounter);
//                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            public void onException(Exception ex) {
            }
        };
        StatusStream statStream = 
                new SyncTweetFileStream(new FileInputStream("D://Forschung/playground/2012-06-27__003004.json"), conf);

        while (true) {
        	statStream.next(listener);
        }
    }

}
