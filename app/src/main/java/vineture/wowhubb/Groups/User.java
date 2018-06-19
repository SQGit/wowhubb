package vineture.wowhubb.Groups;



    public class User {

        private Userid userid;
        private String _id;
        private String joinedAt;

        public Userid getUserid() {
            return userid;
        }

        public void setUserid(Userid userid) {
            this.userid = userid;
        }

        public String getId() {
            return _id;
        }

        public void setId(String id) {
            this._id = id;
        }

        public String getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(String joinedAt) {
            this.joinedAt = joinedAt;
        }

    }
