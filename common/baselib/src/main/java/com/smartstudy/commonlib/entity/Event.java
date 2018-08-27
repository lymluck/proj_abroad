package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 17/7/7.
 */

public class Event {

    public static class MsgXxdEvent {
        private int mUnread;

        public MsgXxdEvent(int unread) {
            this.mUnread = unread;
        }

        public int getUnRead() {
            return mUnread;
        }
    }

    public static class MsgMeiQiaEvent {
        private int mUnread;

        public MsgMeiQiaEvent(int unread) {
            this.mUnread = unread;
        }

        public int getUnRead() {
            return mUnread;
        }
    }

    public static class MsgQaEvent {
        private int mUnread;

        public MsgQaEvent(int unread) {
            this.mUnread = unread;
        }

        public int getUnRead() {
            return mUnread;
        }
    }

    public static class HomeFilterEvent {
        private boolean isAbroadPlanAvailable;

        public HomeFilterEvent(boolean isAbroadPlanAvailable) {
            this.isAbroadPlanAvailable = isAbroadPlanAvailable;
        }

        public boolean isAbroadPlanAvailable() {
            return this.isAbroadPlanAvailable;
        }
    }

    public static class CompleteInfoEvent {
        public CompleteInfoEvent() {
        }
    }
}
