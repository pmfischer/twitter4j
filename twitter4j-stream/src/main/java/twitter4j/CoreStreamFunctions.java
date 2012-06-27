package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.json.JSONObjectType;

abstract class CoreStreamFunctions {

    protected static final Logger logger = Logger.getLogger(StatusStreamImpl.class);

	
    protected final Configuration CONF;
    protected z_T4JInternalFactory factory;

	public CoreStreamFunctions(Configuration conf) {
		this.CONF = conf;
		this.factory = new z_T4JInternalJSONImplFactory(conf);
	}
	
    protected void onSender(JSONObject json) throws TwitterException {
        logger.warn("Unhandled event: onSender");
    }

    protected void onStatus(JSONObject json) throws TwitterException {
        logger.warn("Unhandled event: onStatus");
    }

    protected void onDirectMessage(JSONObject json) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDirectMessage");
    }

    protected void onDelete(JSONObject json) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onDelete");
    }

    protected void onLimit(JSONObject json) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onLimit");
    }

    protected void onScrubGeo(JSONObject json) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onScrubGeo");
    }

    protected void onFriends(JSONObject json) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onFriends");
    }

    protected void onFavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        logger.warn("Unhandled event: onFavorite");
    }

    protected void onUnfavorite(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        logger.warn("Unhandled event: onUnfavorite");
    }

    protected void onRetweet(JSONObject source, JSONObject target, JSONObject targetObject) throws TwitterException {
        logger.warn("Unhandled event: onRetweet");
    }

    protected void onFollow(JSONObject source, JSONObject target) throws TwitterException {
        logger.warn("Unhandled event: onFollow");
    }

    protected void onUnfollow(JSONObject source, JSONObject target) throws TwitterException {
        logger.warn("Unhandled event: onUnfollow");
    }

    protected void onUserListMemberAddition(JSONObject addedMember, JSONObject owner, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberAddition");
    }

    protected void onUserListMemberDeletion(JSONObject deletedMember, JSONObject owner, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListMemberDeletion");
    }

    protected void onUserListSubscription(JSONObject source, JSONObject owner, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListSubscription");
    }

    protected void onUserListUnsubscription(JSONObject source, JSONObject owner, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUnsubscription");
    }

    protected void onUserListCreation(JSONObject source, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListCreation");
    }

    protected void onUserListUpdated(JSONObject source, JSONObject userList) throws TwitterException, JSONException {
        logger.warn("Unhandled event: onUserListUpdated");
    }

    protected void onUserListDestroyed(JSONObject source, JSONObject userList) throws TwitterException {
        logger.warn("Unhandled event: onUserListDestroyed");
    }

    protected void onUserUpdate(JSONObject source, JSONObject target) throws TwitterException {
        logger.warn("Unhandled event: onUserUpdate");
    }

    protected void onBlock(JSONObject source, JSONObject target) throws TwitterException {
        logger.warn("Unhandled event: onBlock");
    }

    protected void onUnblock(JSONObject source, JSONObject target) throws TwitterException {
        logger.warn("Unhandled event: onUnblock");
    }

    protected void onException(Exception e) {
        logger.warn("Unhandled event: ", e.getMessage());
    }
    protected Status asStatus(JSONObject json) throws TwitterException {
        Status status = factory.createStatus(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(status, json);
        }
        return status;
    }

    protected DirectMessage asDirectMessage(JSONObject json) throws TwitterException {
        DirectMessage directMessage;
        try {
            directMessage = factory.createDirectMessage(json.getJSONObject("direct_message"));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(directMessage, json);
        }
        return directMessage;
    }

    protected long[] asFriendList(JSONObject json) throws TwitterException {
        JSONArray friends;
        try {
            friends = json.getJSONArray("friends");
            long[] friendIds = new long[friends.length()];
            for (int i = 0; i < friendIds.length; ++i) {
                friendIds[i] = Long.parseLong(friends.getString(i));
            }
            return friendIds;
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    protected User asUser(JSONObject json) throws TwitterException {
        User user = factory.createUser(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(user, json);
        }
        return user;
    }

    protected UserList asUserList(JSONObject json) throws TwitterException {
        UserList userList = factory.createAUserList(json);
        if (CONF.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(userList, json);
        }
        return userList;
    }

    protected void handleTweetTypes(String line) throws JSONException,
    TwitterException {
    	JSONObject json = new JSONObject(line);
    	JSONObjectType jsonObjectType = JSONObjectType.determine(json);
    	if (logger.isDebugEnabled()) {
    		logger.debug("Received:", CONF.isPrettyDebugEnabled() ? json.toString(1) : json.toString());
    	}
    	if (JSONObjectType.SENDER == jsonObjectType) {
    		onSender(json);
    	} else if (JSONObjectType.STATUS == jsonObjectType) {
    		onStatus(json);
    	} else if (JSONObjectType.DIRECT_MESSAGE == jsonObjectType) {
    		onDirectMessage(json);
    	} else if (JSONObjectType.DELETE == jsonObjectType) {
    		onDelete(json);
    	} else if (JSONObjectType.LIMIT == jsonObjectType) {
    		onLimit(json);
    	} else if (JSONObjectType.SCRUB_GEO == jsonObjectType) {
    		onScrubGeo(json);
    	} else if (JSONObjectType.FRIENDS == jsonObjectType) {
    		onFriends(json);
    	} else if (JSONObjectType.FAVORITE == jsonObjectType) {
    		onFavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.UNFAVORITE == jsonObjectType) {
    		onUnfavorite(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.RETWEET == jsonObjectType) {
    		// note: retweet events also show up as statuses
    		onRetweet(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.FOLLOW == jsonObjectType) {
    		onFollow(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.UNFOLLOW == jsonObjectType) {
    		onUnfollow(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.USER_LIST_MEMBER_ADDED == jsonObjectType) {
    		onUserListMemberAddition(json.getJSONObject("target"), json.getJSONObject("source"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.USER_LIST_MEMBER_DELETED == jsonObjectType) {
    		onUserListMemberDeletion(json.getJSONObject("target"), json.getJSONObject("source"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.USER_LIST_SUBSCRIBED == jsonObjectType) {
    		onUserListSubscription(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.USER_LIST_UNSUBSCRIBED == jsonObjectType) {
    		onUserListUnsubscription(json.getJSONObject("source"), json.getJSONObject("target"), json.getJSONObject("target_object"));
    	} else if (JSONObjectType.USER_LIST_CREATED == jsonObjectType) {
    		onUserListCreation(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.USER_LIST_UPDATED == jsonObjectType) {
    		onUserListUpdated(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.USER_LIST_DESTROYED == jsonObjectType) {
    		onUserListDestroyed(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.USER_UPDATE == jsonObjectType) {
    		onUserUpdate(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.BLOCK == jsonObjectType) {
    		onBlock(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else if (JSONObjectType.UNBLOCK == jsonObjectType) {
    		onUnblock(json.getJSONObject("source"), json.getJSONObject("target"));
    	} else {
    		logger.warn("Received unknown event:", CONF.isPrettyDebugEnabled() ? json.toString(1) : json.toString());
    	}
    }

	
}
