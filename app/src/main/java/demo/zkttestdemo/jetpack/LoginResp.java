package demo.zkttestdemo.jetpack;

/**
 * Created by zkt on 19/07/02.
 * Description:
 */
public class LoginResp {

    /**
     * msg : 登录成功
     * data : {"user_id":1,"user_token":"1ae738af773d7b3b12f1cc364075230c"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 1
         * user_token : 1ae738af773d7b3b12f1cc364075230c
         */

        private String user_id;
        private String user_token;
        private String rongcloud_token;
        private String nickname;
        private String headimg;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getRongcloud_token() {
            return rongcloud_token;
        }

        public void setRongcloud_token(String rongcloud_token) {
            this.rongcloud_token = rongcloud_token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_token() {
            return user_token;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }
    }
}
