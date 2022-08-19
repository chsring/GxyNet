package com.srwing.net;

import java.util.List;

/**
 * Description:
 * Created by srwing
 * Date: 2022/6/27
 * Email: 694177407@qq.com
 */
public class HomeListEntity extends BaseEntity {

    public List<DataBean> data;

    public class DataBean {
        public int id;
        public int type;
        public String key;
        public Object feed;
    }

    public class FeedBeanReport {
        public int id;
        public String username;
        public String gamename;
        public String pb;
        public String pw;
        public int blackLevel;
        public int whiteLevel;
        public int moveNum;
        public int handicap;
        public double komi;
        public int boardSize;
        public String gameResult;
        public String gameType;
        public int analyzeStatus;
        public int analyzePo;
        public DateTimeBean playTime;
        public DateTimeBean createTime;
        public boolean favourite;
        public boolean deleteFlag;
        public int startMoveNum;
    }

    public class FeedBeanLive {

        public LiveMatchBean liveMatch;
        public String moves;
    }

    public class FeedBeanArticle {

        public int id;
        public String title;
        public String thumbnailLink;
        public String accessLink;
        public String publisher;
        public DateTimeBean publishTime;
    }

    public class LiveMatchBean {
        public String liveId;
        public String name;
        public String pb;
        public String pw;
        public DateTimeBean startTime;
        public String gameResult;
        public int po;
        public int gpuPlanId;
        public int moveNum;
        public String sgf;
        public String creater;
        public double komi;
        public String type;
        public int clusterId;
        public double winrate;
        public int order;
    }
}
