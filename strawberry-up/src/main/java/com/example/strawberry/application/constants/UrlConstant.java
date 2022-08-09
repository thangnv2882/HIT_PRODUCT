package com.example.strawberry.application.constants;

public class UrlConstant {
    public static class User {
        public User() {
        }

        private static final String PRE_FIX = "/users";
        public static final String DATA_USER = PRE_FIX;
        public static final String DATA_USER_ID = PRE_FIX + "/{id}";
        public static final String DATA_USER_REGISTER = PRE_FIX + "/register";
        public static final String DATA_USER_REGISTER_ACTIVE = PRE_FIX + "/register/{id}/active";
        public static final String DATA_USER_RESEND_CODE = PRE_FIX + "/register/{id}/resend-code";
        public static final String DATA_USER_FORGET_PASSWORD = PRE_FIX + "/forget-password";
        public static final String DATA_USER_CHANGE_PASSWORD = PRE_FIX + "/{id}/change-password";
        public static final String DATA_USER_UPDATE_USER = PRE_FIX + "/{id}/update-user";
        public static final String DATA_USER_DELETE_USER = PRE_FIX + "/{id}/delete-user";
        public static final String DATA_USER_UPDATE_AVATAR = PRE_FIX + "/{id}/update-avatar";
        public static final String DATA_USER_UPDATE_COVER = PRE_FIX + "/{id}/update-cover";
        public static final String DATA_USER_GET_POSTS = PRE_FIX + "/{id}/posts";
        public static final String DATA_USER_GET_POST_ACCESS = PRE_FIX + "/{idUser}/posts/{access}";
        public static final String DATA_USER_GET_GROUPS = PRE_FIX + "/{id}/groups";
        public static final String DATA_USER_GET_FRIENDS = PRE_FIX + "/{id}/friends";
        public static final String DATA_USER_FRIENDS_REQUEST = PRE_FIX + "/{id}/friend-requests";
        public static final String DATA_IMAGES_OF_USER = PRE_FIX + "/{id}/images";
        public static final String DATA_VIDEOS_OF_USER = PRE_FIX + "/{id}/videos";

    }

    public static class Post {
        public Post() {
        }

        private static final String PRE_FIX = "/posts";
        public static final String DATA_POST = PRE_FIX;
        public static final String DATA_POST_GET_POST = PRE_FIX + "/{idPost}";
        public static final String DATA_POST_CREATE_POST = PRE_FIX + "/{idUser}/create-post";
        public static final String DATA_POST_CREATE_IN_GROUP = PRE_FIX + "/{idUser}/{idGroup}/create-post-in-group";
        public static final String DATA_POST_UPDATE_POST = PRE_FIX + "/{idUserFix}/{idPost}/update-post";
        public static final String DATA_POST_DELETE_POST = PRE_FIX + "/{idUserFix}/{idPost}/delete-post";
        public static final String DATA_POST_GET_IMAGES = PRE_FIX + "/{idPost}/images";
        public static final String DATA_POST_GET_VIDEOS = PRE_FIX + "/{idPost}/videos";
        public static final String DATA_POST_GET_COMMENTS = PRE_FIX + "/{idPost}/comments";
        public static final String DATA_POST_FIND_BY_CONTENT = PRE_FIX + "/find-by-content";

    }

    public static class Comment {
        public Comment() {
        }

        private static final String PRE_FIX = "/comments";
        public static final String DATA_COMMENT = PRE_FIX;
        public static final String DATA_COMMENT_WRITE_IN_POST = PRE_FIX + "/{idUser}/{idPost}/write-comment";
        public static final String DATA_COMMENT_WRITE_IN_COMMENT = PRE_FIX + "/{idUser}/{idCommentParent}/write-comment-child";
        public static final String DATA_COMMENT_UPDATE_COMMENT = PRE_FIX + "/{idUserFix}/{idComment}/update-comment";
        public static final String DATA_COMMENT_DETETE_COMMENT = PRE_FIX + "/{idUserFix}/{idComment}/delete-comment";
        public static final String DATA_COMMENT_GET_COMMENT_CHILD = PRE_FIX + "/{idCommentParent}/comments-child";

    }

    public static class Group {
        public Group() {
        }

        private static final String PRE_FIX = "/groups";
        public static final String DATA_GROUP = PRE_FIX;
        public static final String DATA_GROUP_BY_ACCESS = PRE_FIX + "/get-by-access";
        public static final String DATA_GROUP_ALL_USER = PRE_FIX + "/{idGroup}/users";
        public static final String DATA_GROUP_CREATE_GROUP = PRE_FIX + "/{idUser}/create-group";
        public static final String DATA_GROUP_DELETE_GROUP = PRE_FIX + "/{idGroup}/{idUser}/delete-group";
        public static final String DATA_GROUP_ADD_MEMBER = PRE_FIX + "/{idGroup}/{idUser}/add-user-to-group";
        public static final String DATA_GROUP_DELETE_MEMBER = PRE_FIX + "/{idGroup}/{idUserDelete}/{idUser}/delete-user-from-group";
        public static final String DATA_GROUP_GET_POSTS = PRE_FIX + "/{idGroup}/{idUser}/get-post";

    }

    public static class FriendShip {
        public FriendShip() {
        }

        private static final String PRE_FIX = "/friendships";
        public static final String DATA_FRIEND = PRE_FIX;
        public static final String DATA_FRIEND_ADD_FRIEND = PRE_FIX + "/{idUserSender}/{idUserReceiver}/add-friend";
        public static final String DATA_FRIEND_CANCEL_ADD_FRIEND = PRE_FIX + "/{idUserSender}/{idUserReceiver}/cancel-add-friend";
        public static final String DATA_FRIEND_ACCEPT_FRIEND = PRE_FIX + "/{idUserReceiver}/{idUserSender}/accept-friend";
        public static final String DATA_FRIEND_UN_FRIEND = PRE_FIX + "/{idUserSender}/{idUserReceiver}/un-friend";

    }

    public static class Reaction {
        public Reaction() {
        }

        private static final String PRE_FIX = "/reactions";
        public static final String DATA_REACTION = PRE_FIX;
        public static final String DATA_REACTION_SET_REACTION = PRE_FIX + "/set-reaction";
        public static final String DATA_REACTION_GET_COUNT = PRE_FIX + "/{idPost}/get-count";

    }

    public static class Auth {
        private Auth() {
        }

        private static final String PRE_FIX = "/auth";
        public static final String LOGIN = PRE_FIX + "/login";
//        public static final String SIGNUP = PRE_FIX + "/signup";
//        public static final String VALIDATE = PRE_FIX + "/validate";
//        public static final String LOGOUT = PRE_FIX + "/logout/{id}";
    }

    public static class Notification {
        public Notification() {
        }

        private static final String PRE_FIX = "/notifications";
        public static final String DATA_NOTIFICATION = PRE_FIX;
        public static final String DATA_NOTIFICATION_ALL = PRE_FIX + "/{idUser}";
        public static final String DATA_NOTIFICATION_CREATE = PRE_FIX + "/{idUser}/create-notification";
        public static final String DATA_NOTIFICATION_MARK_AS_READ = PRE_FIX + "/{idNoti}/mark-as-read";
        public static final String DATA_NOTIFICATION_MARK_AS_UNREAD = PRE_FIX + "/{idNoti}/mark-as-unread";
        public static final String DATA_NOTIFICATION_DELETE = PRE_FIX + "/{idNoti}/delete-notification";

    }
}
